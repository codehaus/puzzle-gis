
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
