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
package org.puzzle.puzzlecore.context.action.addlayer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JPostGISDataPanel;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.context.ContextService;

public final class AddDBLayerAction extends CallableSystemAction {

    @Override
    public void performAction() {
    
        ContextService service = Lookup.getDefault().lookup(ContextService.class);
        MapContext context = service.getActiveContext();
        
        if (context != null) {
            List<DataPanel> lst = new ArrayList<DataPanel>();
            lst.add(new JPostGISDataPanel());
            
            JDataChooser jdc = new JDataChooser(null, lst);

            JDataChooser.ACTION ret = jdc.showDialog();

            if (ret == JDataChooser.ACTION.APPROVE) {
                MapLayer[] layers = jdc.getLayers();

                for (MapLayer layer : layers) {
                    context.layers().add(layer);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "No active Context");
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AddDBLayerAction.class, "CTL_AddDBLayerAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/context/action/addlayer/db_add.png";
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
