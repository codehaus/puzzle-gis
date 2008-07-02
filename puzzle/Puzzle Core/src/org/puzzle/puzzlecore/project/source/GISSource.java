/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source;

import java.util.Map;
import org.puzzle.puzzlecore.context.RichMapLayer;

/**
 *
 * @author Johann Sorel
 */
public interface GISSource {

    int getID();
    
    RichMapLayer createLayer(Map<String,String> parameters);
    
}
