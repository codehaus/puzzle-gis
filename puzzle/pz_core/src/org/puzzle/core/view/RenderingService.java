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
package org.puzzle.core.view;

import java.awt.Image;

import org.geotoolkit.map.MapContext;
import org.puzzle.core.project.view.GISView;

/**
 * A rendering service is a service providing map view components.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public interface RenderingService {

    /**
     * A small image used to symbolize the rendering engine.
     *
     * @return Image or nul if none
     */
    Image getIcon();

    /**
     * The name of the rendering engine.
     *
     * @return String, never null.
     */
    String getTitle();

    /**
     * Get the service's identifier.
     * @return A {@code String} containing the id.
     */
    String getIdentifier();
    
    /**
     * Create a map view window for the provided mapcontext.
     *
     * @param context : mapcontext to display
     * @param info :
     * @return MapView
     */
    ViewComponent restoreView(MapContext context, GISView info);
    
}
