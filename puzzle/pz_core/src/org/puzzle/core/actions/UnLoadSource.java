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

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;
import org.puzzle.core.project.filetype.GISSourceDataObject;
import org.puzzle.core.resources.MessageBundle;

/**
 *
 * @author eclesia
 */
public class UnLoadSource extends NodeAction{

    /**
     * {@inheritDoc }
     */
    @Override
    protected void performAction(final Node[] activatedNodes) {
        final GISSourceDataObject dataObject = activatedNodes[0].getLookup().lookup(GISSourceDataObject.class);

        new Thread(){
            @Override
            public void run(){
                dataObject.getSource().unload();
            }
        }.start();

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName() {
        return MessageBundle.getString("unloadsource");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if(activatedNodes == null || activatedNodes.length != 1) return false;

        Lookup lk = activatedNodes[0].getLookup();

        GISSourceDataObject obj = lk.lookup(GISSourceDataObject.class);
        return ( obj != null ) ;
    }

}
