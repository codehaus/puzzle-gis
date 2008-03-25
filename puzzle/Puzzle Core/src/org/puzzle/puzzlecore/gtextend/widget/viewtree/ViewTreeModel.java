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

import java.util.HashMap;
import java.util.Map;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.puzzle.puzzlecore.struct.CORE;
import org.puzzle.puzzlecore.struct.ViewEvent;
import org.puzzle.puzzlecore.struct.ViewListener;
import org.puzzle.puzzlecore.struct.MapGroup;
import org.puzzle.puzzlecore.struct.MapView;

/**
 *
 * @author johann sorel
 */
public class ViewTreeModel extends DefaultTreeTableModel {

    private Map<MapView,DefaultMutableTreeTableNode> map = new HashMap<MapView,DefaultMutableTreeTableNode>();
    
    
    /**
     * create a StyleTreeModel
     * @param style , can't be null
     */
    public ViewTreeModel() {
        super(new DefaultMutableTreeTableNode());

        MapView[] views = CORE.getViewManager().getViews();
        
        for(MapView view : views){
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(view);
            map.put(view, node);
            insertNodeInto(node,(DefaultMutableTreeTableNode)getRoot(), 0);
        }

        CORE.getViewManager().addViewListener(new ViewListener() {

            public void viewAdded(ViewEvent event) {
                DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(event.getView());
                map.put(event.getView(), node);
                insertNodeInto(node,(DefaultMutableTreeTableNode)getRoot(), 0);
            }

            public void viewRemoved(ViewEvent event) {
                removeNodeFromParent(map.get(event.getView()));
                map.remove(event.getView());
            }

            public void viewActivated(ViewEvent event) {
            }
        });
    }

    @Override
    public int getColumnCount() {
        return 4;
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
            return null;
        }

    }

    @Override
    public String getColumnName(
            int arg0) {
        return "";
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
        DefaultMutableTreeTableNode parentMovedNode = (DefaultMutableTreeTableNode) movedNode.getParent();
        Object parentMovedObj = parentMovedNode.getUserObject();

        Object targetObj = targetNode.getUserObject();


        DefaultMutableTreeTableNode copy = null;

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
