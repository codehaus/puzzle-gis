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
package org.puzzle.print.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.util.NbBundle;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.ViewService;

/**
 * Save view action.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ExportImage implements ActionListener {

    @Override
    public void actionPerformed(final ActionEvent e) {
        
        final Set<MapView> views = ViewService.getViews();

        if(views.isEmpty()){
            final NotifyDescriptor d =  new NotifyDescriptor.Message(
                    NbBundle.getMessage(ExportImage.class, "noView"), NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }else{
            SaveWizard.showChooserDialog(views);
        }
            
    }
}