/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
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
