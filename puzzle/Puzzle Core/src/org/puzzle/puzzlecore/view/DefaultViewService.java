/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.puzzlecore.view;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.puzzle.puzzlecore.view.MapView;


/**
 *
 * @author Johann Sorel
 */
public class DefaultViewService implements ViewService{

    private final InstanceContent CONTENT = new InstanceContent();
    private final Lookup LOOKUP = new AbstractLookup(CONTENT);
    
//    private final EventListenerList listeners = new EventListenerList();
//    private final MapView[] EMPTY_VIEW_ARRAY = new MapView[0];
//    private final MapGroup[] EMPTY_GROUP_ARRAY = new MapGroup[0];
//    private JDefaultEditableMap2D map2d = null;
//    private JMap2DInfoBar infoBar = null;
//    private JMiniMap miniMap = null;
//    private final List<MapView> views = new ArrayList<MapView>();
//    private final List<MapGroup> groups = new ArrayList<MapGroup>();
//    private MapView activeView = null;

    public DefaultViewService() {

//        JContextTree tree = ContextManager.getInstance().getContextTree();
//
//        SelectionTreeTableColumn selectionColumn = new SelectionTreeTableColumn(null);
//        LayerZoomItem zoomLayerItem = new LayerZoomItem(null);
//
//        selectionColumn.setMap(null);
//        zoomLayerItem.setMap(null);
//
//        tree.addColumn(selectionColumn);
//        tree.getPopupMenu().addItem(0, zoomLayerItem);

    }


////////////////////////////////////////////////////////////////////////////////
// MAPVIEW MANAGEMENT //////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    
    public Lookup getLookup() {
        return LOOKUP;
    }
    
    
    /**
     * add a new view
     * @param view the mapview to add
     */
   public void addView(MapView view) {
        
        CONTENT.add(view);
        
//        if (!views.contains(view)) {
//            views.add(view);
//            
//            CONTENT.add(view);
//            
//            view.getMap().addMap2DListener(new Map2DListener() {
//
//                public void mapStrategyChanged(Map2DEvent arg0) {
//                }
//
//                public void mapActionStateChanged(Map2DEvent arg0) {
//                }
//            });
//
//            new ViewFallow(view);
//
//            fireViewAdded(view);
//        }
    }

//    /**
//     * remove view
//     * @param view target mapview to remove
//     */
    public void removeView(MapView view) {
        CONTENT.remove(view);
//        if (views.contains(view)) {
//            views.remove(view);
//            view.getMap().dispose();
//            fireViewRemoved(view);
//        }

    }
//
//    /**
//     * get the active view
//     * @return return the active MapView, if none return null
//     */
//    public MapView getActiveView() {
//        return activeView;
//    }
//
//    /**
//     * active the view if mapview is in the mapview list
//     * @param view view to activate
//     */
//    public void setActiveContext(MapView view) {
//
////        if (view == null) {
////            throw new NullPointerException();
////        }
////
////        if (views.contains(view)) {
////
////            if (view != activeView) {
////                MapView oldView = activeView;
////                activeView = view;
////                fireViewActivated(oldView, view);
////            }
////        } else {
////            throw new IllegalArgumentException();
////        }
//
//
//    }
//
//    /**
//     * count MapViews
//     * @return number of mapviews in the tree
//     */
//    public int getViewCount() {
//        return views.size();
//    }
//
//    /**
//     * return view at index i
//     * @param i position of the mapview
//     * @return the mapview a position i
//     */
//    public MapView getView(int i) {
//        return views.get(i);
//    }
//
//    /**
//     * get the index of a mapview
//     * @param view the mapview to find
//     * @return index of context
//     */
//    public int getViewIndex(MapView view) {
//        return views.indexOf(view);
//    }
//
//    /**
//     * MapView Array
//     * @return empty Array if no mapviews
//     */
//    public MapView[] getViews() {
//        return views.toArray(EMPTY_VIEW_ARRAY);
//    }
//
//    /**
//     * add a new group
//     * @param group the mapgroup to add
//     */
//    public void addGroup(MapGroup group) {
//
//        if (!groups.contains(group)) {
//            groups.add(group);
//            fireGroupAdded(group);
//        }
//    }
//
//    /**
//     * remove Group
//     * @param group target mapGroup to remove
//     */
//    public void removeGroup(MapGroup group) {
//
//        if (groups.contains(group)) {
//            groups.remove(group);
//            fireGroupRemoved(group);
//        }
//
//    }
//
//    /**
//     * set the mapgroup of a view
//     * @param view
//     * @param group
//     */
//    public void setViewGroup(MapView view, MapGroup group) {
//
//        if (views.contains(view) && groups.contains(group)) {
//
//            MapGroup oldgroup = view.getGroup();
//
//            if (view.getGroup() != group) {
//                view.setGroup(group);
//                fireGroupChanged(group);
//            }
//
//            if (oldgroup != null && oldgroup != group) {
//                fireGroupChanged(oldgroup);
//            }
//
//        } else {
//            throw new IllegalArgumentException("group or view are not registered in the viewmanager");
//        }
//
//    }
//
//    public MapView[] getViewInGroup(MapGroup group) {
//        List<MapView> vs = new ArrayList<MapView>();
//
//        for (MapView view : views) {
//            if (view.getGroup() == group) {
//                vs.add(view);
//            }
//        }
//
//        return vs.toArray(EMPTY_VIEW_ARRAY);
//    }
//
//    /**
//     * count MapGroups
//     * @return number of mapGroups in the tree
//     */
//    public int getGroupCount() {
//        return groups.size();
//    }
//
//    /**
//     * return Group at index i
//     * @param i position of the mapGroup
//     * @return the mapGroup a position i
//     */
//    public MapGroup getGroup(int i) {
//        return groups.get(i);
//    }
//
//    /**
//     * get the index of a mapGroup
//     * @param group the mapGroup to find
//     * @return index of context
//     */
//    public int getGroupIndex(MapGroup group) {
//        return groups.indexOf(group);
//    }
//
//    /**
//     * MapGroup Array
//     * @return empty Array if no mapGroups
//     */
//    public MapGroup[] getGroups() {
//        return groups.toArray(EMPTY_GROUP_ARRAY);
//    }
//
//////////////////////////////////////////////////////////////////////////////////
//// FIREEVENT AND LISTENERS /////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//    private void fireViewAdded(MapView view) {
//        ViewEvent kevent = new ViewEvent(this, view, view);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].viewAdded(kevent);
//        }
//    }
//
//    private void fireViewRemoved(MapView view) {
//        ViewEvent event = new ViewEvent(this, view, view);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].viewRemoved(event);
//        }
//    }
//
//    private void fireViewActivated(MapView oldview, MapView newview) {
//        ViewEvent event = new ViewEvent(this, oldview, newview);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].viewActivated(event);
//        }
//    }
//
//    private void fireGroupAdded(MapGroup group) {
//        GroupEvent kevent = new GroupEvent(this, group);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].groupAdded(kevent);
//        }
//    }
//
//    private void fireGroupRemoved(MapGroup group) {
//        GroupEvent kevent = new GroupEvent(this, group);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].groupRemoved(kevent);
//        }
//    }
//
//    private void fireGroupChanged(MapGroup group) {
//        GroupEvent kevent = new GroupEvent(this, group);
//
//        ViewListener[] list = getViewListeners();
//        for (int i = 0; i < list.length; i++) {
//            list[i].groupChanged(kevent);
//        }
//    }
//
//    public void addViewListener(ViewListener ker) {
//        listeners.add(ViewListener.class, ker);
//    }
//
//    public void removeViewListener(ViewListener ker) {
//        listeners.remove(ViewListener.class, ker);
//    }
//
//    public ViewListener[] getViewListeners() {
//        return listeners.getListeners(ViewListener.class);
//    }
//
//}
//class ViewFallow implements Map2DListener, StrategyListener {
//
//    private final MapView view;
//    private boolean isWorking = false;
//
//    ViewFallow(MapView view) {
//        this.view = view;
//
//        view.getMap().addMap2DListener(this);
//        view.getMap().getRenderingStrategy().addStrategyListener(this);
//    }
//
//    public void mapStrategyChanged(Map2DEvent event) {
//        event.getPreviousStrategy().removeStrategyListener(this);
//        event.getStrategy().addStrategyListener(this);
//    }
//
//    public void mapActionStateChanged(Map2DEvent event) {
//    }
//
//    public void setRendering(boolean event) {
//    }
//
//    public void mapAreaChanged(RenderingStrategyEvent event) {
//
////        if (!isWorking) {
////            isWorking = true;
////            MapGroup group = view.getGroup();
////
////            if (group != null) {
////                MapView[] views = CORE.getViewManager().getViewInGroup(group);
////                for (MapView view : views) {
////                    if (view != this.view && view.isTranslationLink()) {
////                        view.getMap().getRenderingStrategy().setMapArea(event.getMapArea());
////                    }
////                }
////            }
////            isWorking = false;
////        }
//    }
//
//    public void mapContextChanged(RenderingStrategyEvent arg0) {
//    }
}
