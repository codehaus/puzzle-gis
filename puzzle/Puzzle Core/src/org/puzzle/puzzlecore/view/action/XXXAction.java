/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.view.action;

import org.openide.cookies.OpenCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.view.MapView;

public final class XXXAction extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        OpenCookie mapView = activatedNodes[0].getLookup().lookup(OpenCookie.class);
    // TODO use mapView
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(XXXAction.class, "CTL_XXXAction");
    }

    protected Class[] cookieClasses() {
        return new Class[]{OpenCookie.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/view/action/openofficeorg-20-draw.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

