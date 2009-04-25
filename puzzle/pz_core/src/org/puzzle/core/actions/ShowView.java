/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.view.ViewComponent;

/**
 *
 * @author eclesia
 */
public class ShowView extends CookieAction{

    public ShowView() {}

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{GISView.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        if(activatedNodes.length == 0 ) return ;

        final GISView view = activatedNodes[0].getLookup().lookup(GISView.class);

        if(view == null) return;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ViewComponent comp = view.getComponent(true);
                if(!comp.isOpened()){
                    comp.open();
                }
                comp.requestActive();
                comp.requestVisible();
            }
        });
    }

    @Override
    public String getName() {
        return Utilities.getString("showView");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

}
