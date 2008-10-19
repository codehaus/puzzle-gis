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

package org.puzzle.core.tool;

import java.awt.Image;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class AbstractToolDescriptor implements ToolDescriptor{

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String[] getPath() {
        return EMPTY_STRING_ARRAY;
    }

    @Override
    public String[] getKeyWords() {
        return EMPTY_STRING_ARRAY;
    }

    @Override
    public String[] getCategories() {
        return EMPTY_STRING_ARRAY;
    }

    @Override
    public Image getIcon(int iconType){
        return null;
    }
}
