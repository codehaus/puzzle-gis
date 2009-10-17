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
package org.puzzle.core.project.filetype;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.puzzle.core.project.view.GISView;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class GISContextViewNode extends AbstractNode{

    private static final Image IMAGE_VIEW = ImageUtilities.loadImage("org/puzzle/core/resources/view.png");
    private final GISView view;

    public GISContextViewNode(GISView view) {
        super(Children.LEAF, createLookup(view));
        super.setName(view.getTitle());
        this.view = view;
    }

    @Override
    public Image getIcon(int type) {
        return IMAGE_VIEW;
    }

    @Override
    public String getName() {
        return view.getTitle();
    }

    @Override
    public void setName(String s) {
        view.setTitle(s);
        super.setName(s);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        System.out.println("here");
        view.getContext().removeView(view);
        super.destroy();
    }

    @Override
    public Action[] getActions(boolean context) {        
        final List<? extends Action> actions = org.openide.util.Utilities.actionsForPath("Loaders/text/giscontextview/Actions");
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public SystemAction getDefaultAction() {
        return (SystemAction) getActions(false)[0];
    }

    private static Lookup createLookup(GISView view){
        InstanceContent content = new InstanceContent();
        content.add(view);
        return new AbstractLookup(content);
    }

}
