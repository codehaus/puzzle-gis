/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.action.source;

import java.awt.Dialog;
import java.util.Collection;
import org.geotools.map.MapContext;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.filetype.GISSourceDataObject;
import org.puzzle.puzzlecore.project.source.GISSource;

public final class ShowLayerWizard extends CookieAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        GISSourceDataObject dataObject = (GISSourceDataObject) activatedNodes[0].getLookup().lookup(DataObject.class);
        GISSource source = dataObject.getSource();
        
        GISProject prj = (GISProject) FileOwnerQuery.getOwner(dataObject.getPrimaryFile());
        
        Collection<MapContext> contexts = prj.getContexts();
        
        if(contexts.size() > 0 && prj != null){
            source.showLayerWizard(contexts, prj);
        }else{
            DialogDescriptor desc = new DialogDescriptor("No maps in the project", "Warning", true, null);
            DialogDisplayer.getDefault().notify(desc);
        }
        
        
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ShowLayerWizard.class, "CTL_ShowLayerWizard");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{DataObject.class};
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
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

