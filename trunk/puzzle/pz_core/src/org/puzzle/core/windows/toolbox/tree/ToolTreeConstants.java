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

package org.puzzle.core.windows.toolbox.tree;

import java.lang.ref.WeakReference;

/**
 * Tool tree constants.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ToolTreeConstants {

    private static WeakReference<ToolTreeConstants> ref = null;
    
    public final ToolTreePath ANALYSE = new DefaultToolTreePath(null,"analyse");
    public final ToolTreePath ANALYSE_GEOMETRIE = new DefaultToolTreePath(ANALYSE,"geometry");
    
    public final ToolTreePath DATABASE = new DefaultToolTreePath(null,"database");
    public final ToolTreePath DATABASE_CONVERT = new DefaultToolTreePath(DATABASE,"convert");
    public final ToolTreePath DATABASE_CREATE = new DefaultToolTreePath(DATABASE,"create");

    public final ToolTreePath CONVERT = new DefaultToolTreePath(null,"convert");

    public final ToolTreePath FILE = new DefaultToolTreePath(null,"file");
    public final ToolTreePath FILE_CONVERT = new DefaultToolTreePath(FILE,"convert");
    public final ToolTreePath FILE_CREATE = new DefaultToolTreePath(FILE,"create");

    private ToolTreeConstants(){}

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
            ref = new WeakReference<ToolTreeConstants>(cst);
        }
        
        return cst;        
    }
    
           
}
