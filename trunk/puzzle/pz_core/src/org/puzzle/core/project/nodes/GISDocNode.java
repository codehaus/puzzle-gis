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

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.puzzle.core.resources.CoreResource;

/**
 * This class provides a {@link org.openide.nodes.Node} for the "doc" folder
 * defined in the {@link org.puzzle.core.project.GISProject}.
 * This folder is intended to contain all files document files, used to make
 * reports or printable maps...
 *
 * @author Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 *
 * @see     org.openide.nodes.FilterNode
 */
public class GISDocNode extends FilterNode {

    private final Image ICON = ImageUtilities.loadImage("org/puzzle/core/resources/doc.png", true);

    /**
     * Constructor.
     * @param   node      The {@code Node} describing the folder.
     */
    public GISDocNode(Node node) {
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
        return CoreResource.getString("documents");
    }

    @Override
    public boolean canRename() {
        return false;
    }
}