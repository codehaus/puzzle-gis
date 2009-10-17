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
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

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
import org.puzzle.core.resources.MessageBundle;

/**
 * Action to create a new map.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class NewMapContext extends AbstractAction {

    private final GISProject project;
    
    public NewMapContext(){
        this(null);
    }
    
    public NewMapContext(final GISProject project){
        super(MessageBundle.getString("newMap"));
        this.project = project;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final Project candidate;
        if(project == null){
            candidate = OpenProjects.getDefault().getMainProject();
        }else{
            candidate = project;
        }
        
        if(candidate != null && candidate instanceof GISProject) {
            final GISProject gis = (GISProject) candidate;
            TemplateWizard tw = new TemplateWizard();
            final Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
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
            NotifyDescriptor d =  new NotifyDescriptor.Message(
                    MessageBundle.getString("projectIsNotGIS"),
                    NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
        
    }
    
}
