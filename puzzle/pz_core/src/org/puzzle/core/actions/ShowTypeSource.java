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
package org.puzzle.core.actions;

import java.awt.BorderLayout;
import javax.swing.JComponent;

import javax.swing.SwingUtilities;
import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.FeatureSource;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.map.FeatureMapLayer;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.util.RandomStyleFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.CookieAction;
import org.openide.util.lookup.Lookups;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.puzzle.core.resources.MessageBundle;

/**
 * This class is made to extend the menu item from geotoolkit, this
 * will implicitly make the class part of this module class-loader and
 * allow it to be loaded using the layer.xml.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShowTypeSource extends CookieAction{

    /**
     * Method called when this action is clicked.<br>
     * This method displays the first activated context node in a map.
     * @param   activatedNodes  The currently activated nodes.
     */
    @Override
    protected void performAction(final Node[] activatedNodes) {
        if(activatedNodes.length == 0 ) return ;

        final String typeName = activatedNodes[0].getName();

        final DataStore store = activatedNodes[0].getParentNode().getLookup().lookup(DataStore.class);

        if(store == null) return;

        new Thread(){
            @Override
            public void run(){

                final ProgressHandle handle = ProgressHandleFactory.createHandle(
                        MessageBundle.getString("openSource") +" : " + typeName);
                handle.start(100);
                handle.setInitialDelay(1);
                handle.switchToIndeterminate();

                try{
                    final FeatureSource<SimpleFeatureType,SimpleFeature> fs = store.getFeatureSource(typeName);

                    final FeatureMapLayer layer = MapBuilder.createFeatureLayer(fs, RandomStyleFactory.createPolygonStyle());
                    if(layer == null) return;

                    final LayerFeaturePropertyPanel pan = new LayerFeaturePropertyPanel();
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

                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            comp.setDisplayName(typeName);
                            comp.setLayout(new BorderLayout());
                            comp.add(BorderLayout.CENTER,pan);

                            if(!comp.isOpened()){
                                comp.open();
                            }
                            comp.requestActive();
                            comp.requestVisible();
                        }
                    });

                }catch(Exception ex){
                    Exceptions.printStackTrace(ex.fillInStackTrace());
                }finally{
                    handle.finish();
                }

            }
        }.start();

    }


    /**
     * Specify the enable mode of the action.<br>
     * This one is enable when only one node is selected.
     * @return  An {@code int} containing the mode.
     */
    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    /**
     * Get the name of the action.
     * @return  A {@code String} containing the Action's name.
     */
    @Override
    public String getName() {
        return MessageBundle.getString("attributTable");
    }

    /**
     * Defines the cookies needed for the action to be enable. The activated
     * node must contain a {@code GISContextDataObject} to be enabled.
     * @return  A set of {@code Class}es.
     */
    @Override
    protected Class[] cookieClasses() {
        return new Class[]{Object.class};
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected String iconResource() {
        return "org/puzzle/core/actions/showMap.png";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected boolean asynchronous() {
        return false;
    }

}