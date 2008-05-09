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
package org.puzzle.sldeditor;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.puzzle.puzzlecore.tool.ToolDescriptor;
import org.puzzle.puzzlecore.tool.ToolService;
import org.puzzle.sldeditor.gui.SLDEditorDescriptor;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    private final Collection<ToolDescriptor> DEFAULT_TOOLS = new ArrayList<ToolDescriptor>();

    @Override
    public void restored() {

        DEFAULT_TOOLS.add(new SLDEditorDescriptor());

        //grab the tool service
        ToolService service = Lookup.getDefault().lookup(ToolService.class);

        for (ToolDescriptor tool : DEFAULT_TOOLS) {
            service.addTool(tool);
        }

    }
}
