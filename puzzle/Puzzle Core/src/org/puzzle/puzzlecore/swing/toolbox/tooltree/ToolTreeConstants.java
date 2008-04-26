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

package org.puzzle.puzzlecore.swing.toolbox.tooltree;

import java.lang.ref.WeakReference;

/**
 *
 * @author johann sorel
 */
public class ToolTreeConstants {

    private static WeakReference<ToolTreeConstants> ref = null;
    
    
    public final ToolTreePath ANALYSE = new ToolTreePath(null,"analyse");
    public final ToolTreePath ANALYSE_GEOMETRIE = new ToolTreePath(ANALYSE,"geometry");
    
    public final ToolTreePath DATABASE = new ToolTreePath(null,"database");
    public final ToolTreePath DATABASE_CONVERT = new ToolTreePath(DATABASE,"convert");
    public final ToolTreePath DATABASE_CREATE = new ToolTreePath(DATABASE,"create");
    
    public final ToolTreePath FILE = new ToolTreePath(null,"file");
    public final ToolTreePath FILE_CONVERT = new ToolTreePath(FILE,"convert");
    public final ToolTreePath FILE_CREATE = new ToolTreePath(FILE,"create");
    
    
    
        
    /**
     * ToolTreePaths instance
     * @return ToolTreePaths
     */
    public static ToolTreeConstants getInstance(){
     
        ToolTreeConstants cst = null;
        if(ref != null){
            cst = ref.get();
           }
        
        if(cst == null){
            cst = new ToolTreeConstants();
            ref = new WeakReference(cst);
        }
        
        return cst;        
    }
    
           
}
