/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.core.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;
import org.puzzle.core.project.filetype.GISSourceDataObject;

/**
 *
 * @author eclesia
 */
public class LoadSource extends NodeAction{

    /**
     * {@inheritDoc }
     */
    @Override
    protected void performAction(final Node[] activatedNodes) {
        final GISSourceDataObject dataObject = activatedNodes[0].getLookup().lookup(GISSourceDataObject.class);

        new Thread(){
            @Override
            public void run(){
                dataObject.getSource().load();
            }
        }.start();

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName() {
        return Utilities.getString("loadsource");
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
