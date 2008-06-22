/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.filetype;

import java.io.File;
import java.io.IOException;
import org.apache.commons.collections.map.SingletonMap;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;

public class GISContextDataObject extends MultiDataObject {

    private final RandomStyleFactory RANDOM_STYLE_FACTORY = new RandomStyleFactory();
    
    public GISContextDataObject(FileObject pf, GISContextDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    
    public MapContext getContext(){
        MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        
        String path = "H:\\rec\\GIS_PAS_EDITER\\ADMIN_COMMUNE.SHP";
        DataStore store = null;
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = null;
        try {
            store = DataStoreFinder.getDataStore(new SingletonMap("url", new File(path).toURI().toURL()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            fs = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        Style style = RANDOM_STYLE_FACTORY.createRandomVectorStyle(fs);
        MapLayer layer = new DefaultMapLayer(fs, style);
        
        layer.setTitle("demo_polygon.shp");
        context.addLayer(layer);
        context.setTitle("DemoContext");
                
        return context;
    }
    
    
    @Override
    protected Node createNodeDelegate() {
        return new GISContextDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
}
