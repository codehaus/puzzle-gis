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
package org.puzzle.puzzlecore.project.nodes;

import java.awt.Image;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.puzzle.puzzlecore.project.GISProject;

/**
 * This class provides a {@link org.openide.nodes.Node} for the "doc" folder
 * defined in the {@link org.puzzle.puzzlecore.project.GISProject}.
 * This folder is intended to contain all files document files, used to make
 * reports or printable maps...
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.nodes.FilterNode
 */
public class GISDocNode extends FilterNode{
    
    private static final String ICON_PATH = "org/puzzle/puzzlecore/project/doc.png";
    private static final Image ICON = Utilities.loadImage(ICON_PATH, true);
    
    private final GISProject project;
    
    /**
     * Constructor.
     * @param   node      The {@code Node} describing the folder.
     * @param   project   The current {@code GISProject}.
     * @throws  org.openide.loaders.DataObjectNotFoundException
     */
    public GISDocNode(Node node, GISProject project) throws DataObjectNotFoundException{
        super (node, new FilterNode.Children (node),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup (new Lookup[] { Lookups.singleton(project),
                    node.getLookup() }));
            this.project = project;
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
        return "Documents";
    }
    
}
