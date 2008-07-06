/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source;

import java.awt.Image;
import java.util.Map;
import org.puzzle.puzzlecore.context.RichMapLayer;

/**
 *
 * @author Johann Sorel
 */
public interface GISSource {

    int getID();
    
    String getServiceName();
    
    Map<String,String> getParameters();
    
    RichMapLayer createLayer(Map<String,String> parameters);
    
    Image getIcon(int type);
    
    String getTitle();
}
