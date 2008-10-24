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
package org.puzzle.core.windows.toolbox;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows ToolTree component.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ToolTreeAction extends AbstractAction {

    public ToolTreeAction() {
        super(Utilities.getString("toolTree"));
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage("org/puzzle/core/windows/toolbox/tool.png", true)));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final TopComponent win = ToolTreeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
