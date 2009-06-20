/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2009 Puzzle-GIS
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

package org.puzzle.core.contexttree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.geotoolkit.gui.swing.maptree.AbstractTreePopupItem;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.gui.swing.resource.MessageBundle;
import org.geotoolkit.map.FeatureMapLayer;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This class is made to extend the menu item from geotoolkit, this
 * will implicitly make the class part of this module class-loader and
 * allow it to be loaded using the layer.xml.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class LayerFeatureItem extends AbstractTreePopupItem{

    private WeakReference<FeatureMapLayer> layerRef;

    /**
     * Creates a new instance of DefaultContextPropertyPop
     */
    public LayerFeatureItem() {
        super( MessageBundle.getString("contexttreetable_feature_table")  );
        init();
    }

    private void init(){
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(layerRef == null) return;

                FeatureMapLayer layer = layerRef.get();
                if(layer == null) return;

                LayerFeaturePropertyPanel pan = new LayerFeaturePropertyPanel();
                pan.setTarget(layer);

                TopComponent comp = new TopComponent(){
                    @Override
                    public void open() {
                        Mode m = WindowManager.getDefault().findMode("output");
                        if (m != null) {
                            m.dockInto(this);
                        }
                        super.open();
                    }

                };
                comp.setDisplayName(layer.getDescription().getTitle().toString());
                comp.setLayout(new BorderLayout());
                comp.add(BorderLayout.CENTER,pan);

                if(!comp.isOpened()){
                    comp.open();
                }
                comp.requestActive();
                comp.requestVisible();
            }
        }
        );
    }

    @Override
    public boolean isValid(TreePath[] selection) {
        return uniqueAndType(selection,FeatureMapLayer.class);
    }

    @Override
    public Component getComponent(TreePath[] selection) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selection[0].getLastPathComponent();
        layerRef = new WeakReference<FeatureMapLayer>((FeatureMapLayer) node.getUserObject());
        return this;
    }

}