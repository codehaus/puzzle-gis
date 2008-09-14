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

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

import org.puzzle.core.project.GISProject;

/**
 * Filter the GIS project node to show only map,doc and src folders.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.nodes.FilterNode
 */
public class GISProjectNodeFilter extends FilterNode.Children{

    private final GISProject project;
    
    public GISProjectNodeFilter(Node original, GISProject project){
        super(original);
        this.project = project;
    }

    @Override
    protected Node[] createNodes(Node node) {
        DataObject dob =node.getLookup().lookup (DataObject.class);
        FileObject file = dob.getPrimaryFile();
        
        if(file.equals(project.getSourceFolder(true))){
            return new Node[]{new GISSourceNode(node)};
        }else if(file.equals(project.getMapFolder(true))){
            return new Node[]{new GISMapNode(node)};
        }else if(file.equals(project.getDocFolder(true))){
            return new Node[]{new GISDocNode(node)};
        }
        return new Node[0];

//        return super.createNodes(node);
    }


//    protected Node[] createNodes(Object object) {
//        Node origChild = (Node) object;
//        DataObject dob = (DataObject)
//            origChild.getLookup().lookup (DataObject.class);
//
//        if (dob != null) {
//            FileObject fob = dob.getPrimaryFile();
//            if ("text/x-povray".equals(fob.getMIMEType())) {
//                return super.createNodes (object);
//            } else if (dob instanceof DataFolder) {
//                //Allow child folders of the scenes/ dir
//                return new Node[] {
//                    new FilterNode (origChild,
//                            new ProjectFilterChildren(origChild))
//                };
//            }
//        }
//        //Don't create any nodes for non-povray files
//        return new Node[0];
//    }

    
}
