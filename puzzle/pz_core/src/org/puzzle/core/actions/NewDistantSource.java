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
import org.puzzle.core.resources.MessageBundle;

/**
 * Action to create new distant sources.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class NewDistantSource extends AbstractAction {

    private final GISProject project;

    public NewDistantSource(){
        this(null);
    }

    public NewDistantSource(final GISProject project){
        super(MessageBundle.getString("newDistantSource"));
        this.project = project;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Project candidate;
        if(project == null){
            candidate = OpenProjects.getDefault().getMainProject();

            if(candidate == null){
                candidate = ActionUtils.forceChoosingProject();
            }

        }else{
            candidate = project;
        }
        
        if(candidate != null && candidate instanceof GISProject) {
            final GISProject gis =(GISProject) candidate;
            final JDistantSourcePane pane = new JDistantSourcePane();
            final ActionListener lst = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equalsIgnoreCase("Finish")) {

                        new Thread(){

                            @Override
                            public void run(){

                                final ProgressHandle handle = ProgressHandleFactory.createHandle(
                                MessageBundle.getString("openingSources"));
                                handle.start(100);
                                handle.setInitialDelay(1);
                                handle.switchToIndeterminate();
                                
                                try{
                                    final Map<String,GISSourceInfo> sources = pane.getSources();
                                    final Set<String> names = sources.keySet();
                                    for (final String name : names) {
                                        System.out.println("one register" + name);
                                        gis.registerSource(name,sources.get(name));
                                    }
                                }finally{
                                    handle.finish();
                                }
                            }
                        }.start();
                    }
                }
            };

            final WizardDescriptor wdesc = WizardUtilities.createSimplewWizard(pane, MessageBundle.getString("open"));
            wdesc.setButtonListener(lst);
            DialogDisplayer.getDefault().notify(wdesc);
        }else{
            final NotifyDescriptor d =  new NotifyDescriptor.Message(MessageBundle.getString("projectIsNotGIS"), NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
    }
}

