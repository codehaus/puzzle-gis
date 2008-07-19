/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.action.source;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.project.GISProject;

public final class AddMapContext extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        final GISProject gis = activatedNodes[0].getLookup().lookup(GISProject.class);
        
    }

    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    public String getName() {
        return NbBundle.getMessage(AddMapContext.class, "CTL_AddMapContext");
    }

    protected Class[] cookieClasses() {
        return new Class[]{GISProject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/addMapContext.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

