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

package org.puzzle.puzzlecore.swing.toolbox.process;

import java.util.Map;
import org.geotools.gui.swing.toolbox.Parameter;
import org.opengis.util.ProgressListener;

/**
 *
 * @author johann sorel
 */
public class ClipProcessDescriptor implements ProcessDescriptor{

    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValid(Map parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Process createProcess(Map parameters) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Process create(Map parameters, ProgressListener monitor) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Parameter[] getParametersInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
