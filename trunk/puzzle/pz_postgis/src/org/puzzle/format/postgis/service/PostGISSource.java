/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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

package org.puzzle.format.postgis.service;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapBuilder;
import org.geotools.map.MapLayer;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.JLayerChooser;
import org.puzzle.core.project.source.LayerChooserMonitor;
import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;

import static org.geotools.data.postgis.PostgisDataStoreFactory.*;

/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PostGISSource extends GISSource{

    public static final String FEATURETYPENAME_PROP = "featuretype";

    private final DataStore store;
    
    PostGISSource(final GISSourceInfo info){
        super(info);

        final Map<String,String> infosParams = info.getParameters();
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put(DBTYPE.key,          DBTYPE.sample);
        params.put(HOST.key,            infosParams.get(HOST.key));
        params.put(PORT.key,            infosParams.get(PORT.key));
        params.put(SCHEMA.key,          infosParams.get(SCHEMA.key));
        params.put(DATABASE.key,        infosParams.get(DATABASE.key));
        params.put(USER.key,            infosParams.get(USER.key));
        params.put(PASSWD.key,          infosParams.get(PASSWD.key));
        params.put(MAXCONN.key,         Integer.valueOf(infosParams.get(MAXCONN.key)));
        params.put(MINCONN.key,         Integer.valueOf(infosParams.get(MINCONN.key)));
        params.put(NAMESPACE.key,       infosParams.get(NAMESPACE.key));
        params.put(VALIDATECONN.key,    Boolean.valueOf(infosParams.get(VALIDATECONN.key)));
        params.put(ESTIMATEDEXTENT.key, Boolean.valueOf(infosParams.get(ESTIMATEDEXTENT.key)));
        params.put(LOOSEBBOX.key,       Boolean.valueOf(infosParams.get(LOOSEBBOX.key)));
        params.put(WKBENABLED.key,      Boolean.valueOf(infosParams.get(WKBENABLED.key)));
        
        DataStore temp = null;
        try {
            temp = DataStoreFinder.getDataStore(params);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        store = temp;

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MapLayer createLayer(Map<String, String> parameters) {

        try{
            final String featureName = parameters.get(FEATURETYPENAME_PROP);
            final FeatureSource featureSource = store.getFeatureSource(featureName);

            final MutableStyle style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
            final LayerSource source = new LayerSource(getInfo().getID(), parameters,this);
            final MapLayer layer = MapBuilder.getInstance().createFeatureLayer(featureSource, style);
            layer.setUserPropertie(PZLayerConstants.KEY_LAYER_INFO, source);
            layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(featureName,"") );
            return layer;
        }catch(IOException ex){
            System.out.println(ex);
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/postgis/postgres.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JLayerChooser createChooser(LayerChooserMonitor monitor) {
        return new LayerCreationComponent(monitor, store, this);
    }

}
