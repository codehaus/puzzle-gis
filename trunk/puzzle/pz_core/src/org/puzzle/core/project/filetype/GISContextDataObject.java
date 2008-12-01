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

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBException;

import org.geotools.data.DefaultQuery;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.ContextListener;
import org.geotools.map.MapBuilder;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.style.CollectionChangeEvent;
import org.geotools.style.MutableStyle;
import org.geotools.style.sld.Specification.Filter;
import org.geotools.style.sld.Specification.StyledLayerDescriptor;
import org.geotools.style.sld.Specification.SymbologyEncoding;
import org.geotools.style.sld.XMLUtilities;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;

import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.xml.XMLUtil;

import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is used to represent XML files describing contexts for the
 * project. It extends {@code XMLDocument} which provides main functionalities
 * for using XML documents.<br>
 * This class provides special support for understanding and using 
 * contexts files.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.loaders.XMLDataObject
 */
public class GISContextDataObject extends XMLDataObject {

    private static final String TAG_CONTEXT_CRS = "Crs";
    private static final String TAG_LAYERS = "Layers";
    private static final String TAG_LAYER = "Layer";
    private static final String TAG_LAYER_TITLE = "Title";
    private static final String TAG_LAYER_SOURCE = "Source";
    private static final String TAG_LAYER_SOURCE_PARAMS = "Parameters";
    private static final String TAG_LAYER_SOURCE_ID = "Id";
    private static final String TAG_LAYER_STYLE = "sld:UserStyle";
    private static final String TAG_LAYER_VISIBLE = "visible";
    private static final String TAG_LAYER_QUERY = "filter";
    private final DefaultContextListener contextListener = new DefaultContextListener();
    private final SavingThread saver = new SavingThread();
    private MapContext context = null;
    private boolean needSave = false;

    /**
     * Constructor.
     * This contructor creates a {@code GISContextDataObject} and make it
     * openable in an editor.
     * @param   pf      The {@code FileObject} representing XML document.
     * @param   loader  The loader to use for this {@code DataObject}.
     * @throws  org.openide.loaders.DataObjectExistsException
     * @throws  java.io.IOException
     */
    public GISContextDataObject(FileObject pf, GISContextDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        saver.start();
        CookieSet cookies = getCookieSet();
        cookies.add((org.openide.nodes.Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));

    }

    @Override
    public void dispose() {
        Project prj = FileOwnerQuery.getOwner(getPrimaryFile());
        if (prj != null && prj instanceof GISProject) {
            GISProject gisprj = (GISProject) prj;
            gisprj.removeContext(getContext());
        }
        super.dispose();
    }

    /**
     * This method is used to retrieve the {@code MapContext} associated with
     * the {@code GISContextDataObject}. If not context is currently associated,
     * it creates a new one and intialize it with layers described in the 
     * XML document.
     * @return  The {@code MapContext} associated.
     */
    public MapContext getContext() {

        if (context == null) {
            ProgressHandle handle = ProgressHandleFactory.createHandle(Utilities.getString("loadingContext"));
            handle.start(100);
            handle.switchToIndeterminate();
            // at this point the task is finished and removed from status bar
            // it's not realy necessary to count all the way to the limit, finish can be called earlier.
            // however it has to be called at the end of the processing.



            context = parseContext(getDOM());
            context.addContextListener(contextListener);
            handle.finish();
        }

        return context;
    }

    @Override
    protected org.openide.nodes.Node createNodeDelegate() {
        return new GISContextDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    private Document getDOM() {
        Document doc = null;
        try {
            doc = getDocument();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }
        return doc;
    }

    private Collection<? extends GISSource> getGISSources() {
        GISProject prj = (GISProject) FileOwnerQuery.getOwner(getPrimaryFile());
        return prj.getGISSources();
    }

    private MapContext parseContext(Document gisDoc) {
        context = MapBuilder.getInstance().createContext(DefaultGeographicCRS.WGS84);
        context.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(getPrimaryFile().getName().replaceAll(".xml", ""), ""));

        if (gisDoc != null) {

            final NodeList layerNodes = gisDoc.getElementsByTagName(TAG_LAYER);
            for (int i = 0, n = layerNodes.getLength(); i < n; i++) {
                final MapLayer layer = parseLayer(layerNodes.item(i));
                if(layer != null){
                    context.layers().add(layer);
                    context.setCoordinateReferenceSystem(layer.getBounds().getCoordinateReferenceSystem());
                }

            }
        }

        return context;
    }

