/*
Copyright 2008-2014 Gephi
Authors : Eduardo Ramos <eduardo.ramos@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.toolkit.demos.plugins.preview;


import java.awt.Color;
import static java.awt.Color.yellow;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterable;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewMouseEvent;
import static org.gephi.preview.api.PreviewMouseEvent.Button.LEFT;
import static org.gephi.preview.api.PreviewMouseEvent.Button.RIGHT;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;


import java.awt.Color;
import javax.swing.JOptionPane;
import org.gephi.datalab.api.GraphElementsController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewMouseEvent.Button;
import static org.gephi.preview.api.PreviewMouseEvent.Button.RIGHT;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PreviewMouseListener.class)
public class MouseListenerTemplate implements PreviewMouseListener {

  //  @Override
    float start_x;
    float  start_y;
    public static boolean innode;
    PreviewController  previewController;
    private PreviewMouseEvent.Button Left;
    private PreviewMouseEvent.Button Right;
     static ArrayList co=new ArrayList();
        private static  Boolean setHighlight=false;//初始时没有任何点被点亮
        static NodeIterable qq;
       
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
         Graph graph=Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph();
        for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
           
            if (clickingInNode(node, event)) {
              
                 System.out.println(setHighlight);
               if(event.button==Button.LEFT)
               {
                    if(setHighlight==false)
                    {
                     NodeIterable pp=graph.getNeighbors(node);
                           qq=graph.getNeighbors(node);
                     Iterator<Node> cc=pp.iterator();
                     while (cc.hasNext() ) 
                     {
        	       Node n=cc.next();
                
                       System.out.println(n.getColor());
                       co.add(n.getColor());
        	        n.setColor(yellow);   
                       setHighlight=true;
                       System.out.println(n);                   
                      }
                       System.out.println(qq); 
                   }   
                    
                    else if(setHighlight==true){
                      Iterator<Node> cc2=qq.iterator();
                        int i=0;
                        System.out.println(i);
                           System.out.println(qq);   
                           System.out.println(cc2);   
                        while (cc2.hasNext()) {
        	        Node nn=cc2.next();
               
        	      nn.setColor((Color) co.get(i));   
                       i++;
                    
                   }
                      co.clear();
              
                        NodeIterable pp2=graph.getNeighbors(node);
                        qq=graph.getNeighbors(node);
                        Iterator<Node> cc3=pp2.iterator();
                        while (cc3.hasNext() ) {
        	        Node n=cc3.next();                                    
                        co.add(n.getColor());
        	        n.setColor(yellow);                                                                      
               }                         
               }
               } 
               
             if(event.button==Button.RIGHT)
                 {
                     GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
                     PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
                     PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
                 
                    PreviewModel previewModelpreview=previewController.getModel();
                    DirectedGraph graph2=graphModel.getDirectedGraph();
                    GraphElementsController gec=Lookup.getDefault().lookup(GraphElementsController.class);    
                    Node[] constArray=graphModel.getGraph().getNodes().toArray();           ///存储图中节点ID号的数组，直接寻址数组
                  
                     for (Node node2 : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) 
                     {
                         if (clickingInNode(node2, event)) 
                         {                     
                             JOptionPane.showConfirmDialog(null, "Node will be deleted,Continue? ","Delete Node ID "+node.getId(), JOptionPane.YES_NO_OPTION); 
                             graph2.readUnlockAll();                                            //这行代码很关键，在写图数据之前必须解除对图数据的读同步锁             
                             gec.deleteNode(node2);             
                             return;
                         }
                     }                    
                 }

               
            }
        }
      //  properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);//So the renderer is executed and the graph repainted
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
            innode=false;
         for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
         {
             if(clickingInNode(node,event))
             {
                start_x= node.x();
                start_y= node.y(); 
                innode=true;
                break;
             }
         }
              
               
                System.out.println("开始位置的鼠标x坐标:"+start_x);
                System.out.println("开始位置的鼠标x坐标:"+start_y);
               
                
                System.out.println("pressed");
                event.setConsumed(true);
                
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) 
    {
        /*
         System.out.println("dragged开始");
         for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
         {
             boolean temp=dragNode(node);
             System.out.println(temp);
             if (temp)
             {
               node.setPosition(event.x, event.y);
              
              System.out.println("拖拽进入");
               return; 
             }
         }
          event.setConsumed(true);
     
        */
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
         System.out.println("释放开始");
        for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
        {
            if(dragNode(node))
            {
                
                node.setPosition(event.x, -event.y);
                
                System.out.println("释放进入");
            }
        }
        
    }
    private boolean dragNode(Node node)
    {
        return   node.x()==start_x &&  node.y()==start_y;
    }
    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
        float xdiff = node.x() - event.x;
        float ydiff = -node.y() - event.y;//Note that y axis is inverse for node coordinates
        float radius = node.size();
          
        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }
}
