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
import java.util.Collection;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.MapLayerBuilder;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;

/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PostGISSource extends GISSource{

    private final int id;
    private final Map<String,String> parameters;
    private final String name;
    private final String serviceName;
    private FeatureSource featureSource = null;
    
    PostGISSource(String serviceName,int id, Map<String,String> parameters){
        super(null);
        this.id = id;
        this.name = "name";
        this.serviceName = serviceName;
        this.parameters = parameters;
    }
    
    @Override
    public MapLayer createLayer(Map<String, String> parameters) {
        final MutableStyle style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
        final LayerSource source = new LayerSource(id, parameters,this);
        final MapLayerBuilder builder = new MapLayerBuilder();
        final MapLayer layer = builder.create(featureSource, style);
        layer.setUserPropertie(PZLayerConstants.KEY_LAYER_INFO, source);
        layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(name,"") );
        return layer;
    }


    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/postgis/postgres.png");
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public void showLayerWizard(Collection<? extends MapContext> contexts, GISProject project) {
    }

}
