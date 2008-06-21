/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
 *  
 *  GPLv3 + Classpath exception
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.puzzle.puzzlecore.gtextend.widget.viewtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.gui.swing.map.map2d.stream.StreamingMap2D;
import org.geotools.map.MapContext;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.puzzle.puzzlecore.view.MapGroup;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author johann sorel
 */
public class ViewTreeModel extends DefaultTreeTableModel {

    private final Map<MapView, DefaultMutableTreeTableNode> mapViews = new HashMap<MapView, DefaultMutableTreeTableNode>();
    private final Map<MapGroup, DefaultMutableTreeTableNode> mapGroups = new HashMap<MapGroup, DefaultMutableTreeTableNode>();

    private final DefaultMutableTreeTableNode noGroupNode = new DefaultMutableTreeTableNode();
    
    
    /**
     * create a StyleTreeModel
     * @param style , can't be null
     */
    public ViewTreeModel() {
        super(new DefaultMutableTreeTableNode());

        getRoot().add(noGroupNode);
        
        
        
//        parse();
//
//        
//        CORE.getViewManager().addViewListener(new ViewListener() {
//
//            public void viewAdded(ViewEvent event) {
//                DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(event.getView());
//                mapViews.put(event.getView(), node);
//                insertNodeInto(node, (DefaultMutableTreeTableNode) getRoot(), 0);
//            }
//
//            public void viewRemoved(ViewEvent event) {
//                removeNodeFromParent(mapViews.get(event.getView()));
//                mapViews.remove(event.getView());
//            }
//
//            public void groupAdded(GroupEvent event) {
//                DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(event.getGroup());
//                mapGroups.put(event.getGroup(), node);
//                insertNodeInto(node, (DefaultMutableTreeTableNode) getRoot(), getRoot().getChildCount());
//            }
//
//            public void viewActivated(ViewEvent event) {
//            }
//
//            public void groupRemoved(GroupEvent event) {
//                removeNodeFromParent(mapGroups.get(event.getGroup()));
//                mapGroups.remove(event.getGroup());
//            }
//
//            public void groupChanged(GroupEvent event) {
//                parse();
////                MapView[] views = CORE.getViewManager().getViewInGroup(event.getGroup());
////                
////                DefaultMutableTreeTableNode parent = new DefaultMutableTreeTableNode(event.getGroup());
////                
////                //we clean the group node
////                for(int i=0,n=parent.getChildCount(); i<n; i++){
////                    DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) parent.getChildAt(i);
////                    mapViews.remove(node.getUserObject());
////                    removeNodeFromParent(node);
////                }
////                
////                //we refill the group node
////                for(MapView view : views){
////                    DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(view);
////                    mapViews.put(view,node);
////                    insertNodeInto(node, parent, 0);
////                }
//            }
//        });
    }

    public void addView(MapView view){
        if(!mapViews.containsKey(view)){
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(view);
            mapViews.put(view, node);
            if(view.getGroup() == null){
                insertNodeInto(node, noGroupNode, noGroupNode.getChildCount());
            }else{
                DefaultMutableTreeTableNode parent = mapViews.get(view.getGroup());
                insertNodeInto(node, parent, parent.getChildCount());
            }
        }
    }
    
    public void addView(Collection<MapView> views){
        for(MapView view : views){
            addView(view);
        }
    }
    
    public void removeView(MapView view){
        if(mapViews.containsKey(view)){
            DefaultMutableTreeTableNode node = mapViews.get(view);
//            ((MapView)node.getUserObject()).dispose();
            removeNodeFromParent(node);
        }
    }
    
    public void addGroup(MapGroup group){
        if(!mapGroups.containsKey(group)){
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(group);
            mapGroups.put(group, node);
            insertNodeInto(node, getRoot(), getRoot().getChildCount());
         }
    }
    
    public void addGroup(Collection<MapGroup> groups){
        for(MapGroup group : groups){
            addGroup(group);
        }
    }
    
    public void removeGroup(MapGroup group){
        if(mapGroups.containsKey(group)){            
            for(MapView view : mapViews.keySet()){
                if(view.getGroup() == group){
                    view.setGroup(null);
                }
            }            
            removeNodeFromParent(mapGroups.get(group));
        }
    }
    
