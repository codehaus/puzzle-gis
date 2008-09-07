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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Exceptions;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISFileSourceService;
import org.puzzle.core.project.source.GISSource;

/**
 *
 * @author  Johann Sorel
 */
public class ShapeFileSourceService implements GISFileSourceService{
    private static final String TITLE = "Shapefile";
    
    @Override
    public String getIdentifier(){
        return "SingleShapeFile";
    }
    
    @Override
    public GISSource restoreSource(Map<String, String> parameters, int id) throws IllegalArgumentException{
        final String strURI = parameters.get("uri");
        
        if(strURI == null) throw new IllegalArgumentException("missing parameter uri");
        
        URI uri = null;
        try {
           uri = new URI(strURI);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalide parameter uri");
        }
        File shapeFile = new File(uri);
        GISSource shapeSource = new ShapeFileSource(shapeFile,getIdentifier(),id,parameters);
        return shapeSource;
    }

    @Override
    public FileFilter createFilter() {
        return FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE);
    }

    @Override
    public GISSource createSource(File file, GISProject mainProject) throws IllegalArgumentException {
        String uri = null;
        Map<String,String> params = new HashMap<String, String>();
         uri = file.toURI().toString();
        
        if(uri == null){
            throw new IllegalArgumentException("Not a valid File");
        }
        
        params.put("uri", uri);
        
        int id = -1;
        
        if(mainProject != null ){
            GISProject gis = (GISProject) mainProject;
            id = gis.getNextSourceID();
        }else{
            throw new IllegalArgumentException("Not a valid GIS project open");
        }
        
        return restoreSource(params, id);
    }

    @Override
    public boolean isValidFile(File file) {
        String name = file.getName().toLowerCase();
        if(name.endsWith("shp")) return true;
        else return false;
    }
    
    @Override
    public String getTitle() {
        return TITLE;
    }
}
