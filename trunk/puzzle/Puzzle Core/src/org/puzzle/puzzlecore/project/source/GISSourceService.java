/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source;

import java.util.Map;

/**
 *
 * @author Johann Sorel
 */
public interface GISSourceService {

    String getIdentifier();
    
    GISSource createSource(Map<String,String> parameters,int id) throws IllegalArgumentException;
}
