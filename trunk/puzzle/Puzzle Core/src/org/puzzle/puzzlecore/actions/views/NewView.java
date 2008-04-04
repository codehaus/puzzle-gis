/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
