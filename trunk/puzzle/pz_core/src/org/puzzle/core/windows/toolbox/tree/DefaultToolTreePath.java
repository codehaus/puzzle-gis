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

package org.puzzle.core.windows.toolbox.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * tree path for tool.
 *
 * @author johann sorel (Puzzle-GIS)
 */
class DefaultToolTreePath implements ToolTreePath{

    private static final String[] EMPTY_STRING_ARRAY = {};
    
    private final List<String> paths = new ArrayList<String>();
    
    DefaultToolTreePath(final ToolTreePath father, final String i18nKey){
        final String myPath = Utilities.getString(i18nKey);
        
        if(father != null){
            paths.addAll(((DefaultToolTreePath)father).getInerPath());
        }
        paths.add(myPath);
    }
    
    List<String> getInerPath(){
        return paths;
    }
    
    /**
     * get the Sting array of this path
     * @return String[]
     */
    @Override
    public String[] getPath(){
        return paths.toArray(EMPTY_STRING_ARRAY);        
    }
    
}