    private MapLayer parseLayer(Node node) {
        MapLayer layer = null;
        int id = 0;
        Map<String, String> params = null;
        String title = "";
        MutableStyle style = CommonFactoryFinder.getStyleFactory(null).createStyle();
        DefaultQuery query = new DefaultQuery();
        boolean visible = true;

        final NodeList elements = node.getChildNodes();
        for (int j = 0, m = elements.getLength(); j < m; j++) {
            final Node elementNode = elements.item(j);
            final String elementName = elementNode.getNodeName();

            if (TAG_LAYER_SOURCE.equals(elementName)) {
                id = parseSourceId(elementNode);
                params = parseSourceParams(elementNode);
            } else if (TAG_LAYER_TITLE.equals(elementName)) {
                title = elementNode.getTextContent();
            } else if (TAG_LAYER_STYLE.equals(elementName)) {
                try {
                    style = new XMLUtilities().readStyle(elementNode, SymbologyEncoding.V_1_1_0);
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (TAG_LAYER_VISIBLE.equals(elementName)) {
                visible = Boolean.parseBoolean(elementNode.getTextContent());
            } else if (TAG_LAYER_QUERY.equals(elementName)) {
                try {
                    query.setFilter(new XMLUtilities().readFilter(elementNode, Filter.V_1_1_0));
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        //we have a "correct" layer
        if (id > 0) {
            Collection<? extends GISSource> sources = getGISSources();
            for (GISSource src : sources) {
                if (src.getInfo().getID() == id) {
                    layer = src.createLayer(params);
                    layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(title, ""));
                    layer.setStyle(style);
                    layer.setVisible(visible);
                    layer.setQuery(query);
                }
            }
        }

        return layer;
    }

    private int parseSourceId(Node sourceNode) {
        int id = 0;
        NodeList elements = sourceNode.getChildNodes();
        for (int i = 0, n = elements.getLength(); i < n; i++) {
            Node elementNode = elements.item(i);
            String elementName = elementNode.getNodeName();

            if (TAG_LAYER_SOURCE_ID.equals(elementName)) {
                id = Integer.valueOf(elementNode.getTextContent());
                break;
            }
        }

        return id;
    }

    private Map<String, String> parseSourceParams(Node sourceNode) {
        Map<String, String> parameters = new HashMap<String, String>();
        NodeList elements = sourceNode.getChildNodes();
        for (int i = 0, n = elements.getLength(); i < n; i++) {
            Node elementNode = elements.item(i);
            String elementName = elementNode.getNodeName();

            if (TAG_LAYER_SOURCE_PARAMS.equals(elementName)) {
                NodeList params = elementNode.getChildNodes();
                for (int j = 0, m = params.getLength(); j < m; j++) {
                    Node paramNode = params.item(j);
                    if (paramNode instanceof Element) {
                        parameters.put(paramNode.getNodeName(), paramNode.getTextContent());
                    }
                }
                break;
            }
        }

        return parameters;
    }

    private void DOMremoveLayer(final int index) {
        final Document doc = getDOM();
        final Node root = doc.getElementsByTagName(TAG_LAYERS).item(0);
        final NodeList children = root.getChildNodes();
        int onLayer = -1;

        for (int i = 0, n = children.getLength(); i < n; i++) {
            final Node child = children.item(i);
            final String name = child.getNodeName();
            if (name.equals(TAG_LAYER)) {
                onLayer++;
            }
            if (onLayer == index) {
                root.removeChild(child);
                break;
            }
        }

        DOMsave(doc);
    }

    private void DOMupdateLayer(final MapLayer layer, final int index, final EventObject event) {
        final Document doc = getDOM();
        final Node root = doc.getElementsByTagName(TAG_LAYERS).item(0);
        final NodeList children = root.getChildNodes();
        int onLayer = -1;
        for (int i = 0, n = children.getLength(); i < n; i++) {
            final Node child = children.item(i);
            final String name = child.getNodeName();
            if (name.equals(TAG_LAYER)) {
                onLayer++;
            }
            if (onLayer == index) {
                //we are on the layer to update
                DOMencodeLayer(doc, layer, child, event);
                break;
            }
        }
        DOMsave(doc);
    }

    private Node DOMencodeLayer(final Document doc, final MapLayer layer, final Node layerNode, final EventObject event) {

        final LayerSource source = (LayerSource) layer.getUserPropertie(PZLayerConstants.KEY_LAYER_INFO);
        if (source == null) {
            //can not save this layer
            return layerNode;
        }

        if (event == null || !(event instanceof PropertyChangeEvent)) {
            //no provided event save everything
            //clear all store informations
            final NodeList children = layerNode.getChildNodes();
            for (int i = children.getLength() - 1; i >= 0; i--) {
                layerNode.removeChild(children.item(i));
            }

            //store layer title
            final String title = layer.getDescription().getTitle().toString();
            final Element layerTitle = doc.createElement(TAG_LAYER_TITLE);
            layerTitle.setTextContent(title);
            layerNode.appendChild(layerTitle);

            //store layer source
            final int id = source.getSourceId();
            final Map<String, String> params = source.getParameters();
            final Element layerSource = doc.createElement(TAG_LAYER_SOURCE);
            final Element sourceId = doc.createElement(TAG_LAYER_SOURCE_ID);
            final Element sourceParams = doc.createElement(TAG_LAYER_SOURCE_PARAMS);
            final Element visibleNode = doc.createElement(TAG_LAYER_VISIBLE);
            sourceId.setTextContent(String.valueOf(id));
            final Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                final String key = keys.next();
                final Element aParam = doc.createElement(key);
                aParam.setTextContent(params.get(key));
                sourceParams.appendChild(aParam);
            }
            visibleNode.setTextContent(Boolean.valueOf(layer.isVisible()).toString());
            layerSource.appendChild(sourceId);
            layerSource.appendChild(sourceParams);
            layerNode.appendChild(layerSource);
            layerNode.appendChild(visibleNode);

            //store features filter
            if (layer.getQuery() != null && layer.getQuery().getFilter() != null) {
                final org.opengis.filter.Filter filter = layer.getQuery().getFilter();
                if (!filter.equals(org.opengis.filter.Filter.INCLUDE) &&
                        !filter.equals(org.opengis.filter.Filter.EXCLUDE)) {
                    try {
                        new XMLUtilities().writeFilter(layerNode, layer.getQuery().getFilter(), Filter.V_1_1_0);
                    } catch (JAXBException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            //store layer style
            //todo this is slow, try to avoid it
            try {
                new XMLUtilities().writeStyle(layerNode, layer.getStyle(), StyledLayerDescriptor.V_1_1_0);
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            final PropertyChangeEvent propEvent = (PropertyChangeEvent) event;
        }

        return layerNode;
    }

    private synchronized void DOMencodeLayers() {
        final Document doc = getDOM();
        final Node root = doc.getElementsByTagName(TAG_LAYERS).item(0);

        //we clear the layers list
        //TODO avoid doing so by using a more accurate listener method
        final NodeList children = root.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            root.removeChild(children.item(i));
        }

        //create layer nodes
        for (final MapLayer layer : context.layers()) {
            final Element layerNode = doc.createElement(TAG_LAYER);
            DOMencodeLayer(doc, layer, layerNode, null);
            root.appendChild(layerNode);
        }

        DOMsave(doc);
    }

    private class DefaultContextListener implements ContextListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setNeedSave(true);
//            DOMencodeLayers();
        }

        @Override
        public void layerChange(CollectionChangeEvent<MapLayer> evt) {
            setNeedSave(true);
//            switch(evt.getType()){
//                case CollectionChangeEvent.ITEM_ADDED :
//                    DOMencodeLayers();
//                    break;
//                case CollectionChangeEvent.ITEM_CHANGED :
//                    //assume there is only one layer change
//                    DOMupdateLayer(evt.getItems().iterator().next(), (int)evt.getRange().getMinimum(),evt.getChangeEvent() );
//                    break;
//                case CollectionChangeEvent.ITEM_REMOVED :
//                    //assume there is only one layer added
//                    DOMremoveLayer( (int)evt.getRange().getMinimum() );
//                    break;
//            }
        }
    }

    private void setNeedSave(boolean b) {
        needSave = b;
        if (needSave) {
            saver.wake();
        }


    }

    private boolean needSaving() {
        return needSave;
    }

    private void DOMsave(Document doc) {
        try {
            final OutputStream output = getPrimaryFile().getOutputStream(FileLock.NONE);
            XMLUtil.write(doc, output, "UTF-8");
            output.flush();
            output.close();
        } catch (FileAlreadyLockedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private class SavingThread extends Thread {

        private boolean dispose = false;

        public void dispose(){
            dispose = true;
            wake();
        }

        @Override
        public void run() {

            while (!dispose) {

                while (needSaving()) {

                    setNeedSave(false);
                    DOMencodeLayers();
                }

                block();
            }
        }

        public synchronized void wake() {
            notifyAll();
        }

        private synchronized void block() {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


}
