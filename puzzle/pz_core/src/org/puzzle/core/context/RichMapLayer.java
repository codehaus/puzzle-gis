/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.core.context;

import java.util.Collection;
import org.geotools.data.FeatureSource;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.map.DefaultMapLayer;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.TransformException;
import org.geotools.style.MutableStyle;
import org.opengis.feature.IllegalAttributeException;
import org.puzzle.core.project.source.GISSource;

/**
 *
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.geotools.map.DefaultMapLayer
 */
public class RichMapLayer extends DefaultMapLayer {

    private final LayerSource info ;
    private final GISSource source;
    
    public RichMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> collection, MutableStyle style, GISSource src, LayerSource info) {
        super(collection,style);
        this.source = src;
        this.info = info;
    }
    
    public RichMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, MutableStyle style, GISSource src, LayerSource info) {
        super(collection,style);
        this.source = src;
        this.info = info;
    }

    public RichMapLayer(GridCoverage coverage, MutableStyle style, GISSource src, LayerSource info)
            throws TransformException, FactoryRegistryException, SchemaException, IllegalAttributeException {
        super(coverage,style);
        this.source = src;
        this.info = info;
    }
    
    public final LayerSource getSourceParameters(){
        return info;
    }

    public final GISSource getSource(){
        return source;
    }
    
}
