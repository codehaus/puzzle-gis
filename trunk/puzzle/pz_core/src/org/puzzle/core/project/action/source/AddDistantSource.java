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
import org.puzzle.core.context.gui.datadialog.JDistantSourcePane;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;

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
        return "org/puzzle/core/addDistantSource.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

