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

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.resources.MessageBundle;
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

        new Thread() {

            @Override
            public void run() {

                final ProgressHandle handle = ProgressHandleFactory.createHandle(MessageBundle.getString("openView") + " : " + view.getTitle());
                handle.start(100);
                handle.setInitialDelay(1);
                handle.switchToIndeterminate();

                final ViewComponent comp = view.getComponent(true);

                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            if (!comp.isOpened()) {
                                comp.open();
                            }
                            comp.requestActive();
                            comp.requestVisible();
                        }
                    });
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }

                

                handle.finish();
            }
        }.start();
    }

    @Override
    public String getName() {
        return MessageBundle.getString("showView");
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
