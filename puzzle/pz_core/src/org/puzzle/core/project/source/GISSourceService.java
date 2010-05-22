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
package org.puzzle.core.project.source;

import org.openide.util.Lookup;

/**
 * This class describe a generic way of managing {@code GISSource}s.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
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

    Lookup getLookup();
    
    /**
     * Rebuild the {@code GISSource} from the parameters of the XML file.
     * @param   parameters  The parameters of the XML file.
     * @param   id          The source ID.
     * @return  The rebuilt {@code GISSource}.
     * @throws  java.lang.IllegalArgumentException
     */
    GISSource restoreSource(GISSourceInfo info) throws IllegalArgumentException;
    
}
