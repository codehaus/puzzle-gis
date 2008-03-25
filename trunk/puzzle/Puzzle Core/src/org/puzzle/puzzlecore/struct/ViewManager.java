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
package org.puzzle.puzzlecore.struct;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.EventListenerList;
import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.column.SelectionTreeTableColumn;
import org.geotools.gui.swing.contexttree.popup.LayerZoomItem;
import org.geotools.gui.swing.map.map2d.JDefaultEditableMap2D;
import org.geotools.gui.swing.map.map2d.control.JMap2DInfoBar;
import org.geotools.gui.swing.map.map2d.minimap.JMiniMap;
import org.geotools.gui.swing.map.map2d.strategy.MergeBufferedImageStrategy;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.puzzle.puzzlecore.struct.MapGroup;
import org.puzzle.puzzlecore.struct.MapView;

/**
 *
 * @author Johann Sorel
 */
public final class ViewManager {

    
    private final EventListenerList listeners = new EventListenerList();
    
    private static ViewManager instance;
    private JDefaultEditableMap2D map2d = null;
    private JMap2DInfoBar infoBar = null;
    private JMiniMap miniMap = null;
    private final MapView[] EMPTY_VIEW_ARRAY = new MapView[0];
    private final List<MapView> views = new ArrayList<MapView>();
    private final Map<MapGroup, List<MapView>> viewGroups = new HashMap<MapGroup, List<MapView>>();
    private MapView activeView = null;

    private ViewManager() {

        JContextTree tree = ContextManager.getInstance().getContextTree();

        SelectionTreeTableColumn selectionColumn = new SelectionTreeTableColumn(getMap2D());
        LayerZoomItem zoomLayerItem = new LayerZoomItem(getMap2D());

        selectionColumn.setMap(getMap2D());
        zoomLayerItem.setMap(getMap2D());

        tree.addColumn(selectionColumn);
        tree.getPopupMenu().addItem(0, zoomLayerItem);

    }
    
    
    static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }
    
    /**
     * 
     * @return
     * @deprecated
     */
    @Deprecated
    public JDefaultEditableMap2D getMap2D() {

        if (map2d == null) {
            map2d = new JDefaultEditableMap2D();
            map2d.setRenderingStrategy(new MergeBufferedImageStrategy());
            map2d.setBackground(Color.WHITE);
            map2d.setOpaque(true);
        }

        MapContext context = CORE.getInstance().getActiveContext();

        if (context == null) {
            context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            context.setTitle("Context");
        }

        CORE.getInstance().addContext(context);
        CORE.getInstance().setActiveContext(context);
        map2d.getRenderingStrategy().setContext(context);

        return map2d;
    }

    /**
     * 
     * @return
     * @deprecated
     */
    @Deprecated
    public JMap2DInfoBar getMapInfoBar() {

        if (infoBar == null) {
            infoBar = new JMap2DInfoBar();
            infoBar.setFloatable(false);
            infoBar.setMap(getMap2D());
        }


        return infoBar;
    }

    /**
     * 
     * @return
     * @deprecated
     */
    @Deprecated
    public JMiniMap getMiniMap() {
        miniMap = new JMiniMap();
        miniMap.setRelatedMap2D(getMap2D());

        return miniMap;
    }


////////////////////////////////////////////////////////////////////////////////
// MAPVIEW MANAGEMENT //////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    /**
     * add a new view
     * @param view the mapview to add
     */
    public void addView(MapView view) {

        if (!views.contains(view)) {
            views.add(view);
            fireViewAdded(view);
        }
    }

    /**
     * remove view
     * @param view target mapview to remove
     */
    public void removeView(MapView view) {

        if (views.contains(view)) {
            views.remove(view);
            fireViewRemoved(view);
        }

    }
    
    
    /**
     * get the active view
     * @return return the active MapView, if none return null
     */
    public MapView getActiveView() {
        return activeView;
    }

    /**
     * active the view if mapview is in the mapview list
     * @param view view to activate
     */
    public void setActiveContext(MapView view) {

        if (view == null) {
            throw new NullPointerException();
        }

        if (views.contains(view)) {

            if (view != activeView) {
                MapView oldView =  activeView;
                activeView = view;
                fireViewActivated(oldView,view);
            }
        } else {
            throw new IllegalArgumentException();
        }


    }

    /**
     * count MapViews
     * @return number of mapviews in the tree
     */
    public int getViewCount() {
        return views.size();
    }

    /**
     * return view at index i
     * @param i position of the mapview
     * @return the mapview a position i
     */
    public MapView getView(int i) {
        return views.get(i);
    }

    /**
     * get the index of a mapview
     * @param view the mapview to find
     * @return index of context
     */
    public int getViewIndex(MapView view) {
        return views.indexOf(view);
    }

    /**
     * MapView Array
     * @return empty Array if no mapviews
     */
    public MapView[] getViews() {
        return views.toArray(EMPTY_VIEW_ARRAY);
    }


    
////////////////////////////////////////////////////////////////////////////////
// FIREEVENT AND LISTENERS /////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    
    private void fireViewAdded(MapView view) {
        ViewEvent kevent = new ViewEvent(this, view, view);

        ViewListener[] list = getViewListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].viewAdded(kevent);
        }
    }

    private void fireViewRemoved(MapView view) {
        ViewEvent event = new ViewEvent(this, view,view);

        ViewListener[] list = getViewListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].viewRemoved(event);
        }
    }

    private void fireViewActivated(MapView oldview, MapView newview) {
        ViewEvent event = new ViewEvent(this, oldview, newview);

        ViewListener[] list = getViewListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].viewActivated(event);
        }
    }

    public void addViewListener(ViewListener ker) {
        listeners.add(ViewListener.class, ker);
    }

    public void removeViewListener(ViewListener ker) {
        listeners.remove(ViewListener.class, ker);
    }

    public ViewListener[] getViewListeners() {
        return listeners.getListeners(ViewListener.class);
    }
    
    
}
