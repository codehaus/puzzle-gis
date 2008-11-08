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
package org.puzzle.core.project.source;

import java.awt.Image;
import java.util.Collection;
import java.util.Map;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import org.puzzle.core.project.GISProject;

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
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 */
public abstract class GISSource {

    private final GISSourceInfo info;
    
    protected GISSource(final GISSourceInfo info){
        if(info == null || info.getID() < 0){
            throw new IllegalArgumentException("SourceInfo can not be null or have an invalid ID number");
        }
        this.info = info;
    }
    
    public final GISSourceInfo getInfo(){
        return info;
    }
    
    /**
     * Creates a new {@code MapLayer} from the data represented
     * by the {@code GISSource}.
     * @param   parameters A set of parameters used to create the layer.
     * @return  A new {@code PuzzleLayerConstants}.
     */
    public abstract MapLayer createLayer(Map<String,String> parameters);
    
    /**
     * create a wizard to see all possible layers.
     * Exemple : a database source displays all available GIS layers.
     * 
     * @param contexts
     * @param project
     */
    public abstract void showLayerWizard(Collection<? extends MapContext> contexts, GISProject project);
    
    /**
     * Get the icon to use for each kind of {@code GISSource}.
     * @param   type The type of icon to use (allows to define multiple icons).
     * @return  A new {@code Image} representing the icon.
     */
     public abstract Image getIcon(int type);
         
}
