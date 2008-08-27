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

import java.util.Map;

/**
 * This class describe a generic way of managing {@code GISSource}s.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 */
public interface GISSourceService {

    
    /**
     * Get the title of the service.
     * @return A {@code String} containing the title.
     */
    String getTitle();
    
    /**
     * Get the service's identifier.
     * @return A {@code String} containing the id.
     */
    String getIdentifier();
    
    /**
     * Rebuild the {@code GISSource} from the parameters of the XML file.
     * @param   parameters  The parameters of the XML file.
     * @param   id          The source ID.
     * @return  The rebuilt {@code GISSource}.
     * @throws  java.lang.IllegalArgumentException
     */
    GISSource restoreSource(Map<String,String> parameters,int id)
            throws IllegalArgumentException;
    
}
