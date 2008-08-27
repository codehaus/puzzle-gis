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
package org.puzzle.puzzlecore.tool;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.SVG2MIFTTDescriptor;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.vdem2csv.VDem2CSVTTDescriptor;

/**
 *
 * @author Johann Sorel
 */
public class DefaultToolService implements ToolService {

    private final Collection<ToolDescriptor> DEFAULT_TOOLS = new ArrayList<ToolDescriptor>();
    private final InstanceContent CONTENT = new InstanceContent();
    private final Lookup LOOKUP = new AbstractLookup(CONTENT);

    public DefaultToolService() {
        DEFAULT_TOOLS.add(new VDem2CSVTTDescriptor());
        DEFAULT_TOOLS.add(new SVG2MIFTTDescriptor());

        for (ToolDescriptor tool : DEFAULT_TOOLS) {
            CONTENT.add(tool);
        }
    }

    public Lookup getLookup() {
        return LOOKUP;
    }

    public void addTool(ToolDescriptor tool) {
        CONTENT.add(tool);
    }

    public void removeTool(ToolDescriptor tool) {
        CONTENT.remove(tool);
    }
}
