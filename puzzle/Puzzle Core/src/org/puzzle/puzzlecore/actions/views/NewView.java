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
package org.puzzle.puzzlecore.actions.views;

import org.geotools.gui.swing.map.map2d.JDefaultEditableMap2D;
import org.geotools.map.MapContext;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.struct.CORE;
import org.puzzle.puzzlecore.struct.MapView;

public final class NewView extends CallableSystemAction {

    public void performAction() {
        MapView view = new MapView(new JDefaultEditableMap2D());
        view.setName("2D view");

        MapContext context = CORE.getContextManager().getActiveContext();
        view.getMap().getRenderingStrategy().setContext(context);

        CORE.getViewManager().addView(view);

        view.open();
        view.requestActive();
    }

    public String getName() {
        return NbBundle.getMessage(NewView.class, "CTL_NewView");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/actions/views/view_add.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}