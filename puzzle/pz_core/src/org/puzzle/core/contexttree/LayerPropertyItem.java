/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.core.contexttree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.Collections;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.geotoolkit.gui.swing.contexttree.AbstractTreePopupItem;
import org.geotoolkit.gui.swing.propertyedit.JPropertyDialog;
import org.geotoolkit.gui.swing.propertyedit.PropertyPane;
import org.geotoolkit.map.MapLayer;
import org.puzzle.core.windows.mapdetail.JPropertyTree;

/**
 *
 * @author eclesia
 */
public class LayerPropertyItem extends AbstractTreePopupItem {

    private WeakReference<MapLayer> layerRef;
    private final TreeNode node;

    /**
     * Creates a new instance of DefaultContextPropertyPop
     */
    public LayerPropertyItem(TreeNode node) {
        super("Properties");
        this.node = node;
        init();
    }

    private void init() {
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (layerRef == null) {
                    return;
                }

                MapLayer layer = layerRef.get();
                if (layer == null) {
                    return;
                }

                PropertiesPane pane = new PropertiesPane();
                pane.setRoot(node);

                JPropertyDialog.showDialog(Collections.singletonList((PropertyPane)pane), layer);

            }
        });
    }

    @Override
    public boolean isValid(TreePath[] selection) {
        return uniqueAndType(selection, MapLayer.class);
    }

    @Override
    public Component getComponent(TreePath[] selection) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selection[0].getLastPathComponent();
        layerRef = new WeakReference<MapLayer>((MapLayer) node.getUserObject());
        return this;
    }
}
