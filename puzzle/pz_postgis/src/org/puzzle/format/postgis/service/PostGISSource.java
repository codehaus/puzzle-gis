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
package org.puzzle.format.postgis.service;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.DataStoreException;
import org.geotoolkit.data.DataStoreFinder;
import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.util.RandomStyleFactory;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceState;
import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.capabilities.LayerCreation;
import org.puzzle.core.project.source.GISLayerSource;

import static org.geotoolkit.jdbc.JDBCDataStoreFactory.*;

/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PostGISSource extends GISSource{

    public static final String FEATURETYPENAME_PROP = "featuretype";

    private DataStore store = null;
    
    PostGISSource(final GISSourceInfo info){
        super(info);
        content.add(new PostGISLayerCreation());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/postgis/resources/postgres.png");
    }


    @Override
    public void unload() {
        store = null;
        setState(GISSourceState.UNLOADED);
    }

    @Override
    public void load() {
        if(store != null) return;

        final Map<String,Serializable> infosParams = getInfo().getParameters();
        final Map<String,Serializable> params = new HashMap<String,Serializable>();
        params.put(DBTYPE.getName().toString(),          "postgisng");
        params.put(HOST.getName().toString(),            infosParams.get(HOST.getName().toString()));
        params.put(PORT.getName().toString(),            Integer.valueOf( infosParams.get(PORT.getName().toString()).toString() ));
        params.put(SCHEMA.getName().toString(),          infosParams.get(SCHEMA.getName().toString()));
        params.put(DATABASE.getName().toString(),        infosParams.get(DATABASE.getName().toString()));
        params.put(USER.getName().toString(),            infosParams.get(USER.getName().toString()));
        params.put(PASSWD.getName().toString(),          infosParams.get(PASSWD.getName().toString()));

        try {
            store = DataStoreFinder.getDataStore(params);
        } catch (DataStoreException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        content.add(store);
        setState(GISSourceState.LOADED);
    }

    private class PostGISLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            load();

            final String featureName = parameters.get(FEATURETYPENAME_PROP);

            MapLayer layer;
            if(store != null){
                try{
                    final Name name = store.getFeatureType(featureName).getName();
                    final FeatureCollection<SimpleFeature> featureSource = store.createSession(true).getFeatureCollection(QueryBuilder.all(name));
                    final MutableStyle style = RandomStyleFactory.createRandomVectorStyle(featureSource);
                    layer = MapBuilder.createFeatureLayer(featureSource, style);
                }catch(DataStoreException ex){
                    layer = MapBuilder.createEmptyMapLayer();
                    Exceptions.printStackTrace(ex);
                }
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,PostGISSource.this);
            layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);
            layer.setDescription(new DefaultStyleFactory().description(featureName,"") );

            return layer;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            load();
            return new LayerCreationComponent(monitor, store, PostGISSource.this);
        }
    }

}
