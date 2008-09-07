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

package org.puzzle.core.context.action.newcontext;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

import org.puzzle.core.context.ContextService;

/**
 * @author johann sorel
 */
public final class NewContextAction extends CallableSystemAction {

    int inc = 0;
    
    @Override
    public void performAction() {
        MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        context.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription("Context " + ++inc, ""));
        
        ContextService service = Lookup.getDefault().lookup(ContextService.class);
        service.addContext(context);
        service.setActiveContext(context);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewContextAction.class, "CTL_NewContextAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/core/context/action/newcontext/newcontext.png";
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
