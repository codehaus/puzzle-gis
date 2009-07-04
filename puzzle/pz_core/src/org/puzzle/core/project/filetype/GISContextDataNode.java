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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.puzzle.core.project.view.GISView;

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
        super(obj, new ViewChildren(obj));
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


    static class ViewChildren extends Children.Array implements LookupListener{

        private Lookup.Result<GISView> result = null;

        public ViewChildren(GISContextDataObject obj) {
            result = obj.getLookup().lookupResult(GISView.class);
            result.addLookupListener(this);
            parse(result);
        }

        private void parse(Lookup.Result<GISView> r){
            final Collection<? extends GISView> c = r.allInstances();
            remove(getNodes());
            if (!c.isEmpty()) {
                for(GISView view : c){
                    add(new Node[]{new GISContextViewNode(view)});
                }
            }
        }

        @Override
        public void resultChanged(LookupEvent lookupEvent) {
            final Lookup.Result<GISView> r = (Lookup.Result<GISView>) lookupEvent.getSource();
            parse(r);
        }

    }

}
