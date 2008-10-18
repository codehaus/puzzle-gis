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

import java.util.Map;
import org.puzzle.core.context.gui.datadialog.SourceCreationPane;

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
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 */
public interface GISDistantSourceService extends GISSourceService{
    /**
     * Creates a {@code sourceCreationPane} to allow the user entering informations
     * needed to retrieve distant datas. This {@code SourceCreationPane} is a graphical
     * component.
     * @return  A {@code SourceCreationPane}.
     */
    SourceCreationPane createPanel();
    
    /**
     * Creates the {@code GISSource} to associated with the distant data
     * described by the {@code parameters} entered by the user in the 
     * {@code DataPanel}.
     * @param   parameters  The parameters describing the distant data.
     * @return  A new {@code GISSource} associated with the distant data.
     * @throws  java.lang.IllegalArgumentException
     */
    GISSource createSource(Map parameters) throws IllegalArgumentException;
}
