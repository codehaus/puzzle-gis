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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;

import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * action to create new file sources.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class NewFileSource extends AbstractAction {

    private final GISProject project;

    public NewFileSource(){
        this(null);
    }

    public NewFileSource(final GISProject project){
        super(Utilities.getString("newFileSource"));
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
            final GISProject gis =(GISProject) candidate;
            final JFileSourcePane pane = new JFileSourcePane();
            final ActionListener lst = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equalsIgnoreCase("Finish")) {
                        pane.setVisible(false);
                        final Map<String,GISSourceInfo> sources = pane.getSources();
                        final Set<String> names = sources.keySet();
                        for (final String name : names) {
                            gis.registerSource(name,sources.get(name));
                        }
                    }
                }
            };

            final WizardDescriptor wdesc = WizardUtilities.createSimplewWizard(pane, Utilities.getString("openFile"));
            wdesc.setButtonListener(lst);
            DialogDisplayer.getDefault().notify(wdesc);
        }else{
            final NotifyDescriptor d =  new NotifyDescriptor.Message(Utilities.getString("projectIsNotGIS"), NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
        
    }

}
