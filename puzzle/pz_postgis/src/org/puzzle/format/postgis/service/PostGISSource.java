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
package org.puzzle.format.postgis.service;

import java.awt.Image;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.storage.DataStoreException;
import org.geotoolkit.data.DataStoreFinder;

import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.AbstractDataStoreSource;

import static org.geotoolkit.jdbc.JDBCDataStoreFactory.*;

/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PostGISSource extends AbstractDataStoreSource{

    PostGISSource(final GISSourceInfo info){
        super(info);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/postgis/resources/postgres.png");
    }

    @Override
    protected DataStore createDataStore(GISSourceInfo info) throws DataStoreException {
        final Map<String,Serializable> infosParams = info.getParameters();
        final Map<String,Serializable> params = new HashMap<String,Serializable>();
        params.put(DBTYPE.getName().toString(),          "postgisng");
        params.put(HOST.getName().toString(),            infosParams.get(HOST.getName().toString()));
        params.put(PORT.getName().toString(),            Integer.valueOf( infosParams.get(PORT.getName().toString()).toString() ));
        params.put(SCHEMA.getName().toString(),          infosParams.get(SCHEMA.getName().toString()));
        params.put(DATABASE.getName().toString(),        infosParams.get(DATABASE.getName().toString()));
        params.put(USER.getName().toString(),            infosParams.get(USER.getName().toString()));
        params.put(PASSWD.getName().toString(),          infosParams.get(PASSWD.getName().toString()));

        return DataStoreFinder.getDataStore(params);
    }

}
