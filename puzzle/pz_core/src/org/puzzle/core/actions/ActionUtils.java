/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.resources.CoreResource;

/**
 *
 * @author Johann Sorel
 */
public final class ActionUtils {

    private ActionUtils(){}

    /**
     * Check all present projects and potentialy display a wizard to the user
     * to make him choose a GIS project.
     * @return GISProject
     */
    public static GISProject forceChoosingProject(){
        GISProject candidate = null;
        final Project[] projects = OpenProjects.getDefault().getOpenProjects();
        final List<GISProject> valids = new ArrayList<GISProject>();

        for(final Project p : projects){
            if(p instanceof GISProject){
                valids.add((GISProject) p);
            }
        }

        if(valids.size() == 1){
            candidate = valids.get(0);
            OpenProjects.getDefault().setMainProject(candidate);
        }else if(valids.size() > 1){
            final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            final JLabel lbl = new JLabel(CoreResource.getString("chooseProject"));
            final JComboBox box = new JComboBox(valids.toArray());
            box.setSelectedIndex(0);
            panel.add(lbl);
            panel.add(box);

            final NotifyDescriptor desc = new DialogDescriptor(panel,"");
            DialogDisplayer.getDefault().notify(desc);
            candidate = (GISProject)(Project) box.getSelectedItem();
            OpenProjects.getDefault().setMainProject(candidate);
        }

        return candidate;
    }
}
