/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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
package org.puzzle.format;

import java.awt.Image;
import java.util.Map;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.storage.DataStoreException;
import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.util.RandomStyleFactory;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.openide.util.Exceptions;

import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceState;
import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.capabilities.LayerCreation;
import org.puzzle.core.project.source.GISLayerSource;

/**
 * Abstract Datastore source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class AbstractDataStoreSource extends GISSource{

    public static final String FEATURETYPENAME_PROP = "featuretype";

    private DataStore store = null;
    
    protected AbstractDataStoreSource(final GISSourceInfo info){
        super(info);
        content.add(new DataStoreLayerCreation());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return null;
    }

    @Override
    public void unload() {
        store = null;
        setState(GISSourceState.UNLOADED);
    }

    @Override
    public final void load() {
        if(store != null) return;

        try{
            store = createDataStore(getInfo());
        }catch(DataStoreException ex){
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        content.add(store);
        setState(GISSourceState.LOADED);
    }

    protected abstract DataStore createDataStore(GISSourceInfo info) throws DataStoreException;

    private class DataStoreLayerCreation implements LayerCreation{

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

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,AbstractDataStoreSource.this);
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
            return new DataStoreLayerCreationComponent(monitor, store, AbstractDataStoreSource.this);
        }
    }

}
