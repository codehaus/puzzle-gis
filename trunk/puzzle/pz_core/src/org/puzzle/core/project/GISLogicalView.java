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
package org.puzzle.core.project;

import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.puzzle.core.project.nodes.GISProjectNode;

/**
 * This class provides a tree for displaying the {@link GISProject}.
 * This tree is used by the "Projects" window, included by default in
 * the NetBeans GUI. Using this class, extending {@code LogicalViewProvider}
 * is a great improvment in term of time, because we can reuse a lot of
 * possibilities offered by NetBeans to manage projects.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.netbeans.spi.project.ui.LogicalViewProvider
 */
public class GISLogicalView implements LogicalViewProvider {

    private final GISProject project;

    /**
     * Constructor.
     * @param project The {@code GISProject} to present.
     */
    public GISLogicalView(GISProject project) {
        this.project = project;
    }

    /**
     * This method creates the tree.
     * @return  A {@code org.openide.nodes.Node} representing the project. This
     *          node is the root of the tree.
     * 
     * @see org.netbeans.spi.project.ui.LogicalViewProvider#createLogicalView() 
     */
    @Override
    public Node createLogicalView() {

        DataObject obj = null;
        try {
            obj = DataObject.find(project.getProjectDirectory());
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (obj != null) {
            Node root = obj.getNodeDelegate();
            return new GISProjectNode(root, project);
        } else {
            return Node.EMPTY;
        }

    }

    /**
     * @see org.netbeans.spi.project.ui.LogicalViewProvider#findPath(Node,Object)
     */
    @Override
    public Node findPath(Node root, Object target) {
        System.out.println("Called");
        return null;
    }
}