    public Collection<MapView> getViews(){
        return new ArrayList<MapView>(mapViews.keySet());
    }
    
    public Collection<MapGroup> getGroups(){
        return new ArrayList<MapGroup>(mapGroups.keySet());
    }
        
    private void parse() {
        mapGroups.clear();
        mapViews.clear();

        TreeTableNode root = getRoot();
//
//        while(root.getChildCount() >0){
//            removeNodeFromParent( (DefaultMutableTreeTableNode)root.getChildAt(0));
//        }
////        for (int i = root.getChildCount()-1 ; i >= 0; i--) {
////            DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) root.getChildAt(i);
////            removeNodeFromParent(node);
////        }
//
//        MapView[] views = CORE.getViewManager().getViews();
//        MapGroup[] groups = CORE.getViewManager().getGroups();
//
//        for (MapGroup group : groups) {
//            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(group);
//            mapGroups.put(group, node);
//            insertNodeInto(node, (DefaultMutableTreeTableNode) getRoot(), 0);
//        }
//
//        for (MapView view : views) {
//            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(view);
//            mapViews.put(view, node);
//
//            if (view.getGroup() != null) {
//                DefaultMutableTreeTableNode parent = mapGroups.get(view.getGroup());
//                insertNodeInto(node, parent, 0);
//            } else {
//                insertNodeInto(node, (DefaultMutableTreeTableNode) getRoot(), 0);
//            }
//
//
//        }
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(Object target, int index) {
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) target;
        Object obj = node.getUserObject();

        if (obj instanceof MapView) {
            switch (index) {
                case 0:
                    return ((MapView) obj).getName();
                case 1:
                    return ((MapView) obj).isTranslationLink();
                case 2:
                    return ((MapView) obj).isScaleLink();
                case 3:
                    return ((MapView) obj).isRotationLink();
                case 4:
                    return ((StreamingMap2D)((MapView) obj).getMap()).getRenderingStrategy().getContext();
                default:
                    return null;
            }
        } else if (obj instanceof MapGroup) {
            switch (index) {
                case 0:
                    return ((MapGroup) obj).getTitle();
                default:
                    return null;
            }
        } else {
            return "no group";
        }

    }

    @Override
    public void setValueAt(Object value, Object target, int index) {
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) target;
        Object obj = node.getUserObject();


