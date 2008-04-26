/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
