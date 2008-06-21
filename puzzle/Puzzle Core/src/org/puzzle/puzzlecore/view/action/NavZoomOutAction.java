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
package org.puzzle.puzzlecore.view.action;

import org.geotools.gui.swing.map.map2d.Map2D;
import org.geotools.gui.swing.map.map2d.stream.StreamingMap2D;
import org.geotools.gui.swing.map.map2d.stream.control.StreamZoomOut;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * 
 * @author johann sorel
 */
public final class NavZoomOutAction extends CallableSystemAction implements MapAction{

    private StreamZoomOut action = new StreamZoomOut();
    private ViewLookUpSync sync = new ViewLookUpSync(this);

    
    public NavZoomOutAction(){
    }
    
    public Map2D getMap() {
        return action.getMap();
    }

    public void setMap(Map2D map) {
        action.setMap((StreamingMap2D)map);
    }
    
    
    public void performAction() {
        action.actionPerformed(null);
    }

    public String getName() {
        return NbBundle.getMessage(NavZoomOutAction.class, "CTL_ZoomOutMapAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/view/resource/zoomout.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
