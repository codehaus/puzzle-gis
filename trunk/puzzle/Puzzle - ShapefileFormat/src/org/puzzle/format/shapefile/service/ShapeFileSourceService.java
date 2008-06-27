
package org.puzzle.format.shapefile.service;

import java.io.File;
import java.util.Map;
import org.puzzle.puzzlecore.project.source.GISSource;
import org.puzzle.puzzlecore.project.source.GISSourceService;

/**
 *
 * @author Johann Sorel
 */
public class ShapeFileSourceService implements GISSourceService{

    public String getIdentifier(){
        return "SingleShapeFile";
    }
    
    public GISSource createSource(Map<String, String> parameters, int id) throws IllegalArgumentException{
        final String url = parameters.get("url");
        
        if(url == null) throw new IllegalArgumentException("missing parameter url");
        
        File shapeFile = new File(url);
        GISSource shapeSource = new ShapeFileSource(shapeFile,id);
        return shapeSource;
    }

}
