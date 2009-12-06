/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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
package org.puzzle.analyze.processing;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;

import org.openide.windows.TopComponent;
import org.puzzle.analyze.resources.AnalyzeResource;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ShowSequence extends CookieAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        final SequenceDataObject dataObject = activatedNodes[0].getLookup().lookup(SequenceDataObject.class);


        new Thread(){
            @Override
            public void run(){

                final ProgressHandle handle = ProgressHandleFactory.createHandle( AnalyzeResource.getString("loadingSequence"));
                handle.start(100);
                handle.setInitialDelay(1);
                handle.switchToIndeterminate();

                try{
                    final TopComponent comp = new TopComponent();
                    comp.setLayout(new BorderLayout());
                    comp.add(BorderLayout.CENTER,new JSequencePane(dataObject));

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            if (!comp.isOpened()) {
                                comp.open();
                            }
                            comp.setDisplayName(dataObject.getName());
                            comp.requestActive();
                            comp.requestVisible();
                        }
                    });
                }finally{
                    handle.finish();
                }

            }
        }.start();


    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return AnalyzeResource.getString("CTL_ShowSequence");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{SequenceDataObject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/analyze/resources/engrenage.png";
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

