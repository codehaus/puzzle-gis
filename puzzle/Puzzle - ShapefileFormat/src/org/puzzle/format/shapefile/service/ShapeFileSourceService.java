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
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileFilter;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Exceptions;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISFileSourceService;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 *
 * @author Johann Sorel
 */
public class ShapeFileSourceService implements GISFileSourceService{

    public String getIdentifier(){
        return "SingleShapeFile";
    }
    
    public GISSource restoreSource(Map<String, String> parameters, int id) throws IllegalArgumentException{
        final String url = parameters.get("url");
        
        if(url == null) throw new IllegalArgumentException("missing parameter url");
        
        File shapeFile = new File(url);
        GISSource shapeSource = new ShapeFileSource(shapeFile,getIdentifier(),id,parameters);
        return shapeSource;
    }

    public FileFilter createFilter() {
        return FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE);
    }

    public GISSource createSource(File file) throws IllegalArgumentException {
        
        String url = null;
        Map<String,String> params = new HashMap<String, String>();
        try {
            url = file.toURI().toURL().toString();
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        if(url == null){
            throw new IllegalArgumentException("Not a valid File");
        }
        
        params.put("url", url);
        
        Project mainProject = OpenProjects.getDefault().getMainProject();
        int id = -1;
        
        if(mainProject != null && mainProject instanceof GISProject){
            GISProject gis = (GISProject) mainProject;
            id = gis.getNextSourceID();
        }else{
            throw new IllegalArgumentException("Not a valid GIS project open");
        }
        
        
        return restoreSource(params, id);
    }

}
