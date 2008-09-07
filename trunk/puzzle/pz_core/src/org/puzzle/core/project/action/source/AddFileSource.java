/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.core.project.action.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.context.gui.datadialog.JFileSourcePane;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;

public final class AddFileSource extends CookieAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        final GISProject gis = activatedNodes[0].getLookup().lookup(GISProject.class);

        final JFileSourcePane pane = new JFileSourcePane();
        ActionListener lst = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equalsIgnoreCase("ok")) {
                    pane.setVisible(false);
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

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AddFileSource.class, "CTL_AddFileSource");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{GISProject.class};
    }
    
    @Override
    protected String iconResource() {
        return "org/puzzle/core/addFileSource.png";
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

