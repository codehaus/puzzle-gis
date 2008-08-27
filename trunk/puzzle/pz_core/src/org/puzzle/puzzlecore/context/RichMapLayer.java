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
package org.puzzle.puzzlecore.context;

import java.util.Collection;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.CollectionSource;
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

/**
 *
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.geotools.map.DefaultMapLayer
 */
public class RichMapLayer extends DefaultMapLayer {

    private final LayerSource source ;
    
    public RichMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, MutableStyle style,String title, LayerSource source) {
        super(featureSource, style, title);
        this.source = source;
    }

    public RichMapLayer(CollectionSource collec, MutableStyle style, String title, LayerSource source) {
        super(collec,style,title);
        this.source = source;
    }

    public RichMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, MutableStyle style, LayerSource source) {
        super(featureSource, style, "");
        this.source = source;
    }

    public RichMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, MutableStyle style,String title, LayerSource source) {
        super(collection,style,title);
        this.source = source;
    }

    public RichMapLayer(Collection collection, MutableStyle style,String title, LayerSource source) {
        super(collection,style,title);
        this.source = source;
    }

    public RichMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, MutableStyle style, LayerSource source) {
        super(collection,style);
        this.source = source;
    }

    public RichMapLayer(GridCoverage coverage, MutableStyle style, LayerSource source) 
            throws TransformException, FactoryRegistryException, SchemaException, IllegalAttributeException {
        super(coverage,style);
        this.source = source;
    }

    public RichMapLayer(GridCoverage coverage, MutableStyle style, String title, LayerSource source)
            throws TransformException, FactoryRegistryException, SchemaException, IllegalAttributeException {
       super(coverage,style,title);
       this.source = source;
    }
    
    public final LayerSource getLayerSource(){
        return source;
    }
    
}
