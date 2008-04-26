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
package org.puzzle.puzzlecore.swing.toolbox.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 *
 * @author johann sorel
 */
public class ClipProcess implements Process {

    private ProgressListener monitor = null;
    
    private final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private int x = 0;
    private int max = 1;
    private boolean running = false;
    private final FeatureCollection<SimpleFeatureType, SimpleFeature> inSource;
    private final FeatureCollection<SimpleFeatureType, SimpleFeature> clipSource;
    private final FeatureStore<SimpleFeatureType, SimpleFeature> outStore;
    private final Map<String, String> attributMapping;
    private final String geoAttributName;

    public ClipProcess(FeatureCollection<SimpleFeatureType, SimpleFeature> source,
            FeatureCollection<SimpleFeatureType, SimpleFeature> clip,
            FeatureStore<SimpleFeatureType, SimpleFeature> out) {
        this(source, clip, out, null, null);
    }

    public ClipProcess(FeatureCollection<SimpleFeatureType, SimpleFeature> source,
            FeatureCollection<SimpleFeatureType, SimpleFeature> clip,
            FeatureStore<SimpleFeatureType, SimpleFeature> out, Map<String, String> mapping) {
        this(source, clip, out, mapping, null);
    }

    public ClipProcess(FeatureCollection<SimpleFeatureType, SimpleFeature> source,
            FeatureCollection<SimpleFeatureType, SimpleFeature> clip,
            FeatureStore<SimpleFeatureType, SimpleFeature> out, Map<String, String> mapping,
            String geoAttributName) {
        inSource = source;
        clipSource = clip;
        outStore = out;

        //use default geometry for clip if attribut name is not set
        if (geoAttributName == null) {
            this.geoAttributName = source.getSchema().getDefaultGeometry().getName().toString();
        } else {
            this.geoAttributName = geoAttributName;
        }

        //Map only geometry attribut if mapping is not set
        if (mapping == null) {
            attributMapping = new HashMap<String, String>();
            attributMapping.put(source.getSchema().getDefaultGeometry().getName().toString(),
                    outStore.getSchema().getDefaultGeometry().getName().toString());
        } else {
            attributMapping = mapping;
        }

    }

