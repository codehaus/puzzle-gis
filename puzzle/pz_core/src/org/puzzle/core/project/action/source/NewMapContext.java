/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.core.project.action.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.puzzle.core.project.GISProject;

public final class NewMapContext implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Project project = OpenProjects.getDefault().getMainProject();
        
        if(project != null && project instanceof GISProject) {
            final GISProject gis = (GISProject) project;
            TemplateWizard tw = new TemplateWizard();
            //System.out.println("Template folder = " + tw.getTemplatesFolder().getPrimaryFile().getPath());
            Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
            DataObject temp = null;
            while (enu.hasMoreElements()) {
                temp = enu.nextElement();
                if (temp.getName().equals("Other")) {
                    break;
                }
            }

            //System.out.println("templates Other folder = " + temp.getPrimaryFile().getPath());
            FileObject[] templates = temp.getPrimaryFile().getChildren();
            for (FileObject fo : templates) {
                if (fo.getName().equals("GISContextTemplate")) {
                    try {
                        tw.instantiate(DataObject.find(fo), DataFolder.findFolder(gis.getMapFolder(true)));
                    } catch (DataObjectNotFoundException donfe) {
                        Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE,
                                "Unable to find object " + fo.getPath(), donfe);
                    } catch (IOException ioe) {
                        Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE,
                                "Unable to find object " + fo.getPath(), ioe);
                    }
                    break;
                }
            }
        } else {
            NotifyDescriptor d =  new NotifyDescriptor.Message("Main project is not a GIS project", NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
        
    }
    
}
