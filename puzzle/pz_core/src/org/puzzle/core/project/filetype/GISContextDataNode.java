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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 * This classe provides a {@code org.openide.loaders.DataNode} for the
 * {@link GISContextDataObject}.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.loaders.DataNode
 */
public class GISContextDataNode extends DataNode {

    private static final Image IMAGE_LOADED = ImageUtilities.loadImage("org/puzzle/core/project/filetype/context_loaded.png");

    private static final String IMAGE_ICON_BASE = "org/puzzle/core/project/filetype/locale.png";

    /**
     * Constructor.<br>
     * Creates the node from the {@code GISsourceDataObject}.
     * @param obj   The {@code GISSourceDataObject} used.
     */
     GISContextDataNode(GISContextDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);

        obj.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(GISContextDataObject.STATE_PROPERTY)){
                    fireIconChange();
                }
            }
        });

    }

    @Override
    public String getHtmlDisplayName() {
        String str = super.getHtmlDisplayName();
        if(str != null) str = str.replaceAll(".xml", "");
        return str;
    }

    @Override
    public String getDisplayName() {
        String str = super.getDisplayName();
        if(str != null) str = str.replaceAll(".xml", "");
        return str;
    }

    @Override
    public Image getIcon(int arg0) {
        Image img = super.getIcon(arg0);

        if(getDataObject().getState() == GISContextState.LOADED){
            img = ImageUtilities.mergeImages(img, IMAGE_LOADED, 6, 0);
        }

        return img;
    }

    @Override
    public void destroy() throws IOException {
        getDataObject().dispose();
        super.destroy();
    }

    @Override
    public GISContextDataObject getDataObject() {
        return (GISContextDataObject)super.getDataObject();
    }

}