    private void fillLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> lstFeatures, FeatureStore<SimpleFeatureType, SimpleFeature> store) throws IOException {

        DefaultTransaction transaction = new DefaultTransaction();
        store.setTransaction(transaction);

        IOException ioex = null;

        try {
            store.addFeatures(lstFeatures);
            transaction.commit();
        } catch (IOException ex) {
            try {
                store.getTransaction().rollback();
            } catch (IOException e) {
                ioex = e;
            }
            ioex = ex;
        } finally {
            transaction.close();
        }

        if (ioex != null) {
            throw ioex;
        }

    }

    private Geometry[] parseGeometry(Class outClass, Geometry outGeom) {

       
        Geometry[] geoms = {};

        Class geoClass = outGeom.getClass();
        Class POINT = Point.class;
        Class MULTI_POINT = MultiPoint.class;
        Class LINE = LineString.class;
        Class MULTILINE = MultiLineString.class;
        Class POLYGON = Polygon.class;
        Class MULTIPOLYGON = Point.class;
        Class MULTIGEO = GeometryCollection.class;

        if (outClass == geoClass) {

        } else if (outClass == POINT) {

             if (geoClass == POINT) {

            } else if (geoClass == MULTI_POINT) {

            } else if (geoClass == LINE) {

            } else if (geoClass == MULTILINE) {

            } else if (geoClass == POLYGON) {

            }


        } else if (outClass == MULTI_POINT) {

        } else if (outClass == LINE) {

        } else if (outClass == MULTILINE) {

        } else if (outClass == POLYGON) {

        } else if (outClass == MULTIPOLYGON) {

        } else if (outClass == MULTIGEO) {

        }

        if (outGeom instanceof Point) {
            outGeom = GEOMETRY_FACTORY.createMultiPoint(new Point[]{(Point) outGeom});
        } else if (outGeom instanceof LineString) {
            outGeom = GEOMETRY_FACTORY.createMultiLineString(new LineString[]{(LineString) outGeom});
        } else if (outGeom instanceof Polygon) {
            outGeom = GEOMETRY_FACTORY.createMultiPolygon(new Polygon[]{(Polygon) outGeom});
        }


        return geoms;
    }

    private void clip(
            CoordinateReferenceSystem inCRS,
            CoordinateReferenceSystem clipCRS,
            FeatureCollection<SimpleFeatureType, SimpleFeature> outCol,
            SimpleFeature inSF,
            SimpleFeatureType outType)
            throws FactoryException, MismatchedDimensionException, TransformException {

        MathTransform transformToInCRS = null;
        Geometry inGeom = (Geometry) inSF.getAttribute(geoAttributName);


        //build a MathTransfom if clip layer CRS is different from source layer
        if (!inCRS.equals(clipCRS)) {
            try {
                transformToInCRS = CRS.findMathTransform(clipCRS, inCRS, true);
            } catch (FactoryException ex) {
                throw ex;
            }
        }

        if (inGeom != null) {

            //We should make a second filter here to reduce the feature list ans speed up
            //JTSGeometryFactory geoFact = new JTSGeometryFactory();
            //FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
            //Filter filter = ff.intersects(ff.property(geoAtt), ff.literal(env));


            if (clipSource != null) {
                FeatureIterator<SimpleFeature> ite = clipSource.features();

                //we create the clipped geometry

                Geometry outGeom = null;
                while (ite.hasNext()) {
                    Geometry clipGeom = (Geometry) ite.next().getDefaultGeometry();

                    if (transformToInCRS != null) {
                        try {
                            clipGeom = JTS.transform((Geometry) clipGeom, transformToInCRS);
                        } catch (MismatchedDimensionException ex) {
                            throw ex;
                        } catch (TransformException ex) {
                            throw ex;
                        }
                    }

                    if (inGeom.intersects(clipGeom)) {
                        Geometry partGeom = inGeom.intersection(clipGeom);
                        outGeom = (outGeom == null) ? partGeom : outGeom.union(partGeom);
                    }
                }

                if (outGeom != null) {

                    //we verify the geometry is a Multi-Geometry
                    Class outClass = outType.getDefaultGeometry().getType().getBinding();
                    Class geoClass = outGeom.getClass();

                    if (outClass == geoClass) {

                    } else if (outClass == Point.class) {

                    } else if (outClass == MultiPoint.class) {

                    } else if (outClass == LineString.class) {

                    } else if (outClass == MultiLineString.class) {

                    } else if (outClass == Polygon.class) {

                    } else if (outClass == MultiPolygon.class) {

                    } else if (outClass == GeometryCollection.class) {

                    }

                    if (outGeom instanceof Point) {
                        outGeom = GEOMETRY_FACTORY.createMultiPoint(new Point[]{(Point) outGeom});
                    } else if (outGeom instanceof LineString) {
                        outGeom = GEOMETRY_FACTORY.createMultiLineString(new LineString[]{(LineString) outGeom});
                    } else if (outGeom instanceof Polygon) {
                        outGeom = GEOMETRY_FACTORY.createMultiPolygon(new Polygon[]{(Polygon) outGeom});
                    }

                    //we copy each attribut and replace the geometry
                    Set<String> keySet = attributMapping.keySet();
                    Iterator<String> keyIte = keySet.iterator();
                    Map<String, Object> mappedValues = new HashMap<String, Object>();

                    //we build a Map using the attribut mapping
                    while (keyIte.hasNext()) {
                        String inKey = keyIte.next();
                        String outKey = attributMapping.get(inKey);
                        AttributeDescriptor outAtt = outType.getAttribute(outKey);

                        if (inKey.equals(geoAttributName)) {
                            mappedValues.put(outAtt.getName().toString(), outGeom);
                        } else {
                            mappedValues.put(outAtt.getName().toString(), inSF.getAttribute(inKey));
                        }

                    }

                    //we build a Feature with all attribut of the output store
                    List<AttributeDescriptor> attributes = outType.getAttributes();
                    Object[] values = new Object[attributes.size()];

                    for (int i = 0,  n = attributes.size(); i < n; i++) {
                        AttributeDescriptor oneAttribut = attributes.get(i);
                        Object val = mappedValues.get(oneAttribut.getName().toString());
                        values[i] = (val != null) ? val : oneAttribut.getDefaultValue();
                    }

                    SimpleFeature myFeature = SimpleFeatureBuilder.build(outType, values, null);
                    outCol.add(myFeature);
                }

            }
        }

    }

    public boolean isProcessing() {
        return running;
    }

    public void process() {
        running = true;
        
        monitor.setTask(new SimpleInternationalString("Clip"));
        
        if(monitor != null){
            monitor.progress(0);
        }


        SimpleFeatureType inType = inSource.getSchema();
        CoordinateReferenceSystem inCRS = inType.getCRS();

        CoordinateReferenceSystem clipCRS = clipSource.getSchema().getCRS();

        String geoAtt = inType.getDefaultGeometry().getName().toString();

        //we make a first filter with envelope to limite the collection size
        ReferencedEnvelope env = clipSource.getBounds();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter filter = ff.intersects(ff.property(geoAtt), ff.literal(env));

        try {
            SimpleFeatureType outType = outStore.getSchema();

            FeatureCollection<SimpleFeatureType, SimpleFeature> inCol = inSource.subCollection(filter);
            FeatureIterator<SimpleFeature> ite = inCol.features();

            FeatureCollection<SimpleFeatureType, SimpleFeature> outCol = FeatureCollections.newCollection();
            max = inCol.size();
            
            if(monitor != null){
                monitor.progress(x/max);
            }

            x = 0;
            while (ite.hasNext()) {
                x++;
                SimpleFeature sf = ite.next();
                clip(inCRS, clipCRS, outCol, sf, outType);

                if(monitor != null){
                    monitor.progress(x/max);
                    monitor.setTask(new SimpleInternationalString("Clip : "+x+"/"+max) );
                }
            }

            fillLayer(outCol, outStore);

            if(monitor != null){
                monitor.complete();
                monitor.dispose();
            }
            
        } catch (Exception i) {
            if(monitor != null){
                monitor.exceptionOccurred(i);
            }
        }
    }

    public void setMonitor(ProgressListener monitor) {
        this.monitor = monitor;
    }

    public ProgressListener getMonitor() {
        return monitor;
    }

    public Object[] getOutputObjects() {
        return new Object[0];
    }

    public ProcessDescriptor getDescriptor() {
        return new ClipProcessDescriptor();
    }
}
