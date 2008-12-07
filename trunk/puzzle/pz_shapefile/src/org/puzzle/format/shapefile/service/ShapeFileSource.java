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

package org.puzzle.format.shapefile.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapLayer;
import org.geotools.map.MapBuilder;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.JLayerChooser;
import org.puzzle.core.project.source.LayerChooserMonitor;
import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * Shapefile source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeFileSource extends GISSource{

    private String name;
    private FeatureSource<SimpleFeatureType,SimpleFeature> featureSource = null;
    
    
    ShapeFileSource(final GISSourceInfo info, final File shapefile){
        super(info);
        this.name = shapefile.getName();

        if(name.endsWith(".shp") || name.endsWith(".SHP")){
            name = name.substring(0, name.length()-4);
        }

        DataStore store = null;
        try {
            store = DataStoreFinder.getDataStore(Collections.singletonMap("url",shapefile.toURI().toURL()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MapLayer createLayer(Map<String, String> parameters) {
        final MutableStyle style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
        if(parameters == null) parameters = Collections.emptyMap();
        final LayerSource source = new LayerSource(getInfo().getID(), parameters,this);
        final MapLayer layer = MapBuilder.getInstance().createFeatureLayer(featureSource, style);
        layer.setUserPropertie(PZLayerConstants.KEY_LAYER_INFO, source);
        layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(name,"") );
        return layer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/shapefile/shapefile.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JLayerChooser createChooser(LayerChooserMonitor monitor) {
        return new LayerCreationComponent(monitor, this, name);
    }
    
}
