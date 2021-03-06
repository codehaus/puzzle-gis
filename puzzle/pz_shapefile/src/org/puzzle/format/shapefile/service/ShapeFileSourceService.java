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

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;

import org.geotoolkit.gui.swing.misc.FileFormat;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.capabilities.FileSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.shapefile.resources.ShapefileResource;

/**
 * Shapefile sourcre creation service.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeFileSourceService extends AbstractGISSourceService implements FileSourceCreation{
    private static final String SERVICE_ID = "SingleShapeFile";

    public ShapeFileSourceService(){
        super();
    }

    @Override
    public String getIdentifier(){
        return SERVICE_ID;
    }
    
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException{
        final String strURI = info.getParameters().get("uri").toString();
        
        if(strURI == null) throw new IllegalArgumentException("missing parameter uri");
        
        return new ShapeFileSource(info);
    }

    @Override
    public Collection<FileFilter> createFilters() {
        return Collections.singleton(FileFormat.ESRI_SHAPEFILE.getFileFilter());
    }

    @Override
    public GISSourceInfo createSourceInfo(File file) throws IllegalArgumentException {
        final String uri = file.toURI().toString();
        final Map<String,Serializable> params = new HashMap<String, Serializable>();
        params.put("uri", uri);
        return new GISSourceInfo(GISSourceInfo.UNREGISTERED_ID, SERVICE_ID, params);
    }
    
    @Override
    public String getTitle() {
        return ShapefileResource.getString("shapeTitle");
    }

}
