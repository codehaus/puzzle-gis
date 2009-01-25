/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.analyze.tool.convert.vector;

import java.io.IOException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Utility class to transform a featureSource into a Datastore
 * @author kevin delfour (Geomatys)
 * @author Johann Sorel (Puzzle-GIS)
 */
public class TransformUtils {

    /**
     * Transform a FeatureSource into a DataStore
     * @param source The FeatureSource to transform
     * @param target The DataStore where the FeatureSource will be inserted
     * @return the new FeatureSource
     * @throws java.io.IOException 
     *          if the schema already exist or the user doesn't have the permission to write
     *          the DataStore target
     */
    public static FeatureSource<SimpleFeatureType, SimpleFeature> transform(FeatureSource<SimpleFeatureType, SimpleFeature> source, DataStore target) throws IOException {

        //Before create schema, we are looking for 
        for (String typeName : target.getTypeNames()) {
            if (typeName.equals(source.getName().getLocalPart())) {
                throw new IOException("Schema already exist.");
            }
        }

        target.createSchema(source.getSchema());

        final FeatureSource<SimpleFeatureType, SimpleFeature> featureTarget = target.getFeatureSource(target.getTypeNames()[0]);

        if (featureTarget instanceof FeatureStore) {
            FeatureStore<SimpleFeatureType, SimpleFeature> store = (FeatureStore<SimpleFeatureType, SimpleFeature>) featureTarget;

            DefaultTransaction transaction = new DefaultTransaction();
            store.setTransaction(transaction);

            try {
                store.addFeatures(source.getFeatures());
                transaction.commit();
            } catch (IOException ex) {
                try {
                    store.getTransaction().rollback();
                } catch (IOException e) {
                    throw e;
                }
                throw ex;
            } finally {
                transaction.close();
            }
            
            return store;

        } else {
            throw new IOException("You don't have permission to write in this DataStore.");
        }
    }
}
