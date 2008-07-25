/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.action.source;

import java.util.Enumeration;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.project.GISProject;

public final class AddMapContext extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        final GISProject gis = activatedNodes[0].getLookup().lookup(GISProject.class);
        TemplateWizard tw = new TemplateWizard();
        System.out.println("Template folder = " + tw.getTemplatesFolder().getPrimaryFile().getPath());
        Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
        while(enu.hasMoreElements())
        {
            DataObject temp = enu.nextElement();
            System.out.println("template = " + temp.getName());
        }
        //System.out.println("Template = " + tw.getTemplate().getName());
        tw.templateChooser();
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

