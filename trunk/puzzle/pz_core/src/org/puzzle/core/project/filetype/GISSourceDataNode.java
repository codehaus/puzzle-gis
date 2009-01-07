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
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceState;

/**
 * This classe provides a {@code org.openide.loaders.DataNode} for the
 * {@link GISSourceDataObject}.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.loaders.DataNode
 */
public class GISSourceDataNode extends DataNode {

    private static final Image IMAGE_LOADED = ImageUtilities.loadImage("org/puzzle/core/project/filetype/source_loaded.png");
    private static final Image IMAGE_UNLOADED = ImageUtilities.loadImage("org/puzzle/core/project/filetype/source_unloaded.png");
    private static final Image IMAGE_ERROR = ImageUtilities.loadImage("org/puzzle/core/project/filetype/source_error.png");
    private static final String IMAGE_ICON_BASE = "org/puzzle/core/project/filetype/signal-1.png";
    
    /**
     * Constructor.<br>
     * Creates the node from the {@code GISContextDataObject}.
     * @param obj   The {@code GISContextDataObject} used.
     */
    GISSourceDataNode(GISSourceDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);

        obj.getSource().addPropertyChangeListener(GISSource.STATE_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireIconChange();
            }
        });

        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    @Override
    public String getHtmlDisplayName() {
        String str = super.getHtmlDisplayName();
        if(str != null) str = str.replaceAll(".xml", "");
        return str;
    }

    @Override
    public Image getIcon(int arg0) {
        Image img = null;
        if(getDataObject() != null && getDataObject() instanceof GISSourceDataObject){
            GISSourceDataObject data = getDataObject();
            if(data.getSource() != null){
                final GISSource source = data.getSource();
                img = source.getIcon(arg0);

                if(source.getState() == GISSourceState.LOADED){
                    img = ImageUtilities.mergeImages(img, IMAGE_LOADED, 8, 0);
                }else if(source.getState() == GISSourceState.UNLOADED){
                    img = ImageUtilities.mergeImages(img, IMAGE_UNLOADED, 8, 0);
                }else{
                    img = ImageUtilities.mergeImages(img, IMAGE_ERROR, 8, 0);
                }
            }
        }
        if(img != null) return img;
        return super.getIcon(arg0);
    }

    @Override
    public GISSourceDataObject getDataObject() {
        return (GISSourceDataObject) super.getDataObject();
    }

    @Override
    public void destroy() throws IOException {
        getDataObject().dispose();
        super.destroy();
    }

    @Override
    public String getDisplayName() {
        String str = super.getDisplayName();
        if(str != null) str = str.replaceAll(".xml", "");
        return str;
    }
    
}
