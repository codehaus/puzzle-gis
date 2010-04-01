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

package org.puzzle.format.shapefile.service;

import java.awt.Image;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.DataStoreFinder;
import org.geotoolkit.storage.DataStoreException;

import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.AbstractDataStoreSource;

/**
 * Shapefile source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeFileSource extends AbstractDataStoreSource{

    ShapeFileSource(final GISSourceInfo info){
        super(info);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/shapefile/resources/shapefile.png");
    }

    @Override
    protected DataStore createDataStore(GISSourceInfo info) throws DataStoreException {
        final String strURI = info.getParameters().get("uri").toString();

        if(strURI == null) throw new DataStoreException("missing parameter uri");

        URL url = null;
        try {
           url = new URI(strURI).toURL();
        } catch (URISyntaxException ex) {
            throw new DataStoreException("Invalide parameter uri");
        } catch (MalformedURLException ex) {
            throw new DataStoreException("Invalide parameter uri");
        }

        return DataStoreFinder.getDataStore(Collections.singletonMap("url",(Serializable)url));
    }

}
