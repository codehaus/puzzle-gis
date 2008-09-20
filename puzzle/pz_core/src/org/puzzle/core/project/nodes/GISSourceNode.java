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
package org.puzzle.core.project.nodes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

import org.puzzle.core.context.gui.datadialog.JFileSourcePane;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;

/**
 * This class represents the folder "src" defined in the
 * {@link org.puzzle.core.project.GISProject}, which is intended to
 * contain all sources describing the datas of a project.
 *
 * @author Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 *
 * @see     org.openide.nodes.FilterNode
 */
public class GISSourceNode extends FilterNode {

    private final String ICON_PATH = "org/puzzle/core/project/source.png";
    private final Image ICON = Utilities.loadImage(ICON_PATH, true);

    /**
     * Constructor.
     * @param  node      The "src" folder.
     */
    public GISSourceNode(Node node) {
        super(node);
    }

    @Override
    public Image getIcon(int type) {
        return ICON;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public String getDisplayName() {
        return "Sources";
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
                    new NewFileSourceAction()
                };
    }

    private class NewFileSourceAction extends AbstractAction {

        NewFileSourceAction() {
            super("New file source", new ImageIcon(Utilities.loadImage("/org/puzzle/core/addFileSource.png")));
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {

            final JFileSourcePane pane = new JFileSourcePane();
            ActionListener lst = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    DataObject obj = getLookup().lookup(DataObject.class);
                    Project proj = FileOwnerQuery.getOwner(obj.getPrimaryFile());

                    if (proj instanceof GISProject) {
                        GISProject project = (GISProject) proj;

                        if (e.getActionCommand().equalsIgnoreCase("ok")) {
                            pane.setVisible(false);
                        }

                        Collection<GISSource> sources = pane.getGISSources(project);
                        for (GISSource source : sources) {
                            project.appendGISSource(source);
                        }
                    } else {
                        NotifyDescriptor d = new NotifyDescriptor.Message("Project is not a GIS project", NotifyDescriptor.INFORMATION_MESSAGE);
                        DialogDisplayer.getDefault().notify(d);
                    }



                }
            };

            DialogDescriptor desc = new DialogDescriptor(pane, "Open file", true, lst);
            DialogDisplayer.getDefault().notify(desc);
        }
    }
}