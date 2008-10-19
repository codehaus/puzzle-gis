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

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

import org.puzzle.core.actions.NewDistantSource;
import org.puzzle.core.actions.NewFileSource;
import org.puzzle.core.project.GISProject;

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
        final DataObject obj = getLookup().lookup(DataObject.class);
        final Project proj = FileOwnerQuery.getOwner(obj.getPrimaryFile());
        
        if(proj != null && proj instanceof GISProject){
            return new Action[]{
                new NewFileSource((GISProject)proj),
                new NewDistantSource((GISProject)proj)
            };
        }else{
            return new Action[0];
        }
    }

}