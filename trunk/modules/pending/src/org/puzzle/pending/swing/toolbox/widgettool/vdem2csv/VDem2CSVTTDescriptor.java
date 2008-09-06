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

package org.puzzle.pending.swing.toolbox.widgettool.vdem2csv;

import java.awt.Component;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.ToolTreeConstants;
import org.puzzle.puzzlecore.tool.AbstractToolDescriptor;


/**
 *
 * @author johann Sorel
 */
public class VDem2CSVTTDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE_CONVERT.getPath();
    String title = NbBundle.getMessage(VDem2CSVTTDescriptor.class, "title");
    
    
    public String getTitle(){
        return "VDem > CSV";
    }

    @Override
    public String[] getPath() {
        return path;
    }

    public Component getComponent() {
        return new VDem2CSVTool();
    }



}
