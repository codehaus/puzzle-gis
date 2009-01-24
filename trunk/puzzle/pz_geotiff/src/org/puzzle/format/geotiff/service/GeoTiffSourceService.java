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
package org.puzzle.format.geotiff.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;

import org.geotools.gui.swing.misc.filter.FileFilterFactory;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.GISFileSourceService;
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
public class GeoTiffSourceService extends AbstractGISSourceService implements GISFileSourceService{
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
        final String strURI = info.getParameters().get("uri");
        
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
        final Map<String,String> params = new HashMap<String, String>();
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
