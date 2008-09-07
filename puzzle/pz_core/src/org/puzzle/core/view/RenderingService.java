/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.core.view;

import java.awt.Image;
import org.geotools.map.MapContext;

/**
 *
 * @author Johann Sorel
 */
public interface RenderingService {

    public Image getIcon();

    public String getTitle();
    
    MapView createView(MapContext context);
    
}
