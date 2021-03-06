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
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;

import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.resources.CoreResource;

/**
 * Action to create new file sources.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class NewFileSource extends AbstractAction {

    private final GISProject project;

    public NewFileSource(){
        this(null);
    }

    public NewFileSource(final GISProject project){
        super(CoreResource.getString("newFileSource"));
        this.project = project;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Project candidate = null;
        if(project == null){
            candidate = OpenProjects.getDefault().getMainProject();

            if(candidate == null){
                candidate = ActionUtils.forceChoosingProject();
            }

        }else{
            candidate = project;
        }
        
        if(candidate != null && candidate instanceof GISProject) {
            final GISProject currentProject =(GISProject) candidate;
            final JFileSourcePane pane = new JFileSourcePane();
            final ActionListener lst = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equalsIgnoreCase("Finish")) {
                        pane.setVisible(false);

                        new Thread(){

                            public void run(){

                                final ProgressHandle handle = ProgressHandleFactory.createHandle(
                                CoreResource.getString("openingSources"));
                                handle.start(100);
                                handle.setInitialDelay(1);
                                handle.switchToIndeterminate();

                                try{
                                    final Map<String,GISSourceInfo> sources = pane.getSources();
                                    final Set<String> names = sources.keySet();
                                    for (final String name : names) {
                                        currentProject.registerSource(name,sources.get(name));
                                    }
                                }finally{
                                    handle.finish();
                                }
                            }
                        }.start();

                        
                    }
                }
            };

            final WizardDescriptor wdesc = WizardUtilities.createSimplewWizard(pane, CoreResource.getString("openFile"));
            wdesc.setButtonListener(lst);
            DialogDisplayer.getDefault().notify(wdesc);
        }else{
            final NotifyDescriptor d =  new NotifyDescriptor.Message(
                    CoreResource.getString("projectIsNotGIS"),
                    NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
        
    }

}
