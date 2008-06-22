/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source.shapefile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.collections.map.SingletonMap;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.openide.util.Exceptions;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 *
 * @author Johann Sorel
 */
public class ShapeFileSource implements GISSource{

    private final int id;
    private final String name;
    private FeatureSource featureSource = null;
    
    
    ShapeFileSource(File shapefile, int id){
        this.id = id;
        this.name = shapefile.getName();
        DataStore store = null;
        try {
            store = DataStoreFinder.getDataStore(new SingletonMap("url", shapefile.toURI().toURL()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    public MapLayer createLayer(Map<String, String> parameters) {
        
        Style style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
        MapLayer layer = new DefaultMapLayer(featureSource, style);
        layer.setTitle(name);
        
        return layer;
    }

    public int getID() {
        return id;
    }

}
