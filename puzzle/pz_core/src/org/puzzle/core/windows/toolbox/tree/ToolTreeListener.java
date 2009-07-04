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

import java.util.EventListener;

import org.puzzle.core.tool.ToolDescriptor;

/**
 * Listener for tool tree
 *
 * @author johann sorel (Puzzle-GIS)
 */
public interface ToolTreeListener extends EventListener{

    public void treeToolActivated(ToolDescriptor tool);
    
}
