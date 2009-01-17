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

package org.puzzle.analyze.tool.convert.vector;

import java.awt.Component;

import org.openide.util.NbBundle;

import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;
import org.puzzle.core.tool.AbstractToolDescriptor;


/**
 * Widget Tool descriptor for vector format conversion, this is used in the ToolBox tree.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ConvertVectorDescriptor extends AbstractToolDescriptor{
    
    private final String[] path = ToolTreeConstants.getInstance().CONVERT.getPath();
    
    private String title = NbBundle.getMessage(ConvertVectorDescriptor.class, "vector_convert");
       
    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getPath() {
        return path;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getComponent() {
        return new ConvertVectorTool();
    }

}
