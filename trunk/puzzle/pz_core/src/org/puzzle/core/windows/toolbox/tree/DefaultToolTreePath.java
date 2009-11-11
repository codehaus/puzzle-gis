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

import java.util.ArrayList;
import java.util.List;
import org.puzzle.core.resources.CoreResource;

/**
 * tree path for tool.
 *
 * @author johann sorel (Puzzle-GIS)
 */
class DefaultToolTreePath implements ToolTreePath{

    private static final String[] EMPTY_STRING_ARRAY = {};
    
    private final List<String> paths = new ArrayList<String>();
    
    DefaultToolTreePath(final ToolTreePath father, final String i18nKey){
        final String myPath = CoreResource.getString(i18nKey);
        
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
