/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.context;

import org.geotools.map.DefaultMapContext;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author Administrateur
 */
public class RichMapContext extends DefaultMapContext{

    public RichMapContext(CoordinateReferenceSystem crs){
        super(crs);
    }
    
    
}
