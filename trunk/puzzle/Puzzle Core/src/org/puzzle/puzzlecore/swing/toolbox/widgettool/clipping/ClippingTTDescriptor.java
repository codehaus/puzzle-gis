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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.clipping;

import java.awt.Component;
import java.util.HashMap;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.ToolTreeConstants;
import org.puzzle.puzzlecore.tool.AbstractToolDescriptor;


/**
 *
 * @author johann sorel
 */
public class ClippingTTDescriptor extends AbstractToolDescriptor{

   
    private final String[] path = ToolTreeConstants.getInstance().ANALYSE_GEOMETRIE.getPath();
    
    private String title =  NbBundle.getMessage(ClippingTTDescriptor.class, "clip");
       
    
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getPath() {
        return path;
    }

    public Component getComponent() {
        return new ClippingTool(new HashMap());
    }


}
