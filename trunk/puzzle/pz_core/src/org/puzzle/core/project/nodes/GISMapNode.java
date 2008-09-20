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
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

import org.puzzle.core.project.GISProject;

/**
 * This class provides a {@link org.openide.nodes.Node} for the "map" folder
 * defined in the {@link org.puzzle.core.project.GISProject}.
 * This folder is intended to contain all files describing
 * {@code MapContext}s for each project.
 *
 * @author Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 *
 * @see     org.openide.nodes.FilterNode
 */
public class GISMapNode extends FilterNode {

    private final String ICON_PATH = "org/puzzle/core/project/map.png";
    private final Image ICON = Utilities.loadImage(ICON_PATH, true);

    /**
     * Constructor.
     * @param Node, map folder
     */
    public GISMapNode(Node node) {
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
        return "Maps";
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
                    new NewMapContextAction()
                };
    }

    private class NewMapContextAction extends AbstractAction {

        NewMapContextAction() {
            super("New map", new ImageIcon(Utilities.loadImage("/org/puzzle/core/addMapContext.png")));
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            DataObject obj = getLookup().lookup(DataObject.class);
            Project project = FileOwnerQuery.getOwner(obj.getPrimaryFile());

            if (project instanceof GISProject) {
                final GISProject gis = (GISProject) project;
                TemplateWizard tw = new TemplateWizard();
                Enumeration<DataObject> enu = tw.getTemplatesFolder().children();
                DataObject temp = null;
                while (enu.hasMoreElements()) {
                    temp = enu.nextElement();
                    if (temp.getName().equals("Other")) {
                        break;
                    }
                }

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
                NotifyDescriptor d = new NotifyDescriptor.Message("Project is not a GIS project", NotifyDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(d);
            }
        }
    }
}