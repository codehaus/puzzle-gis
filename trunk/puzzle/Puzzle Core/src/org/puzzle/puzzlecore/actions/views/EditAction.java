/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

import org.geotools.gui.swing.map.map2d.Map2D;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.struct.CORE;
import org.puzzle.puzzlecore.struct.ViewEvent;
import org.puzzle.puzzlecore.struct.ViewListener;
import org.puzzle.puzzlecore.struct.ViewManager;

/**
 * 
 * @author johann sorel
 */
public final class EditAction extends CallableSystemAction implements ViewListener{

   private org.geotools.gui.swing.map.map2d.control.EditAction action = new org.geotools.gui.swing.map.map2d.control.EditAction();

    public EditAction() {
        ViewManager manager = CORE.getViewManager();
        manager.addViewListener(this);
        
        if(manager.getActiveView() != null){
            action.setMap(manager.getActiveView().getMap());
        }
    }

    public Map2D getMap() {
        return action.getMap();
    }

    public void setMap(Map2D map) {
        action.setMap(map);
    }

    public void performAction() {
        action.actionPerformed(null);
    }

    public String getName() {
        return NbBundle.getMessage(EditAction.class, "CTL_EditMapAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/actions/views/edit.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public void viewAdded(ViewEvent event) {
    }

    public void viewRemoved(ViewEvent event) {
    }

    public void viewActivated(ViewEvent event) {
        if(event.getView() != null){
            action.setMap(event.getView().getMap());
        }else{
            action.setMap(null);
        }
        
    }
}
