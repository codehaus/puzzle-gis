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

package org.puzzle.format.shapefile.service;

import java.awt.Image;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.DataStoreFinder;
import org.geotoolkit.data.FeatureSource;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.util.RandomStyleFactory;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceState;
import org.puzzle.core.project.source.capabilities.LayerCreation;

/**
 * Shapefile source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeFileSource extends GISSource{

    private String name;
    private final File shapefile;
    private FeatureSource<SimpleFeatureType,SimpleFeature> featureSource = null;
    
    
    ShapeFileSource(final GISSourceInfo info, final File shapefile){
        super(info);
        this.shapefile = shapefile;
        this.name = shapefile.getName();

        if(name.endsWith(".shp") || name.endsWith(".SHP")){
            name = name.substring(0, name.length()-4);
        }

        content.add(new ShapeFileLayerCreation());

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/shapefile/shapefile.png");
    }

    @Override
    public void unload() {
        featureSource = null;
        setState(GISSourceState.UNLOADED);
    }

    @Override
    public void load() {
        if(featureSource != null) return;

        DataStore store = null;
        try {
            store = DataStoreFinder.getDataStore(Collections.singletonMap("url",(Serializable)shapefile.toURI().toURL()));
        } catch (Exception ex) {
            //we can not trust the underlying datastore, they sometime throw nullpointer errors
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        content.add(store);

        try {
            featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (Exception ex) {
            //we can not trust the underlying datastore, they sometime throw nullpointer errors
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        setState(GISSourceState.LOADED);
    }

    private class ShapeFileLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            if(parameters == null) parameters = Collections.emptyMap();
            load();

            final MapLayer layer;

            if(featureSource != null){
                final MutableStyle style = RandomStyleFactory.createRandomVectorStyle(featureSource);
                layer = MapBuilder.createFeatureLayer(featureSource, style);
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,ShapeFileSource.this);
            layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);
            layer.setDescription(new DefaultStyleFactory().description(name,"") );

            return layer;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            load();
            return new LayerCreationComponent(monitor, ShapeFileSource.this, name);
        }
    }


}
