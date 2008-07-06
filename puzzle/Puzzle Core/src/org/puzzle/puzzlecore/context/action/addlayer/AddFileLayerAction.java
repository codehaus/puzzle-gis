/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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
package org.puzzle.puzzlecore.context.action.addlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.context.gui.datadialog.JFileSourcePane;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 * @author johann sorel
 */
public final class AddFileLayerAction extends CallableSystemAction {

    public void performAction() {

        final Project mainProject = OpenProjects.getDefault().getMainProject();
        
        
      
        
        if(mainProject != null && mainProject instanceof GISProject) {
            final GISProject gis = (GISProject) mainProject;
            
            final JFileSourcePane pane = new JFileSourcePane();
            ActionListener lst = new ActionListener() {

              public void actionPerformed(ActionEvent e) {
                  
                  if(e.getActionCommand().equalsIgnoreCase("ok")){
                      
                    Collection<GISSource> sources = pane.getGISSources();
                    for(GISSource source : sources){
                        gis.appendGISSource(source);  
                    }
                  }
              }
          };
      
        DialogDescriptor desc = new DialogDescriptor(pane,"Open file",true,lst);
            
            
            
            DialogDisplayer.getDefault().notify(desc);
            
        } else {
            NotifyDescriptor notify = new NotifyDescriptor.Message("Current main project is not a GIS project.",
                    NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(notify);
        }
  }
  
  public String getName() {
        return NbBundle.getMessage(AddFileLayerAction.class, "CTL_AddLayerAction");
    }

  @Override
  protected String iconResource() {
    return "org/puzzle/puzzlecore/context/action/addlayer/file_add.png";
  }

  public HelpCtx getHelpCtx() {
    return HelpCtx.DEFAULT_HELP;
  }

  @Override
  protected boolean asynchronous() {
    return false;
  }
}
