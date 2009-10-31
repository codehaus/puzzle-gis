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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.geotoolkit.gui.swing.contexttree.AbstractTreePopupItem;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.gui.swing.resource.MessageBundle;
import org.geotoolkit.map.FeatureMapLayer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
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

                final FeatureMapLayer layer = layerRef.get();
                if(layer == null) return;

                new Thread(){

                    @Override
                    public void run() {

                        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                                org.puzzle.core.resources.MessageBundle.getString("openSource") +" : " + layer.getName());
                        handle.start(100);
                        handle.setInitialDelay(1);
                        handle.switchToIndeterminate();

                        try{
                            LayerFeaturePropertyPanel pan = new LayerFeaturePropertyPanel();
                            pan.setTarget(layer);

                            //configure all actions
                            Lookup lk = Lookups.forPath("/Puzzle/AttributeTable/Actions");
                            for(JComponent item : lk.lookupAll(JComponent.class)){
                                pan.actions().add(item);
                            }


                            final TopComponent comp = new TopComponent(){
                                @Override
                                public void open() {
                                    Mode m = WindowManager.getDefault().findMode("output");
                                    if (m != null) {
                                        m.dockInto(this);
                                    }
                                    super.open();
                                }

                            };

                            comp.setLayout(new BorderLayout());
                            comp.add(BorderLayout.CENTER,pan);

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    comp.setDisplayName(layer.getDescription().getTitle().toString());
                                    if(!comp.isOpened()){
                                        comp.open();
                                    }
                                    comp.requestActive();
                                    comp.requestVisible();
                                }
                            });
                            
                        }finally{
                            handle.finish();
                        }

                    }

                }.start();

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