        if (obj instanceof MapView) {
            switch (index) {
                case 0:
                    ((MapView) obj).setName((String) value);
                    break;
                case 1:
                    ((MapView) obj).setTranslationLink((Boolean) value);
                    break;
                case 2:
                    ((MapView) obj).setScaleLink((Boolean) value);
                    break;
                case 3:
                    ((MapView) obj).setRotationLink((Boolean) value);
                    break;
                case 4:
                    if(value != null)                    
                    ((StreamingMap2D)((MapView) obj).getMap()).getRenderingStrategy().setContext( (MapContext)value);
                    break;
            }
        } else if (obj instanceof MapGroup) {
            switch (index) {
                case 0:
                    ((MapGroup) obj).setTitle((String) value);
                    break;
            }
        }
    }

    @Override
    public boolean isCellEditable(Object target, int index) {
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) target;
        Object obj = node.getUserObject();

        if (obj instanceof MapView) {
            return true;
        } else if (obj instanceof MapGroup) {
            switch (index) {
                case 0:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getColumnName(
            int arg0) {
        return "";
    }

    @Override
    public DefaultMutableTreeTableNode getRoot() {
        return (DefaultMutableTreeTableNode) super.getRoot();
    }

    
//---------------------using nodes------------------------------------------
    /**
     * move an existing node
     * @param movedNode :node to move
     * @param targetNode 
     * @return DefaultMutableTreeNode or null if node could not be moved
     */
    public void moveNode(DefaultMutableTreeTableNode movedNode, DefaultMutableTreeTableNode targetNode) {



        Object movedObj = movedNode.getUserObject();
//        DefaultMutableTreeTableNode parentMovedNode = (DefaultMutableTreeTableNode) movedNode.getParent();
//        Object parentMovedObj = parentMovedNode.getUserObject();

        Object targetObj = targetNode.getUserObject();

        if (targetObj instanceof MapGroup) {
            MapGroup group = (MapGroup) targetObj;
            if (movedObj instanceof MapView) {
                MapView view = (MapView) movedObj;
                if (view.getGroup() != group) {
//                    CORE.getViewManager().setViewGroup(view, group);
                }
            }
        } else if (targetObj instanceof MapView) {
            MapGroup group = ((MapView) targetObj).getGroup();
            if (movedObj instanceof MapView && group != null) {
                MapView view = (MapView) movedObj;
                if (view.getGroup() != group) {
//                    CORE.getViewManager().setViewGroup(view, group);
                }
            }
        }

//        DefaultMutableTreeTableNode copy = null;

//        
//
//        if (targetObj instanceof FeatureTypeStyle && movedObj instanceof FeatureTypeStyle) {
//            copy = moveAt(movedNode,(FeatureTypeStyle) movedObj, indexof(style, (FeatureTypeStyle) targetObj));
//            
//        } else if (targetObj instanceof FeatureTypeStyle && movedObj instanceof Rule) {
//                        
//            if (parentMovedNode == targetNode) {                
//            } else if(parentMovedNode.getChildCount() == 1){
//                Rule rule = RANDOM_FACTORY.duplicate((Rule) movedObj);
//                copy = insert(targetNode, rule);
//            } else{
//                remove(parentMovedNode, (Rule) movedObj);
//                copy = insert(targetNode, (Rule) movedObj);
//            }
//
//
//        } else if (targetObj instanceof Rule && movedObj instanceof Rule) {
//            
//            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode)targetNode.getParent();
//            Rule targetRule = (Rule) targetObj;
//            int targetIndex = indexof((FeatureTypeStyle)targetParentNode.getUserObject(),targetRule);
//            
//            if (parentMovedNode == targetParentNode) {          
//                copy = moveAt(movedNode, (Rule)movedObj, targetIndex);
//            } else if(parentMovedNode.getChildCount() == 1){
//                Rule rule = RANDOM_FACTORY.duplicate((Rule) movedObj);                                
//                FeatureTypeStyle parentfts = (FeatureTypeStyle) targetParentNode.getUserObject();                
//                copy = insertAt(targetParentNode, rule, targetIndex );
//            } else{
//                remove(parentMovedNode, (Rule) movedObj);
//                copy = insertAt(targetParentNode, (Rule) movedObj,targetIndex);
//            }
//                        
//        } else if (targetObj instanceof Rule && movedObj instanceof Symbolizer) {
//            
//            if (parentMovedNode == targetNode) {                
//            } else if(parentMovedNode.getChildCount() == 1){
//                Symbolizer symbol = RANDOM_FACTORY.duplicate((Symbolizer) movedObj);
//                copy = insert(targetNode, symbol);
//            } else{
//                remove(parentMovedNode, (Symbolizer) movedObj);
//                copy = insert(targetNode, (Symbolizer) movedObj);
//            }
//            
//        } else if (targetObj instanceof Symbolizer && movedObj instanceof Symbolizer) {
//            
//            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode)targetNode.getParent();
//            Symbolizer targetSymbol = (Symbolizer) targetObj;
//            int targetIndex = indexof((Rule)targetParentNode.getUserObject(),targetSymbol);
//            
//            if (parentMovedNode == targetParentNode) {          
//                copy = moveAt(movedNode, (Symbolizer)movedObj, targetIndex);
//            } else if(parentMovedNode.getChildCount() == 1){
//                Symbolizer symbol = RANDOM_FACTORY.duplicate((Symbolizer) movedObj);                                
//                Rule parentRule = (Rule) targetParentNode.getUserObject();                
//                copy = insertAt(targetParentNode, symbol, targetIndex );
//            } else{
//                remove(parentMovedNode, (Symbolizer) movedObj);
//                copy = insertAt(targetParentNode, (Symbolizer) movedObj,targetIndex);
//            }
//            
//        }
//
//        return copy;
    }
}
