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
package org.puzzle.format.wfs.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.storage.DataStoreException;
import org.geotoolkit.data.DataStoreFinder;

import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.AbstractDataStoreSource;

/**
 * WFS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WFSSource extends AbstractDataStoreSource{

    WFSSource(final GISSourceInfo info){
        super(info);
    }

    @Override
    protected DataStore createDataStore(GISSourceInfo info) throws DataStoreException {
        final Map<String,Serializable> infosParams = getInfo().getParameters();
        final Map<String,Serializable> params = new HashMap<String,Serializable>();
//        params.put(DBTYPE.getName().toString(),          "postgisng");
//        params.put(HOST.getName().toString(),            infosParams.get(HOST.getName().toString()));
//        params.put(PORT.getName().toString(),            Integer.valueOf( infosParams.get(PORT.getName().toString()).toString() ));
//        params.put(SCHEMA.getName().toString(),          infosParams.get(SCHEMA.getName().toString()));
//        params.put(DATABASE.getName().toString(),        infosParams.get(DATABASE.getName().toString()));
//        params.put(USER.getName().toString(),            infosParams.get(USER.getName().toString()));
//        params.put(PASSWD.getName().toString(),          infosParams.get(PASSWD.getName().toString()));

        return DataStoreFinder.getDataStore(params);
    }

}
