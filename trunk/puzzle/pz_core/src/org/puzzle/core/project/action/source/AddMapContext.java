/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.core.project.action.source;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.GISProject;

public final class AddMapContext extends CookieAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
//        final GISProject gis = (GISProject)OpenProjects.getDefault().getMainProject();
        final GISProject gis = activatedNodes[0].getLookup().lookup(GISProject.class);
        
        TemplateWizard tw = new TemplateWizard();
        //System.out.println("Template folder = " + tw.getTemplatesFolder().getPrimaryFile().getPath());
        Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
        DataObject temp = null;
        while(enu.hasMoreElements()){
            temp = enu.nextElement();
            if(temp.getName().equals("Other")) break;
        }
        
        //System.out.println("templates Other folder = " + temp.getPrimaryFile().getPath());
        
        FileObject[] templates = temp.getPrimaryFile().getChildren();
        for(FileObject fo : templates){
            
            if(fo.getName().equals("GISContextTemplate")){
                
                try{
                    tw.instantiate(DataObject.find(fo), DataFolder.findFolder(gis.getMapFolder(true)));
                }catch(DataObjectNotFoundException donfe){
                    Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE,
                        "Unable to find object "+ fo.getPath(), donfe);
                }catch(IOException ioe){
                    Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to find object "+ fo.getPath(), ioe);
                }
                break;
            }
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(AddMapContext.class, "CTL_AddMapContext");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{GISProject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/core/addMapContext.png";
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

