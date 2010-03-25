/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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

import com.vividsolutions.jts.geom.Geometry;
import gov.nasa.worldwind.globes.Globe;

import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotoolkit.data.DataStoreException;
import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.FeatureIterator;
import org.geotoolkit.data.query.Query;
import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.geometry.DefaultBoundingBox;
import org.geotoolkit.geometry.Envelope2D;
import org.geotoolkit.geometry.GeneralEnvelope;
import org.geotoolkit.map.FeatureMapLayer;
import org.geotoolkit.referencing.CRS;

import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Exceptions;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class FeatureLayerWWJ extends RenderableLayer{

    private static final FilterFactory2 FF = (FilterFactory2) FactoryFinder.getFilterFactory(null);

    private final Globe globe;
    private final FeatureMapLayer layer;
    private final Updater updater = new Updater();
    private final List<Renderable> currentRenderedFeatures = new ArrayList<Renderable>();

    public FeatureLayerWWJ(FeatureMapLayer featureLayer, Globe globe){
        this.layer = featureLayer;
        this.globe = globe;
        addRenderable(new BoundsListenerGL() {
            @Override
            public void boundsChange(Envelope env) {
                updater.updateEnvelope(env);
            }
        });
        updater.start();

    }

    private class Updater extends Thread{

        private final GeneralEnvelope lastEnv = new GeneralEnvelope(WorldWindConstants.EPSG_4326);
        private final GeneralEnvelope currentEnv = new GeneralEnvelope(WorldWindConstants.EPSG_4326);

        public void updateEnvelope(Envelope newEnv){
            synchronized(lastEnv){
                lastEnv.setRange(0, newEnv.getMinimum(0), newEnv.getMaximum(0));
                lastEnv.setRange(1, newEnv.getMinimum(1), newEnv.getMaximum(1));
            }
            synchronized(Updater.this){
                Updater.this.notify();
            }
        }

        @Override
        public void run() {
            while(true){

                while(true){
                    synchronized(lastEnv){
                        if(lastEnv.equals(currentEnv)){
                            break;
                        }
                        currentEnv.setRange(0, lastEnv.getMinimum(0), lastEnv.getMaximum(0));
                        currentEnv.setRange(1, lastEnv.getMinimum(1), lastEnv.getMaximum(1));
                    }
                    try {
                        refresh();
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                synchronized(Updater.this){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }

        private void refresh() throws MismatchedDimensionException, FactoryException, TransformException, DataStoreException{

            //we render only if the bbox is small enough, envelope is always in degree
            if(currentEnv.getSpan(0) > 1 && currentEnv.getSpan(1) > 1){
                System.out.println("BBox to large to display features.");
                //remove features
                for(Renderable r : currentRenderedFeatures){
                    removeRenderable(r);
                }
                currentRenderedFeatures.clear();
                return;
            }

            final FeatureCollection<? extends Feature> col = layer.getCollection()
                    .subCollection(prepareQuery(new DefaultBoundingBox(currentEnv), layer));

            final CoordinateReferenceSystem dataCrs = col.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem();
            final MathTransform trs = CRS.findMathTransform(dataCrs, WorldWindConstants.EPSG_4326,true);
            final List<Renderable> toAdd = new ArrayList<Renderable>();

            final FeatureIterator<? extends Feature> features = col.iterator();
            try{
                int i =0;
                while(features.hasNext()){
                    i++;
                    final SimpleFeature sf = (SimpleFeature) features.next();
                    Collection<? extends Renderable> r = WorldWindUtils.toRenderable((Geometry)sf.getDefaultGeometry(),trs,globe);
                    if(r != null){
                        toAdd.addAll(r);
                    }

                    if(i > 5000){
                        //we dont display over 5000 features
                        break;
                    }

                }
            }finally{
                features.close();
            }

            //replace the renderable
            for(Renderable r : currentRenderedFeatures){
                removeRenderable(r);
            }
            currentRenderedFeatures.clear();
            currentRenderedFeatures.addAll(toAdd);
            addRenderables(toAdd);
        }

    }


    protected static Query prepareQuery(BoundingBox bbox, FeatureMapLayer layer){

        final FeatureCollection<SimpleFeature> fs                = (FeatureCollection<SimpleFeature>) layer.getCollection();
        final FeatureType schema                                 = fs.getFeatureType();
        final String geomAttName                                 = schema.getGeometryDescriptor().getLocalName();
        final CoordinateReferenceSystem bboxCRS                  = bbox.getCoordinateReferenceSystem();
        final CoordinateReferenceSystem layerCRS                 = schema.getCoordinateReferenceSystem();

        if( !CRS.equalsIgnoreMetadata(layerCRS,bboxCRS)){
            //BBox and layer bounds have different CRS. reproject bbox bounds
            Envelope env;

            try{
                env = CRS.transform(bbox, layerCRS);
            }catch(TransformException ex){
                //TODO is fixed in geotidy, the result envelope will have infinte values where needed
                //TODO should do something about this, since canvas bounds may be over the crs bounds
                ex.printStackTrace();
                env = new Envelope2D();
            }catch(IllegalArgumentException ex){
                //looks like the coordinate of the bbox are outside of the crs valide area.
                //some crs raise this error, other not.
                //if so we should reduce our bbox to the valide extent of the crs.

                final GeographicBoundingBox gbox = CRS.getGeographicBoundingBox(layerCRS);

                if(gbox == null){
                    env = new GeneralEnvelope(layerCRS);
                }else{
                    env = new GeneralEnvelope(gbox);
                }

            }catch(Exception ex){
                //we should not catch this but we must not block the canvas
                ex.printStackTrace();
                return null;
            }

            //TODO looks like the envelope after transform operation doesnt have always exactly the same CRS.
            //fix CRS classes method and remove the two next lines.
            env = new GeneralEnvelope(env);
            ((GeneralEnvelope)env).setCoordinateReferenceSystem(layerCRS);

            bbox = new DefaultBoundingBox(env);
        }

        Filter filter;

        //final Envelope layerBounds = layer.getBounds();
        //we better not do any call to the layer bounding box before since it can be
        //really expensive, the datastore is the best placed to check if he might
        //optimize the filter.
        //if( ((BoundingBox)bbox).contains(new DefaultBoundingBox(layerBounds))){
            //the layer bounds overlaps the bbox, no need for a spatial filter
        //   filter = Filter.INCLUDE;
        //}else{
        //make a bbox filter
        filter = FF.bbox(FF.property(geomAttName),bbox);
        //}

        //concatenate geographique filter with data filter if there is one
        if(layer.getQuery() != null && layer.getQuery().getFilter() != null){
            filter = FF.and(filter,layer.getQuery().getFilter());
        }


        final QueryBuilder qb = new QueryBuilder();
        qb.setTypeName(schema.getName());
        qb.setFilter(filter);
        qb.setProperties(new Name[]{schema.getGeometryDescriptor().getName()});
        final Query query = qb.buildQuery();
        return query;
    }

}
