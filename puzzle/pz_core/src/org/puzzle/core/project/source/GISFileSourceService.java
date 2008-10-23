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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class is a special {@code GISSourceService} dedicated to file datas.<br>
 * Indeed, files get a number of particular features such as "extension" etc, 
 * which should be handled in a special way.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 */
public interface GISFileSourceService extends GISSourceService{
    /**
     * Creates a {@code FileFilter} corresponding to the data file to handle.
     * @return  A new {@code FileFilter}.
     */
    FileFilter createFilter();
    
    /**
     * Creates the {@code GISSource} associated with the data {@code file}.
     * @param   file  The data to represent throw the {@code GISSource}.
     * @return  A new {@code GISSource} associated with the {@code file}.
     * @throws  java.lang.IllegalArgumentException
     */
    GISSourceInfo createSourceInfo(File file) throws IllegalArgumentException;
    
    /**
     * This method is used to know if a specific file can be handled by
     * a specific {@code GISFileSourceService}. For instance, a service
     * used to handle shapefiles will not be able to handle geotiff.
     * @param   file    The file to handle.
     * @return  A {@code boolean} :
     * <ul>
     *  <li>{@code true} : the file can be handled by this service,</li>
     *  <li>{@code false}: the file can not ben handled by the service.</li>
     * </ul>
     */
    boolean isValidFile(File file);
}
