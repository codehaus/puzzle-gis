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

    private static final Image IMAGE_VIEW = ImageUtilities.loadImage("org/puzzle/core/project/filetype/view.png");
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
