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
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.puzzle.core.actions.JDistantSourcePane;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSourceInfo;

public final class NewDistantSource extends AbstractAction {

    private final GISProject project;

    public NewDistantSource(){
        this(null);
    }

    public NewDistantSource(final GISProject project){
        super("New distant source");
        this.project = project;
    }
    
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
            final JDistantSourcePane pane = new JDistantSourcePane();
            final ActionListener lst = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equalsIgnoreCase("ok")) {
                        final Map<String,GISSourceInfo> sources = pane.getSources();
                        final Set<String> names = sources.keySet();
                        for (final String name : names) {
                            gis.registerSource(name,sources.get(name));
                        }
                    }
                }
            };

            final DialogDescriptor desc = new DialogDescriptor(pane, "Open file", true, lst);
            DialogDisplayer.getDefault().notify(desc);
        }else{
            final NotifyDescriptor d =  new NotifyDescriptor.Message("Main project is not a GIS project", NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
    }
}

