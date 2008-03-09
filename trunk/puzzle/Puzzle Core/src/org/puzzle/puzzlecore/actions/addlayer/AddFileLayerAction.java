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
package org.puzzle.puzzlecore.actions.addlayer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JFileDataPanel;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.struct.Application;

/**
 * @author johann sorel
 */ 
public final class AddFileLayerAction extends CallableSystemAction {

    public void performAction() {
    
        if (Application.getInstance().getActiveContext() != null) {
            List<DataPanel> lst = new ArrayList<DataPanel>();
            lst.add(new JFileDataPanel());
            
            JDataChooser jdc = new JDataChooser(null, lst);

            JDataChooser.ACTION ret = jdc.showDialog();

            if (ret == JDataChooser.ACTION.APPROVE) {
                MapLayer[] layers = jdc.getLayers();

                MapContext context = Application.getInstance().getActiveContext();
                for (MapLayer layer : layers) {
                    context.addLayer(layer);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "No active Context");
        }
        
    }

    public String getName() {
        return NbBundle.getMessage(AddFileLayerAction.class, "CTL_AddLayerAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/actions/addlayer/file_add.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
