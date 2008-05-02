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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif;

import java.awt.Component;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.ToolTreeConstants;
import org.puzzle.puzzlecore.tool.AbstractToolDescriptor;


/**
 *
 * @author Laurent Jegou
 */
public class SVG2MIFTTDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE_CONVERT.getPath();    
    String title = NbBundle.getMessage(SVG2MIFTTDescriptor.class,"title");
    
    public String getTitle(){
        return "SVG > MIF";
    }

    @Override
    public String[] getPath() {
        return path;
    }

    public Component getComponent() {
        return new SVG2MIFTool().getComponent();
    }

}
