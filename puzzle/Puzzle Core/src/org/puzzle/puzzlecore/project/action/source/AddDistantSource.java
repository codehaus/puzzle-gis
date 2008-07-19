/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.action.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.context.gui.datadialog.JDistantSourcePane;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;

public final class AddDistantSource extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        final GISProject gis = activatedNodes[0].getLookup().lookup(GISProject.class);
        final JDistantSourcePane pane = new JDistantSourcePane();
        ActionListener lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equalsIgnoreCase("ok")) {

                    Collection<GISSource> sources = pane.getGISSources();
                    for (GISSource source : sources) {
                        gis.appendGISSource(source);
                    }
                }
            }
        };

        DialogDescriptor desc = new DialogDescriptor(pane, "Open file", true, lst);
        DialogDisplayer.getDefault().notify(desc);
    }

    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    public String getName() {
        return NbBundle.getMessage(AddDistantSource.class, "CTL_AddDistantSource");
    }

    protected Class[] cookieClasses() {
        return new Class[]{GISProject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/addDistantSource.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

