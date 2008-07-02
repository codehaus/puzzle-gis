/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.context;

import java.util.Collection;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.CollectionSource;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Administrateur
 */
public class RichMapLayer extends DefaultMapLayer {

    private final LayerSource source ;
    
    public RichMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style,String title, LayerSource source) {
        super(featureSource, style, title);
        this.source = source;
    }

    public RichMapLayer(CollectionSource collec, Style style, String title, LayerSource source) {
        super(collec,style,title);
        this.source = source;
    }

    public RichMapLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style, LayerSource source) {
        super(featureSource, style, "");
        this.source = source;
    }

    public RichMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, Style style,String title, LayerSource source) {
        super(collection,style,title);
        this.source = source;
    }

    public RichMapLayer(Collection collection, Style style,String title, LayerSource source) {
        super(collection,style,title);
        this.source = source;
    }

    public RichMapLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, Style style, LayerSource source) {
        super(collection,style);
        this.source = source;
    }

    public RichMapLayer(GridCoverage coverage, Style style, LayerSource source) 
            throws TransformException, FactoryRegistryException, SchemaException, IllegalAttributeException {
        super(coverage,style);
        this.source = source;
    }

    public RichMapLayer(GridCoverage coverage, Style style, String title, LayerSource source)
            throws TransformException, FactoryRegistryException, SchemaException, IllegalAttributeException {
       super(coverage,style,title);
       this.source = source;
    }
    
    public final LayerSource getLayerSource(){
        return source;
    }
    
}
