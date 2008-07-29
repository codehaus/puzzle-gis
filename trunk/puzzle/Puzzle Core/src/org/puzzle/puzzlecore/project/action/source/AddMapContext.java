/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.action.source;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.puzzle.puzzlecore.project.GISProject;

public final class AddMapContext extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        final GISProject gis = (GISProject)OpenProjects.getDefault().getMainProject();//activatedNodes[0].getLookup().lookup(GISProject.class);
        
        TemplateWizard tw = new TemplateWizard();
        //System.out.println("Template folder = " + tw.getTemplatesFolder().getPrimaryFile().getPath());
        Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
        DataObject temp = null;
        while(enu.hasMoreElements())
        {
            temp = enu.nextElement();
            if(temp.getName().equals("Other")) break;
        }
        
        //System.out.println("templates Other folder = " + temp.getPrimaryFile().getPath());
        
        FileObject[] templates = temp.getPrimaryFile().getChildren();
        for(FileObject fo : templates){
            //System.out.println("template name = " + fo.getName());
            if(fo.getName().equals("GISContextTemplate")){
                try{
                    //System.out.println("Starting wizard...");
                    //tw.setTemplate(DataObject.find(fo));
                    //tw.setTargetFolder(DataFolder.findFolder(gis.getMapFolder(true)));
                    tw.instantiate(DataObject.find(fo), DataFolder.findFolder(gis.getMapFolder(true)));
                    //System.out.println(tw.getTemplate().getPrimaryFile().getPath());
                    //System.out.println(tw.getTargetFolder().getPrimaryFile().getPath());
                    //System.out.println(TemplateWizard.getDescription(DataObject.find(fo)));
                    //DialogDisplayer.getDefault().createDialog(tw);
                }catch(DataObjectNotFoundException donfe){
                    Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to find object "+ fo.getPath(), donfe);
                }catch(IOException ioe){
                    Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to find object "+ fo.getPath(), ioe);
                }
            }
        }
    }

    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    public String getName() {
        return NbBundle.getMessage(AddMapContext.class, "CTL_AddMapContext");
    }

    protected Class[] cookieClasses() {
        return new Class[]{GISProject.class};
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzlecore/addMapContext.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

