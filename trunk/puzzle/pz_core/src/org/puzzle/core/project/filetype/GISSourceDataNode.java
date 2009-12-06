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
import java.util.List;
import javax.swing.Action;

import org.geotoolkit.data.DataStore;

import org.openide.loaders.DataNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

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

    private static final Image IMAGE_LOADED = ImageUtilities.loadImage("org/puzzle/core/resources/source_loaded.png");
    private static final Image IMAGE_UNLOADED = ImageUtilities.loadImage("org/puzzle/core/resources/source_unloaded.png");
    private static final Image IMAGE_ERROR = ImageUtilities.loadImage("org/puzzle/core/resources/source_error.png");
    private static final String IMAGE_ICON_BASE = "org/puzzle/core/resources/signal-1.png";
    private static final String IMAGE_ICON_TYPE = "org/puzzle/core/resources/sourcetable.png";
    
    /**
     * Constructor.<br>
     * Creates the node from the {@code GISContextDataObject}.
     * @param obj   The {@code GISContextDataObject} used.
     */
    GISSourceDataNode(GISSourceDataObject obj, Lookup lookup) {
        super(obj, Children.create(new MyFactory(obj), true), lookup);

        GISSource source = obj.getSource();
        if(source != null){
            obj.getSource().addPropertyChangeListener(GISSource.STATE_PROPERTY, new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    fireIconChange();
                }
            });
        }

        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    @Override
    public String getHtmlDisplayName() {
        String str = super.getHtmlDisplayName();
        if(str != null) str = str.replaceAll(".xml", "");
        return str;
    }

    @Override
    public Image getOpenedIcon(int arg0) {
        return getIcon(arg0);
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
    
    private static class MyFactory extends ChildFactory<String>{

        private final GISSourceDataObject obj;
        private String[] typeNames = null;

        public MyFactory(GISSourceDataObject obj){
            this.obj = obj;
        }

        @Override
        protected boolean createKeys(List<String> keys) {

            obj.getSource().load();
            final DataStore store = obj.getSource().getLookup().lookup(DataStore.class);

            if(store != null){
                try {
                    typeNames = store.getTypeNames();

                    for(int i=0; i<typeNames.length; i++){
                        keys.add(typeNames[i]);
                    }

                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            return true;
        }

        @Override
        protected Node createNodeForKey(String key) {
            obj.getSource().load();
            final DataStore store = obj.getSource().getLookup().lookup(DataStore.class);

            return new TypeNode(key);
        }
        
    }

    private static class TypeNode extends AbstractNode{

        private final String type;

        private TypeNode(String type){
            super(Children.LEAF, Lookups.singleton(type));
            this.type = type;
            setName(type);
            setIconBaseWithExtension(IMAGE_ICON_TYPE);
            setDisplayName(type);
        }

        @Override
        public Action[] getActions(boolean context) {
            final List<? extends Action> actions = org.openide.util.Utilities.actionsForPath("Loaders/text/gissourcetype/Actions");
            return actions.toArray(new Action[actions.size()]);
        }

        @Override
        public SystemAction getDefaultAction() {
            return (SystemAction) getActions(false)[0];
        }



    }

}
