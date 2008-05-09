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

package org.puzzle.shell.misc;

import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.ToolTreePath;


/**
 *
 * @author johann sorel
 */
class DefaultShellTreePath implements ToolTreePath{

    private static final String[] EMPTY_STRING_ARRAY = {};
    
    private final List<String> paths = new ArrayList<String>();
    
    DefaultShellTreePath(ToolTreePath father, String i18nKey){
        String myPath = NbBundle.getMessage(DefaultShellTreePath.class, i18nKey);
        
        if(father != null){
            paths.addAll(((DefaultShellTreePath)father).getInerPath());
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
    public String[] getPath(){
        return paths.toArray(EMPTY_STRING_ARRAY);        
    }
    
           
}
