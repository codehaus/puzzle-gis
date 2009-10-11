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
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotoolkit.data.FeatureReader;
import org.geotoolkit.data.FeatureStore;
import org.geotoolkit.feature.collection.CollectionListener;
import org.geotoolkit.feature.collection.FeatureCollection;
import org.geotoolkit.feature.collection.FeatureIterator;
import org.geotoolkit.geometry.jts.JTSEnvelope2D;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.map.FeatureMapLayer;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;
import org.openide.util.Exceptions;
import org.puzzle.core.attributetable.typemapping.FeatureMapper;
import org.puzzle.core.attributetable.typemapping.MappingChooser;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PasteAction extends AbstractDataBufferAction{

    public PasteAction(){
        setText(Utilities.getString("paste"));

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final LayerFeaturePropertyPanel panel = getFeaturePanel();
                if(panel == null) return;
                final FeatureMapLayer currentLayer = panel.getTarget();
                if(currentLayer == null) return;

                final FeatureMapLayer bufferLayer = getLayer();
                final Filter bufferFilter = getFilter();

                if(bufferLayer == null || bufferFilter == null) return;

                //we have everything needed for pasting datas

                if(currentLayer.getFeatureSource() instanceof FeatureStore){
                    final FeatureStore<SimpleFeatureType,SimpleFeature> store =
                        (FeatureStore<SimpleFeatureType, SimpleFeature>) currentLayer.getFeatureSource();

                    if(currentLayer == bufferLayer){
                        //we are copying datas in the same datasource
                        //no need to check feature types
                        try {
                            FeatureCollection<SimpleFeatureType, SimpleFeature> features = bufferLayer.getFeatureSource().getFeatures(bufferFilter);
                            store.addFeatures(features);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }else{
                        //schema may not fit, we display a dialog to choose matching properties
                        final MappingChooser chooser = new MappingChooser(
                                bufferLayer.getFeatureSource().getSchema(),
                                currentLayer.getFeatureSource().getSchema());
                        chooser.showChooserDialog();
                        final FeatureMapper mapper = chooser.getMapper();

                        if(mapper != null){
                            try {
                                FeatureCollection<SimpleFeatureType, SimpleFeature> features = bufferLayer.getFeatureSource().getFeatures(bufferFilter);
                                store.addFeatures(new TransformReader(features, mapper));
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }

                    }
                }

            }
        });

    }

    private static class TransformReader implements FeatureReader<SimpleFeatureType,SimpleFeature>{

        private final FeatureCollection<SimpleFeatureType,SimpleFeature> col;
        private final Iterator<SimpleFeature> ite;
        private final FeatureMapper mapper;

        TransformReader(FeatureCollection<SimpleFeatureType,SimpleFeature> col, FeatureMapper mapper){
            this.col = col;
            this.ite = col.iterator();
            this.mapper = mapper;
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return col.getSchema();
        }

        @Override
        public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
            return mapper.transform(ite.next());
        }

        @Override
        public boolean hasNext() throws IOException {
            return ite.hasNext();
        }

        @Override
        public void close() throws IOException {
            col.close(ite);
        }

    }

}
