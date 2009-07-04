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

import org.puzzle.core.project.source.*;

/**
 * This class is a special {@code GISSourceService} dedicated to distant datas,
 * such as tables in a database.
 * <br>
 * Indeed, to get access to this kind of datas, it is needed to know some
 * special parameters. For instance, in case of a database table, we need
 * <ul>
 *  <li>The server address,</li>
 *  <li>The port,</li>
 *  <li>The username/password,</li>
 *  <li>The name of the table,</li>
 *  <li>...</li>
 * </ul>
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 */
public interface DistantSourceCreation{
    /**
     * Creates a {@code sourceCreationPane} to allow the user entering informations
     * needed to retrieve distant datas. This {@code SourceCreationPane} is a graphical
     * component.
     * @return  A {@code SourceCreationPane}.
     */
    SourceCreationPane createPanel();
    
}
