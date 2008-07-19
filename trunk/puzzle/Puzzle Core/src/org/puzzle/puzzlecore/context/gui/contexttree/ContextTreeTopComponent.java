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
package org.puzzle.puzzlecore.context.gui.contexttree;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.puzzle.puzzlecore.gtextend.widget.style.JAdvancedStylePanel;
import org.puzzle.puzzlecore.swing.layerprop.style.JXMLStylePanel;

/**
 * Top component which displays something.
 * @author : johann sorel
 */
final class ContextTreeTopComponent extends TopComponent {

    private JContextTree tree = null;
    private static ContextTreeTopComponent instance;
    static final String ICON_PATH = "org/puzzle/puzzlecore/project/map.png";
    private static final String PREFERRED_ID = "ContextTreeTopComponent";

    private ContextTreeTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ContextTreeTopComponent.class, "CTL_ContextTreeTopComponent"));
        setToolTipText(NbBundle.getMessage(ContextTreeTopComponent.class, "HINT_ContextTreeTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));

    }

    public JContextTree getContextTree() {

        BindContextTree tree = new BindContextTree();

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

        return tree;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ContextTreeTopComponent getDefault() {
        if (instance == null) {
            instance = new ContextTreeTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ContextTreeTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ContextTreeTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ContextTreeTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ContextTreeTopComponent) {
            return (ContextTreeTopComponent) win;
        }
        Logger.getLogger(ContextTreeTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        removeAll();
        if (tree == null) {
            tree = getContextTree();
        }

        add(BorderLayout.CENTER, tree);
    }

    @Override
    public void componentClosed() {
        removeAll();
        tree = null;
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ContextTreeTopComponent.getDefault();
        }
    }
}
