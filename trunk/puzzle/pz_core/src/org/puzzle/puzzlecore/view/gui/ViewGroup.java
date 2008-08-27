/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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

package org.puzzle.puzzlecore.view.gui;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children.Array;
import org.openide.util.lookup.Lookups;
import org.puzzle.puzzlecore.view.MapGroup;

/**
 *
 * @author Administrateur
 */
class ViewGroup extends AbstractNode{

    public ViewGroup(MapGroup obj) {
        super (new Array(), Lookups.singleton(obj));
        setDisplayName(obj.getTitle());
    }
    
    @Override
    public String getHtmlDisplayName() {
        return "<b>" + getDisplayName() + "</b>";
    }

    @Override
    public Image getIcon(int arg0) {
        return ViewNodeModel.ICON_GROUP;
    }

    @Override
    public Image getOpenedIcon(int arg0) {
        return getIcon(arg0);
    }
    
    
}
