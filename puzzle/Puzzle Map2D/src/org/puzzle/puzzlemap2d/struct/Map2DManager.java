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
package org.puzzle.puzzlemap2d.struct;

import java.awt.Color;
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
import org.puzzle.puzzlecontexttree.struct.ContextTreeManager;
import org.puzzle.puzzlecore.struct.Application;

/**
 *
 * @author Johann Sorel
 */
public final class Map2DManager {

    private static Map2DManager instance;
    private JDefaultEditableMap2D map2d = null;
    private JMap2DInfoBar infoBar = null;
    private JMiniMap miniMap = null;

    private Map2DManager() {

        JContextTree tree = ContextTreeManager.getInstance().getContextTree();

        SelectionTreeTableColumn selectionColumn = new SelectionTreeTableColumn(getMap2D());
        LayerZoomItem zoomLayerItem = new LayerZoomItem(getMap2D());

        selectionColumn.setMap(getMap2D());
        zoomLayerItem.setMap(getMap2D());

        tree.addColumn(selectionColumn);
        tree.getPopupMenu().addItem(0, zoomLayerItem);

    }

    public JDefaultEditableMap2D getMap2D() {

        if (map2d == null) {
            map2d = new JDefaultEditableMap2D();
            map2d.setRenderingStrategy(new MergeBufferedImageStrategy());
            map2d.setBackground(Color.WHITE);
            map2d.setOpaque(true);
        }

        MapContext context = Application.getInstance().getActiveContext();
        
        if(context == null){
            context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            context.setTitle("Context");
        }
        
        Application.getInstance().addContext(context);
        Application.getInstance().setActiveContext(context);
        map2d.getRenderingStrategy().setContext(context);

        return map2d;
    }

    public JMap2DInfoBar getMapInfoBar() {

        if (infoBar == null) {
            infoBar = new JMap2DInfoBar();
            infoBar.setFloatable(false);            
            infoBar.setMap(getMap2D());
        }
        

        return infoBar;
    }

    public JMiniMap getMiniMap(){
        miniMap = new JMiniMap();
        miniMap.setRelatedMap2D(getMap2D());
        
        return miniMap;
    }
    
    
    public static Map2DManager getInstance() {
        if (instance == null) {
            instance = new Map2DManager();
        }
        return instance;
    }
}
