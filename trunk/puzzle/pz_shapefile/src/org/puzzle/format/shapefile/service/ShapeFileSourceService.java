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
package org.puzzle.format.shapefile.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;

import org.geotools.gui.swing.misc.filter.FileFilterFactory;

import org.puzzle.core.project.source.GISFileSourceService;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 *
 * @author  Johann Sorel
 */
public class ShapeFileSourceService implements GISFileSourceService{
    private static final String TITLE = "Shapefile";
    private static final String SERVICE_ID = "SingleShapeFile";
    
    @Override
    public String getIdentifier(){
        return SERVICE_ID;
    }
    
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException{
        final String strURI = info.getParameters().get("uri");
        
        if(strURI == null) throw new IllegalArgumentException("missing parameter uri");
        
        URI uri = null;
        try {
           uri = new URI(strURI);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalide parameter uri");
        }
        final File shapeFile = new File(uri);

        return new ShapeFileSource(info,shapeFile);
    }

    @Override
    public FileFilter createFilter() {
        return FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE);
    }

    @Override
    public GISSourceInfo createSourceInfo(File file) throws IllegalArgumentException {
        final String uri = file.toURI().toString();
        final Map<String,String> params = new HashMap<String, String>();
        params.put("uri", uri);
        return new GISSourceInfo(GISSourceInfo.UNREGISTERED_ID, SERVICE_ID, params);
    }

    @Override
    public boolean isValidFile(File file) {
        final String name = file.getName().toLowerCase();
        if(name.endsWith("shp")) return true;
        else return false;
    }
    
    @Override
    public String getTitle() {
        return TITLE;
    }
}
