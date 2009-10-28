/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.puzzle.core.resources.MessageBundle;

/**
 *
 * @author eclesia
 */
public final class ActionUtils {

    private ActionUtils(){}

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
            final JLabel lbl = new JLabel(MessageBundle.getString("chooseProject"));
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
