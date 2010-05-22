/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.core.project.source.capabilities;

import java.util.Map;
import org.geotoolkit.map.MapLayer;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public interface LayerCreation {

    /**
     * Creates a new {@code MapLayer} from the data represented
     * by the {@code GISSource}.
     * @param   parameters A set of parameters used to create the layer.
     * @return  A new {@code PuzzleLayerConstants}.
     */
    MapLayer createLayer(Map<String,String> parameters);
    
    /**
     * Create a swing component that will be used to create layers from this source.
     * 
     * @param monitor
     * @return JLayerChooser
     */
    JLayerChooser createChooser(LayerChooserMonitor monitor);

}
