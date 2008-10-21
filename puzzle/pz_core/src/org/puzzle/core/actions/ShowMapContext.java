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
package org.puzzle.core.actions;

import org.geotools.map.MapContext;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.filetype.GISContextDataObject;
import org.puzzle.core.view.RendererChooser;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.ViewService;

/**
 * An action allowing the user to display a context in a map.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.util.actions.CookieAction
 */
public final class ShowMapContext extends CookieAction {

    /**
     * Method called when this action is clicked.<br>
     * This method displays the first activated context node in a map.
     * @param   activatedNodes  The currently activated nodes.
     */
    @Override
    protected void performAction(Node[] activatedNodes) {
        if(activatedNodes.length == 0 ) return ;
        
        final GISContextDataObject dataObject = activatedNodes[0].getLookup().lookup(GISContextDataObject.class);
    
        if(dataObject == null) return;
        
        final MapContext context = dataObject.getContext();
        
        if(context != null){
            final MapView view = RendererChooser.showChooserDialog(context);
            if(view != null){
                final ViewService viewService = Lookup.getDefault().lookup(ViewService.class);
                viewService.add(view);
        }
    }
    }

    /**
     * Specify the enable mode of the action.<br>
     * This one is enable when only one node is selected.
     * @return  An {@code int} containing the mode.
     */
    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    /**
     * Get the name of the action.
     * @return  A {@code String} containing the Action's name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(ShowMapContext.class, "CTL_ShowMapContext");
    }

    /**
     * Defines the cookies needed for the action to be enable. The activated
     * node must contain a {@code GISContextDataObject} to be enabled.
     * @return  A set of {@code Class}es.
     */
    @Override
    protected Class[] cookieClasses() {
        return new Class[]{GISContextDataObject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/core/actions/showMap.png";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

