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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.JContextTreePopup;
import org.geotools.gui.swing.contexttree.column.OpacityTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.StyleTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.VisibleTreeTableColumn;
import org.geotools.gui.swing.contexttree.popup.ContextActiveItem;
import org.geotools.gui.swing.contexttree.popup.CopyItem;
import org.geotools.gui.swing.contexttree.popup.CutItem;
import org.geotools.gui.swing.contexttree.popup.DeleteItem;
import org.geotools.gui.swing.contexttree.popup.DuplicateItem;
import org.geotools.gui.swing.contexttree.popup.LayerFeatureItem;
import org.geotools.gui.swing.contexttree.popup.LayerPropertyItem;
import org.geotools.gui.swing.contexttree.popup.LayerVisibilityItem;
import org.geotools.gui.swing.contexttree.popup.PasteItem;
import org.geotools.gui.swing.contexttree.popup.SeparatorItem;
import org.geotools.gui.swing.propertyedit.LayerCRSPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerFilterPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerGeneralPanel;
import org.geotools.gui.swing.propertyedit.LayerStylePropertyPanel;
import org.geotools.gui.swing.propertyedit.PropertyPane;
import org.geotools.gui.swing.propertyedit.filterproperty.JCQLPropertyPanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JSimpleStylePanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JXMLStylePanel;
import org.puzzle.puzzlecore.gtextend.widget.sldeditor.JAdvancedStylePanel;
import org.puzzle.puzzlecore.gui.contexttree.BindContextTree;

/**
 *
 * @author johann sorel
 */
public class ContextManager {

    private static ContextManager instance;
    private WeakReference<BindContextTree> treeRef = new WeakReference<BindContextTree>(null);

    private ContextManager() {
    }

    public JContextTree getContextTree() {

        BindContextTree tree = treeRef.get();
                
        if (tree == null) {
            tree = new BindContextTree(CORE.getInstance());

            tree.addColumn(new VisibleTreeTableColumn());
            tree.addColumn(new OpacityTreeTableColumn());
            tree.addColumn(new StyleTreeTableColumn());

            JContextTreePopup popupMenu = tree.getPopupMenu();
            popupMenu.addItem(new LayerVisibilityItem());
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new LayerFeatureItem());
            popupMenu.addItem(new ContextActiveItem(tree));
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new CutItem(tree));
            popupMenu.addItem(new CopyItem(tree));
            popupMenu.addItem(new PasteItem(tree));
            popupMenu.addItem(new DuplicateItem(tree));
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new DeleteItem(tree));
            popupMenu.addItem(new SeparatorItem());

            LayerPropertyItem property = new LayerPropertyItem();
            List<PropertyPane> lstproperty = new ArrayList<PropertyPane>();
            lstproperty.add(new LayerGeneralPanel());
            lstproperty.add(new LayerCRSPropertyPanel());

            LayerFilterPropertyPanel filters = new LayerFilterPropertyPanel();
            filters.addPropertyPanel(new JCQLPropertyPanel());
            lstproperty.add(filters);

            LayerStylePropertyPanel styles = new LayerStylePropertyPanel();
            styles.addPropertyPanel(new JSimpleStylePanel());
            styles.addPropertyPanel(new JAdvancedStylePanel());
            styles.addPropertyPanel(new JXMLStylePanel());
            lstproperty.add(styles);

            property.setPropertyPanels(lstproperty);
            popupMenu.addItem(property);


            tree.revalidate();
        }

        return tree;
    }

    static ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }
        return instance;
    }
}
