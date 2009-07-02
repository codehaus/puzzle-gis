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
package org.puzzle.core.actions;

import java.util.Collection;

import javax.swing.SwingUtilities;
import org.geotoolkit.map.MapContext;

import org.netbeans.api.project.FileOwnerQuery;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;

import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.capabilities.JLayerChooserWizard;
import org.puzzle.core.project.source.capabilities.LayerCreation;

/**
 *  Action to show a layer creation wizard, this wizard will query the source
 *  and add a maplayer is one of the project map context.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ShowLayerWizard extends NodeAction {

    /**
     * {@inheritDoc }
     */
    @Override
    protected void performAction(final Node[] activatedNodes) {
        final DataObject dataObject = activatedNodes[0].getLookup().lookup(DataObject.class);
        final GISProject prj = (GISProject) FileOwnerQuery.getOwner(dataObject.getPrimaryFile());

        new Thread(){

            public void run(){
                final LayerCreation creation = activatedNodes[0].getLookup().lookup(LayerCreation.class);
                final Collection<MapContext> contexts = prj.getContexts();

                if(contexts.size() > 0 && prj != null){
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JLayerChooserWizard.showChooserDialog(contexts, creation);
                        }
                    });

                }else{
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            final DialogDescriptor desc = new DialogDescriptor(Utilities.getString("projectHasNoMap"), Utilities.getString("warning"), true, null);
                            DialogDisplayer.getDefault().notify(desc);
                        }
                    });
                }
            }

        }.start();
        
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName() {
        return Utilities.getString("showLayerWizard");
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

        if(lk.lookup(LayerCreation.class) == null) return false;

        DataObject obj = lk.lookup(DataObject.class);
        return ( obj != null && FileOwnerQuery.getOwner(obj.getPrimaryFile()) instanceof GISProject) ;
    }


}
