/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2008-2009, Johann Sorel, Thomas Bonavia
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
package org.puzzle.format.geotiff.service;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;

import org.geotoolkit.gui.swing.misc.FileFilterFactory;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.capabilities.FileSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * This is the service linked with the {@code GeoTiffSource}.
 * The service is used to manage this kind of source.
 * 
 * @author  Thomas Bonavia
 * @author  Johann Sorel (Puzzle-GIS)
 * 
 * @see     org.puzzle.core.project.source.GISFileSourceService
 */
public class GeoTiffSourceService extends AbstractGISSourceService implements FileSourceCreation{
    private static final String SERVICE_ID = "SingleGeoTiff";
    
    /** {@inheritDoc} */
    @Override
    public FileFilter createFilter() {
        return FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.GEOTIFF);
    }

    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return SERVICE_ID;
    }

    /** {@inheritDoc} */
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException {
        final String strURI = info.getParameters().get("uri").toString();
        
        if(strURI == null) throw new IllegalArgumentException("missing parameter url");

        File geotiff = null;
        try{
            geotiff = new File(new URI(strURI));
        }catch(URISyntaxException urise){
            Exceptions.printStackTrace(urise);
        }
        
        return new GeoTiffSource(info,geotiff);
    }
    
    /** {@inheritDoc} */
    @Override
    public GISSourceInfo createSourceInfo(final File file) throws IllegalArgumentException {
        final String uri = file.toURI().toString();
        final Map<String,Serializable> params = new HashMap<String, Serializable>();
        params.put("uri", uri);
        return new GISSourceInfo(GISSourceInfo.UNREGISTERED_ID, SERVICE_ID, params);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isValidFile(File file) {
        final String name = file.getName().toLowerCase();
        return (name.endsWith("tiff") || name.endsWith("tif"));
    }
    
    /** {@inheritDoc} */
    @Override
    public String getTitle(){
        return NbBundle.getMessage(GeoTiffSourceService.class, "geotiffTitle");
    }
}
