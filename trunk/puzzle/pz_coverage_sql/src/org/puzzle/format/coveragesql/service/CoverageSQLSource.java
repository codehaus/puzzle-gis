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
package org.puzzle.format.coveragesql.service;

import java.awt.Image;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.geotoolkit.coverage.io.CoverageStoreException;
import org.geotoolkit.coverage.sql.CoverageDatabase;
import org.geotoolkit.coverage.sql.LayerCoverageReader;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.sql.WrappedDataSource;
import org.geotoolkit.util.RandomStyleFactory;
import org.openide.util.Exceptions;

import org.openide.util.ImageUtilities;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.capabilities.LayerCreation;


/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class CoverageSQLSource extends GISSource{

    public static final String KEY_SERVER           = "server";
    public static final String KEY_PORT             = "port";
    public static final String KEY_DATABASE         = "database";
    public static final String KEY_SCHEMA           = "schema";
    public static final String KEY_USER             = "user";
    public static final String KEY_PASSWORD         = "password";
    public static final String KEY_READONLY         = "readOnly";
    public static final String KEY_DRIVER           = "driver";
    public static final String KEY_ROOT_DIRECTORY   = "rootDirectory";

    public static final String COVERAGENAME_PROP   = "coverageName";

    private CoverageDatabase database = null;


    CoverageSQLSource(final GISSourceInfo info){
        super(info);
        content.add(new  DimapLayerCreation());
    }

    public CoverageDatabase getDatabase() {
        return database;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/coveragesql/resources/coverageSQL.png");
    }

    @Override
    public synchronized void unload() {
        database.dispose();
        database = null;
    }

    @Override
    public synchronized void load() {
        if(database != null) return;

        final Map<String,Serializable> infosParams = getInfo().getParameters();

        final Properties properties = new Properties();
        properties.put(KEY_SERVER, infosParams.get(KEY_SERVER));
        properties.put(KEY_PORT, infosParams.get(KEY_PORT));
        properties.put(KEY_DATABASE, infosParams.get(KEY_DATABASE));
        properties.put(KEY_SCHEMA, infosParams.get(KEY_SCHEMA));
        properties.put(KEY_USER, infosParams.get(KEY_USER));
        properties.put(KEY_PASSWORD, infosParams.get(KEY_PASSWORD));
        properties.put(KEY_ROOT_DIRECTORY, infosParams.get(KEY_ROOT_DIRECTORY));

        final PGConnectionPoolDataSource pool = new PGConnectionPoolDataSource();
        pool.setServerName(infosParams.get(KEY_SERVER).toString());
        pool.setPortNumber(Integer.valueOf(infosParams.get(KEY_PORT).toString()));
        pool.setDatabaseName(infosParams.get(KEY_DATABASE).toString());
        pool.setUser(infosParams.get(KEY_USER).toString());
        pool.setPassword(infosParams.get(KEY_PASSWORD).toString());
        //pool.setLoginTimeout(5);

        final DataSource dataSource = new WrappedDataSource(pool);

        this.database = new CoverageDatabase(dataSource, properties);
    }

    private class DimapLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            load();

            final String coverageName = parameters.get(COVERAGENAME_PROP);

            try {
                final LayerCoverageReader coverageReader = database.createGridCoverageReader(coverageName);
                final MapLayer layer = MapBuilder.createCoverageLayer(coverageReader, RandomStyleFactory.createRasterStyle(), coverageName);

                final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,CoverageSQLSource.this);
                layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);

                return layer;
            } catch (CoverageStoreException ex) {
                Exceptions.printStackTrace(ex);
                return null;
            }
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            load();
            return new LayerCreationComponent(monitor, CoverageSQLSource.this);
        }
    }

}
