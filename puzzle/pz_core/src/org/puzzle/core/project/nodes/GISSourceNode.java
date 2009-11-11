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

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

import org.puzzle.core.actions.NewDistantSource;
import org.puzzle.core.actions.NewFileSource;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.resources.CoreResource;

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

    private final Image ICON = ImageUtilities.loadImage("org/puzzle/core/resources/source.png", true);

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
        return CoreResource.getString("sources");
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Action[] actions = super.getActions(arg0);

        final DataObject obj = getLookup().lookup(DataObject.class);
        final Project proj = FileOwnerQuery.getOwner(obj.getPrimaryFile());
        
        if(proj != null && proj instanceof GISProject){
            Action[] temp = actions;
            actions = new Action[actions.length+2];
            actions[0] = new NewFileSource((GISProject)proj);
            actions[1] = new NewDistantSource((GISProject)proj);
            for (int i = 0; i < temp.length; i++) {
                actions[i+2] = temp[i];

            }
        }

        return actions;
    }

}