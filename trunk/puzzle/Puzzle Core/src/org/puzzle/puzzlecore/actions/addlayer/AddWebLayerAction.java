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
import org.geotools.gui.swing.datachooser.JWFSDataPanel;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.struct.CORE;

public final class AddWebLayerAction extends CallableSystemAction {

    public void performAction() {
    
        if (CORE.getInstance().getActiveContext() != null) {
            List<DataPanel> lst = new ArrayList<DataPanel>();
            lst.add(new JWFSDataPanel());
            
            JDataChooser jdc = new JDataChooser(null, lst);

            JDataChooser.ACTION ret = jdc.showDialog();

            if (ret == JDataChooser.ACTION.APPROVE) {
                MapLayer[] layers = jdc.getLayers();

                MapContext context = CORE.getInstance().getActiveContext();
                for (MapLayer layer : layers) {
                    context.addLayer(layer);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "No active Context");
        }
        
    }

    public String getName() {
        return NbBundle.getMessage(AddWebLayerAction.class, "CTL_AddWebLayerAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/actions/addlayer/web_add.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
