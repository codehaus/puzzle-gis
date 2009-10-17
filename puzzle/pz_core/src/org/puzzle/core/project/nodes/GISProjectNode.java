/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.core.project.nodes;

import java.awt.Image;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ui.support.CommonProjectActions;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

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

    private final ImageIcon ICON = new ImageIcon(ImageUtilities.loadImage("org/puzzle/core/resources/boussole.png", true));

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
                    Lookups.singleton(project));
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
            CommonProjectActions.setAsMainProjectAction(),
            CommonProjectActions.closeProjectAction()
        };
    }

}

class GISProjectNodeFilter extends FilterNode.Children{

    private final GISProject project;

    public GISProjectNodeFilter(Node original,GISProject project){
        super(original);
        this.project = project;
    }

    @Override
    protected Node copyNode(Node node) {
        final DataObject dob = node.getLookup().lookup (DataObject.class);
        final FileObject file = dob.getPrimaryFile();

        if(file.equals(project.getSourceFolder(true))){
            return new GISSourceNode(node);
        }else if(file.equals(project.getMapFolder(true))){
            return new GISMapNode(node);
        }else if(file.equals(project.getDocFolder(true))){
            return new GISDocNode(node);
        }else{
            return new FilterNode(node);
        }
    }


    @Override
    protected Node[] createNodes(Node node) {
        final String name = node.getName();
        if(name.equals("gisproject")){
            return new Node[0];
        }else{
            return super.createNodes(node);
        }
    }

}


