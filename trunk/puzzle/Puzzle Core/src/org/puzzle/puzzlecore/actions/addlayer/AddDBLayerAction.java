/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.actions.addlayer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JOracleDataPanel;
import org.geotools.gui.swing.datachooser.JPostGISDataPanel;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.struct.Application;

public final class AddDBLayerAction extends CallableSystemAction {

    public void performAction() {
    
        if (Application.getInstance().getActiveContext() != null) {
            List<DataPanel> lst = new ArrayList<DataPanel>();
            lst.add(new JPostGISDataPanel());
            lst.add(new JOracleDataPanel());
            
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
        return NbBundle.getMessage(AddDBLayerAction.class, "CTL_AddDBLayerAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/actions/addlayer/db_add.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
