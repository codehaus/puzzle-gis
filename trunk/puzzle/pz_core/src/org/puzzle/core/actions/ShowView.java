/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
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

    @Override
    protected boolean asynchronous() {
        return false;
    }

}
