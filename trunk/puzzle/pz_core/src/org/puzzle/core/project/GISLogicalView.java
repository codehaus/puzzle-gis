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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.puzzle.core.project.nodes.GISDocNode;
import org.puzzle.core.project.nodes.GISMapNode;
import org.puzzle.core.project.nodes.GISProjectNode;
import org.puzzle.core.project.nodes.GISSourceNode;

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
public class GISLogicalView implements LogicalViewProvider{

    private final GISProject project;
    
    /**
     * Constructor.
     * @param project The {@code GISProject} to present.
     */
    public GISLogicalView(GISProject project){
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
        
        GISProjectNode root = new GISProjectNode(project);
        
        Node mapNode = null;
        Node docNode = null;
        Node srcNode = null;

        //we must load the sources first
        try {
            FileObject sources = project.getSourceFolder(true);
            DataFolder sourceDataObject = DataFolder.findFolder (sources);
            srcNode = new GISSourceNode (sourceDataObject, project);
        } catch (DataObjectNotFoundException donfe) {
            Logger.getLogger(GISLogicalView.class.getName()).log(
                    Level.WARNING,"Unable to find sources folder",donfe );
            srcNode = new AbstractNode (Children.LEAF);
        }

        //when we have the sources we can load the maps
        try {
            FileObject maps = project.getMapFolder(true);
            DataFolder mapDataObject = DataFolder.findFolder (maps);
            mapNode = new GISMapNode (mapDataObject, project);
        } catch (DataObjectNotFoundException donfe) {
            Logger.getLogger(GISLogicalView.class.getName()).log(
                    Level.WARNING,"Unable to find maps folder",donfe );
            mapNode = new AbstractNode (Children.LEAF);
        }

        //and finaly the documents that might need the maps
        try {
            FileObject docs = project.getDocFolder(true);
            DataFolder docDataObject = DataFolder.findFolder (docs);
            Node docsNode = docDataObject.getNodeDelegate();
            docNode = new GISDocNode (docsNode, project);
        } catch (DataObjectNotFoundException donfe) {
            Logger.getLogger(GISLogicalView.class.getName()).log(
                    Level.WARNING,"Unable to find docs folder",donfe );
            docNode = new AbstractNode (Children.LEAF);
        }
        
        
        
        root.getChildren().add(new Node[]{mapNode,docNode,srcNode});
        return root;
    }

    /**
     * @see org.netbeans.spi.project.ui.LogicalViewProvider#findPath(Node,Object)
     */
    @Override
    public Node findPath(Node root, Object target) {
        return null;
    }

}
