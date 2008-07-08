/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.puzzlecore.project.source;

import java.awt.Image;
import java.util.Map;
import org.puzzle.puzzlecore.context.RichMapLayer;

/**
 * This interface allows to create some GIS "sources".<br>
 * A {@code GISSource} represents a GIS data in the project.
 * A {@code GISSource} could represent :
 * <ul>
 *  <li>A Shapefile,</li>
 *  <li>a Geotiff,</li>
 *  <li>a geodatabase table,</li>
 *  <li>...</li>
 * </ul>
 *  
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 */
public interface GISSource {

    int getID();
    
    /**
     * Get the {@code GISSourceService} name to use for managing the data
     * represented by each {@code GISSource}.
     * @return  A {@code String} containing the name of the service.
     */
    String getServiceName();
    
    /**
     * The parameters describing the {@code GISSource}.
     * This parameters are retrieved from the XML storing the {@code GISSource}.
     * @return A {@code Map} containing all parameters.
     */
    Map<String,String> getParameters();
    
    /**
     * Creates a new {@code MapLayer} from the data represented
     * by the {@code GISSource}.
     * @param   parameters A set of parameters used to create the layer.
     * @return  A new {@code RichMapLayer}.
     */
    RichMapLayer createLayer(Map<String,String> parameters);
    
    /**
     * Get the icon to use for each kind of {@code GISSource}.
     * @param   type The type of icon to use (allows to define multiple icons).
     * @return  A new {@code Image} representing the icon.
     */
    Image getIcon(int type);
    
    /**
     * Get the title of the {@code GISSource}. It makes sense to give to the
     * source the name of its data, but it is not an obligation !
     * @return  A {@code String} containing the name of the {@code GISSource}.
     */
    String getTitle();
}
