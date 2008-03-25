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
package org.puzzle.puzzlecore.gtextend.widget.j2drenderer;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 *
 * @author johann sorel
 */
public class J2DRenderer implements GTRenderer {

    private MapContext context = null;

    //not necessary for test
    public void stopRendering() {
    }

    public void addRenderListener(RenderListener arg0) {
    }

    public void removeRenderListener(RenderListener arg0) {
    }

    public void setJava2DHints(RenderingHints arg0) {
    }

    public RenderingHints getJava2DHints() {
        return null;
    }

    public void setRendererHints(Map<Object, Object> arg0) {
    }

    public Map<Object, Object> getRendererHints() {
        return null;
    }

    public void paint(Graphics2D arg0, Rectangle arg1, Envelope arg2) {
    }

    public void paint(Graphics2D arg0, Rectangle arg1, ReferencedEnvelope arg2) {
    }

    public void paint(Graphics2D arg0, Rectangle arg1, Envelope arg2, AffineTransform arg3) {
    }

    public void paint(Graphics2D arg0, Rectangle arg1, ReferencedEnvelope arg2, AffineTransform arg3) {
    }

    public void setContext(MapContext context) {
        this.context = context;
    }

    public MapContext getContext() {
        return context;
    }

    public void paint(Graphics2D g2d, Rectangle rect, AffineTransform aff) {

        MapLayer[] layers = context.getLayers();

        try {
            for (MapLayer layer : layers) {
                FeatureCollection<SimpleFeatureType, SimpleFeature> fc = filter(layer);
                FeatureIterator<SimpleFeature> fi = fc.features();
                while(fi.hasNext()){
                    Feature feature = fi.next();
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private FeatureCollection filter(MapLayer layer) throws IOException {
        return layer.getFeatureSource().getFeatures();
    }
    
    
    private Geometry project(SimpleFeature feature){
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        return null;
    }
    
}
