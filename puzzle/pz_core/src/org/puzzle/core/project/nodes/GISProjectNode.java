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
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

import org.puzzle.core.project.GISProject;

/**
 * This class provides a {@link org.openide.nodes.Node} for the
 * {@link org.puzzle.core.project.GISProject}.
 * <br>
 * This is the root node used in the the "Projects" window provided
 * by NetBeans.
 *
 * @author Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 *
 * @see     org.openide.nodes.FilterNode
 */
public class GISProjectNode extends FilterNode{

    private final String ICON_PATH = "org/puzzle/core/project/boussole.png";
    private final ImageIcon ICON = new ImageIcon(Utilities.loadImage(ICON_PATH, true));

    private final GISProject project;

    /**
     * Constructor.<br>
     * This constructor only initialize a new array to contain project node
     * childrens ("src", "doc", "map").
     *
     * @param   project The current {@code GISProject}.
     */
    public  GISProjectNode(Node node, GISProject project){
        super(node, new GISProjectNodeFilter(node,project),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup (new Lookup[] { Lookups.singleton(project),
                    node.getLookup() }));
        this.project = project;
    }

    @Override
    public Image getIcon(int arg0) {
        return ICON.getImage();
    }

    @Override
    public Image getOpenedIcon(int arg0) {
        return ICON.getImage();
    }

    @Override
    public String getDisplayName() {
        ProjectInformation info = project.getLookup().lookup(ProjectInformation.class);
        return info.getDisplayName();
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
            CommonProjectActions.closeProjectAction(),
            CommonProjectActions.setAsMainProjectAction()
        };
    }

}