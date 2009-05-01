/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
 *  
 *  GPLv3 + Classpath exception
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
