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
public interface FileSourceCreation{
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
    
}
