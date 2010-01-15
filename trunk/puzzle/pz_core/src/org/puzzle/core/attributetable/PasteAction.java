/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.core.attributetable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.memory.mapping.FeatureMapper;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.map.FeatureMapLayer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.openide.util.Exceptions;
import org.puzzle.core.attributetable.typemapping.MappingChooser;
import org.puzzle.core.resources.CoreResource;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PasteAction extends AbstractDataBufferAction{

    public PasteAction(){
        setText(CoreResource.getString("paste"));

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
//                final LayerFeaturePropertyPanel panel = getFeaturePanel();
//                if(panel == null) return;
//                final FeatureMapLayer currentLayer = panel.getTarget();
//                if(currentLayer == null) return;
//
//                final FeatureMapLayer bufferLayer = getLayer();
//                final Filter bufferFilter = getFilter();
//
//                if(bufferLayer == null || bufferFilter == null) return;
//
//                //we have everything needed for pasting datas
//
//                if(currentLayer.getCollection().isWritable()){
//                    final FeatureStore<SimpleFeatureType,SimpleFeature> store =
//                        (FeatureStore<SimpleFeatureType, SimpleFeature>) currentLayer.getFeatureSource();
//
//                    if(currentLayer == bufferLayer){
//                        //we are copying datas in the same datasource
//                        //no need to check feature types
//
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                final ProgressHandle handle = ProgressHandleFactory.createHandle(
//                                CoreResource.getString("copyingDatas"));
//                                handle.start(100);
//                                handle.setInitialDelay(1);
//                                handle.switchToIndeterminate();
//                                try {
//                                    FeatureCollection<SimpleFeatureType, SimpleFeature> features = bufferLayer.getFeatureSource().getFeatures(bufferFilter);
//                                    store.addFeatures(features);
//                                    //refresh the data table
//                                    panel.reset();
//                                } catch (IOException ex) {
//                                    Exceptions.printStackTrace(ex);
//                                }finally{
//                                    handle.finish();
//                                }
//                            }
//                        }.start();
//
//                    }else{
//                        //schema may not fit, we display a dialog to choose matching properties
//                        final MappingChooser chooser = new MappingChooser(
//                                bufferLayer.getCollection().getFeatureType(),
//                                currentLayer.getCollection().getFeatureType());
//                        chooser.showChooserDialog();
//                        final FeatureMapper mapper = chooser.getMapper();
//
//                        if(mapper != null){
//                            new Thread(){
//                                @Override
//                                public void run() {
//                                    final ProgressHandle handle = ProgressHandleFactory.createHandle(
//                                    CoreResource.getString("copyingDatas"));
//                                    handle.start(100);
//                                    handle.setInitialDelay(1);
//                                    handle.switchToIndeterminate();
//                                    try {
//                                        FeatureCollection<SimpleFeature> features = bufferLayer.getFeatureSource().getFeatures(bufferFilter);
//                                        store.addFeatures(new TransformReader(features, mapper));
//                                        //refresh the data table
//                                        panel.reset();
//                                    } catch (IOException ex) {
//                                        Exceptions.printStackTrace(ex);
//                                    }finally{
//                                        handle.finish();
//                                    }
//                                }
//                            }.start();
//
//                        }
//
//                    }
//                    //refresh the data table
//                    panel.reset();
//                }

            }
        });

    }


}
