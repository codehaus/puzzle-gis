/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.renderer.worldwind;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.WMSLayerFactory;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.wms.Capabilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.FeatureIterator;
import org.geotoolkit.geometry.jts.JTS;
import org.geotoolkit.map.ContextListener;
import org.geotoolkit.map.FeatureMapLayer;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.style.CollectionChangeEvent;
import org.geotoolkit.wms.WebMapServer;
import org.geotoolkit.wms.map.WMSMapLayer;
import org.opengis.feature.Feature;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import org.openide.util.Exceptions;

import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO3 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WorldWindMapView extends ViewComponent implements ContextListener {

    private final MapContext context;
    private final Map<MapLayer, Layer> links = new HashMap<MapLayer, Layer>();
    private final Model model;

    public WorldWindMapView(MapContext context) {
        super();
        this.context = context;
        context.addContextListener(this);
        setLayout(new BorderLayout());

        model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        parseContext();

        final WorldWindowGLCanvas canvas = new WorldWindowGLCanvas();
        canvas.setModel(model);

        add(BorderLayout.CENTER, canvas);
    }

    @Override
    public MapContext getContext() {
        return context;
    }

    private synchronized void parseContext() {

        for (MapLayer layer : context.layers()) {
            if (layer instanceof WMSMapLayer) {
                WMSMapLayer wms = (WMSMapLayer) layer;
                try {
                    Layer candidate = toWWJLayer(wms);
                    if(!wms.isVisible()){
                        candidate.setEnabled(false);
                    }
                    links.put(wms, candidate);
                    model.getLayers().add(candidate);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }else if(layer instanceof FeatureMapLayer){
                FeatureMapLayer fl = (FeatureMapLayer) layer;
                try {
                    Layer candidate = toWWJLayer(fl);
                    if(!fl.isVisible()){
                        candidate.setEnabled(false);
                    }
                    links.put(fl, candidate);
                    model.getLayers().add(candidate);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }

    public synchronized void layerChange(CollectionChangeEvent<MapLayer> eventList) {
        final int type = eventList.getType();

        if (type == CollectionChangeEvent.ITEM_ADDED) {
            for (MapLayer layer : eventList.getItems()) {
                if (layer instanceof WMSMapLayer) {
                    final WMSMapLayer wmsLayer = (WMSMapLayer) layer;
                    try {
                        Layer candidate = toWWJLayer(wmsLayer);
                        if(!wmsLayer.isVisible()){
                            candidate.setEnabled(false);
                        }
                        links.put(wmsLayer, candidate);
                        model.getLayers().add(candidate);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }else if(layer instanceof FeatureMapLayer){
                    FeatureMapLayer fl = (FeatureMapLayer) layer;
                    try {
                        Layer candidate = toWWJLayer(fl);
                        if(!fl.isVisible()){
                            candidate.setEnabled(false);
                        }
                        links.put(fl, candidate);
                        model.getLayers().add(candidate);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        } else if (type == CollectionChangeEvent.ITEM_CHANGED) {
            for(MapLayer layer : eventList.getItems()){
                final Layer candidate = links.get(layer);

                if(candidate != null){
                    //check the visibility level
                    candidate.setEnabled(layer.isVisible());
                }
            }

        } else if (type == CollectionChangeEvent.ITEM_REMOVED) {
            for (MapLayer layer : eventList.getItems()) {
                final Layer candidate = links.get(layer);

                if(candidate != null){
                    links.remove(layer);
                    model.getLayers().remove(candidate);
                }

            }
        }
    }

    private Layer toWWJLayer(WMSMapLayer wms) throws Exception {
        final WebMapServer server = wms.getServer();
        System.out.println("URI : " + server.getURI());
        final Capabilities cap = Capabilities.retrieve(server.getURI(), "WMS");

        final AVList params = new AVListImpl();
        params.setValue(AVKey.LAYER_NAMES, wms.getCombinedLayerNames());
        params.setValue(AVKey.TILE_WIDTH, 512);
        params.setValue(AVKey.TILE_HEIGHT, 512);
        params.setValue(AVKey.STYLE_NAMES, "");
//                    params.setValue(AVKey.DATA_CACHE_NAME, "Earth/NASA LandSat I3");
//                    params.setValue(AVKey.SERVICE, "http://worldwind25.arc.nasa.gov/lstile/lstile.aspx");
//                    params.setValue(AVKey.DATASET_NAME,  wms.getCombinedLayerNames());
//                    params.setValue(AVKey.FORMAT_SUFFIX, ".dds");
//                    params.setValue(AVKey.NUM_LEVELS, 10);
//                    params.setValue(AVKey.NUM_EMPTY_LEVELS, 4);
//                    params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(Angle.fromDegrees(36d), Angle.fromDegrees(36d)));
        params.setValue(AVKey.SECTOR, Sector.FULL_SPHERE);
        Layer candidate = WMSLayerFactory.newLayer(cap, params);
        return candidate;
    }

    private Layer toWWJLayer(FeatureMapLayer featureLayer) throws Exception {
        final RenderableLayer wwj = new RenderableLayer();

        final FeatureCollection<? extends Feature> col = featureLayer.getCollection();

        final CoordinateReferenceSystem dataCrs = col.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem();
        final MathTransform trs = CRS.findMathTransform(dataCrs, CRS.decode("EPSG:4326"),true);

        final FeatureIterator<? extends Feature> features = col.iterator();

        try{
            int i =0;
            while(features.hasNext()){
                i++;
                final SimpleFeature sf = (SimpleFeature) features.next();
                Renderable r = toRenderable((Geometry)sf.getDefaultGeometry(),trs);
                if(r != null){
                    wwj.addRenderable(r);
                }

                if(i > 500){
                    break;
                }

            }
        }finally{
            features.close();
        }

        return wwj;
    }


    private Renderable toRenderable(Geometry geom, MathTransform trs) throws MismatchedDimensionException, TransformException{

        if(geom instanceof Point){

        }else if(geom instanceof MultiPoint){

        }else if(geom instanceof LineString){

        }else if(geom instanceof MultiLineString){
            return toRenderable((MultiLineString)geom,trs);
        }else if(geom instanceof Polygon){

        }else if(geom instanceof MultiPolygon){
            return toRenderable((MultiPolygon)geom,trs);

        }

        return null;
    }

    private Renderable toRenderable(MultiLineString line, MathTransform trs) throws MismatchedDimensionException, TransformException{
        final Geometry geoEPSGWGS84 = JTS.transform(line, trs);
        final Coordinate[] coords = geoEPSGWGS84.getCoordinates();

        List<Position> positions = new ArrayList<Position>();
        for (int i = 0; i < coords.length; i++) {
            positions.add(new Position(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y), coords[i].z));
        }

        Polyline wwjLine = new Polyline(); 
        wwjLine.setPositions(positions);
        wwjLine.setFollowTerrain(true);
        wwjLine.setLineWidth(1);
        wwjLine.setColor(Color.RED);

        return wwjLine;
    }

    private Renderable toRenderable(MultiPolygon polygon, MathTransform trs) throws MismatchedDimensionException, TransformException{
        final Geometry geoEPSGWGS84 = JTS.transform(polygon, trs);
        final Coordinate[] coords = geoEPSGWGS84.getCoordinates();

        List<LatLon> latlons = new ArrayList<LatLon>();
        for (int i = 0; i < coords.length; i++) {
            latlons.add(new LatLon(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y)));
        }

        gov.nasa.worldwind.render.airspaces.Polygon wwjLine = new gov.nasa.worldwind.render.airspaces.Polygon();
        wwjLine.setLocations(latlons);
        wwjLine.setTerrainConforming(true);
        wwjLine.setAltitudes(5, 30);
        wwjLine.setEnableCaps(false);
        //wwjLine.setEnableCaps(false);
        wwjLine.setEnableLevelOfDetail(false);
//        wwjLine.setPositions(positions);
//        wwjLine.setFollowTerrain(true);
//        wwjLine.setLineWidth(1);
//        wwjLine.setColor(Color.RED);

        return wwjLine;
    }


}
