/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.view;

import org.geotools.map.MapContext;

/**
 *
 * @author Johann Sorel
 */
public interface RenderingService {

    MapView createView(MapContext context);
    
}
