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
package org.puzzle.core.windows.toolbox;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;
import org.puzzle.core.resources.CoreResource;

/**
 * Action which shows ToolTree component.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ToolTreeAction extends AbstractAction {

    public ToolTreeAction() {
        super(CoreResource.getString("toolTree"));
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage("org/puzzle/core/resources/tool.png", true)));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final TopComponent win = ToolTreeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
