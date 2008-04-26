/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.view.gui;

import java.awt.Image;
import java.beans.IntrospectionException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children.Array;
import org.openide.util.lookup.Lookups;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author Administrateur
 */
class ViewNode extends BeanNode<MapView>{

//    private boolean TranslationLink = true;
    
    public ViewNode(MapView obj) throws IntrospectionException {
        super(obj);
//        super (new Array(), Lookups.singleton(obj));
//        obj.isTranslationLink()
        setDisplayName ( obj.getName());
    }

    @Override
    public String getHtmlDisplayName() {
        return super.getHtmlDisplayName();
    }

    @Override
    public Image getIcon(int arg0) {
        return ViewNodeModel.ICON_VIEW;
    }

    @Override
    public Image getOpenedIcon(int arg0) {
        return getIcon(arg0);
    }
//
//    public boolean isTranslationLink() {
//        return TranslationLink;
//    }
//
//    public void setTranslationLink(boolean lin) {
//        this.TranslationLink = lin;
//    }

}
