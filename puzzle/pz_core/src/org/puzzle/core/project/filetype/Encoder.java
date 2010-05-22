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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;

import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.map.FeatureMapLayer;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.sld.xml.Specification.StyledLayerDescriptor;
import org.geotoolkit.sld.xml.Specification.SymbologyEncoding;
import org.geotoolkit.sld.xml.XMLUtilities;

import org.opengis.filter.Filter;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.openide.util.Exceptions;

import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.capabilities.LayerCreation;
import org.puzzle.core.project.view.GISViewInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Encoder {

    private static final String TAG_CONTEXT_CRS = "Crs";
    private static final String TAG_PARAMETERS = "Parameters";
    private static final String TAG_ID = "Id";
    private static final String TAG_TITLE = "Title";

    //tags for layers
    private static final String TAG_LAYERS = "Layers";
    private static final String TAG_LAYER = "Layer";
    private static final String TAG_LAYER_SOURCE = "Source";
    private static final String TAG_LAYER_STYLE = "sld:UserStyle";
    private static final String TAG_LAYER_SELECTABLE = "selectable";
    private static final String TAG_LAYER_VISIBLE = "visible";
    private static final String TAG_LAYER_QUERY = "filter";

    //tags for views
    private static final String TAG_VIEWS = "Views";
    private static final String TAG_VIEW = "View";
    private static final String TAG_VIEW_SERVICE = "Service";

    private Encoder(){}

    public static MapContext parseContext(Document gisDoc, Collection<? extends GISSource> sources) {
        final MapContext context = MapBuilder.createContext(DefaultGeographicCRS.WGS84);

        if (gisDoc != null) {
            final NodeList layerNodes = gisDoc.getElementsByTagName(TAG_LAYER);
            for (int i = 0, n = layerNodes.getLength(); i < n; i++) {
                final MapLayer layer = parseLayer(layerNodes.item(i),sources);
                if(layer != null){
                    context.layers().add(layer);
                    Envelope env = layer.getBounds();
                    if(env != null && env.getCoordinateReferenceSystem() != null){
                        context.setCoordinateReferenceSystem(env.getCoordinateReferenceSystem());
                    }
                }

            }
        }

        return context;
    }

    public static List<GISViewInfo> parseViews(Document gisDoc) {
        final List<GISViewInfo> views = new ArrayList<GISViewInfo>();

        if (gisDoc != null) {

            final NodeList layerNodes = gisDoc.getElementsByTagName(TAG_VIEW);
            for (int i = 0, n = layerNodes.getLength(); i < n; i++) {
                views.add(parseView(layerNodes.item(i)));

            }
        }

        return views;
    }


    private static MapLayer parseLayer(Node node, Collection<? extends GISSource> sources) {
        MapLayer layer = null;
        int id = 0;
        Map<String, String> params = null;
        String title = "";
        MutableStyle style = new DefaultStyleFactory().style();
        Filter filter = null;
        boolean visible = true;
        boolean selectable = true;

        final NodeList elements = node.getChildNodes();
        for (int j = 0, m = elements.getLength(); j < m; j++) {
            final Node elementNode = elements.item(j);
            final String elementName = elementNode.getNodeName();

            if (TAG_LAYER_SOURCE.equals(elementName)) {
                id = parseId(elementNode);
                params = parseParameters(elementNode);
            } else if (TAG_TITLE.equals(elementName)) {
                title = elementNode.getTextContent();
            } else if (TAG_LAYER_STYLE.equals(elementName)) {
                try {
                    style = new XMLUtilities().readStyle(elementNode, SymbologyEncoding.V_1_1_0);
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (FactoryException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (TAG_LAYER_VISIBLE.equals(elementName)) {
                visible = Boolean.parseBoolean(elementNode.getTextContent());
            } else if (TAG_LAYER_SELECTABLE.equals(elementName)) {
                selectable = Boolean.parseBoolean(elementNode.getTextContent());
            } else if (TAG_LAYER_QUERY.equals(elementName)) {
                try {
                    filter = new XMLUtilities().readFilter(elementNode, org.geotoolkit.sld.xml.Specification.Filter.V_1_1_0);
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (FactoryException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        //we have a "correct" layer
        if (id > 0) {
            for (GISSource src : sources) {
                if (src.getInfo().getID() == id) {
                    LayerCreation lc = src.getLookup().lookup(LayerCreation.class);
                    layer = lc.createLayer(params);
                    layer.setDescription(new DefaultStyleFactory().description(title, ""));
                    layer.setStyle(style);
                    layer.setVisible(visible);
                    layer.setSelectable(selectable);
                    if(layer instanceof FeatureMapLayer){
                        FeatureMapLayer fml = (FeatureMapLayer) layer;
                        if(filter != null){
                            ((FeatureMapLayer)layer).setQuery(QueryBuilder.filtered(fml.getCollection().getFeatureType().getName(), filter));
                        }
                    }
                }
            }
        }

        return layer;
    }

    private static int parseId(Node sourceNode) {
        int id = 0;
        final NodeList elements = sourceNode.getChildNodes();
        for (int i = 0, n = elements.getLength(); i < n; i++) {
            final Node elementNode = elements.item(i);
            final String elementName = elementNode.getNodeName();

            if (TAG_ID.equals(elementName)) {
                id = Integer.valueOf(elementNode.getTextContent());
                break;
            }
        }

        return id;
    }

    private static Map<String, String> parseParameters(Node sourceNode) {
        final Map<String, String> parameters = new HashMap<String, String>();
        final NodeList elements = sourceNode.getChildNodes();
        if(elements == null) return parameters;
        for (int i = 0, n = elements.getLength(); i < n; i++) {
            final Node elementNode = elements.item(i);
            final String elementName = elementNode.getNodeName();

            if (TAG_PARAMETERS.equals(elementName)) {
                final NodeList params = elementNode.getChildNodes();
                for (int j = 0, m = params.getLength(); j < m; j++) {
                    final Node paramNode = params.item(j);
                    if (paramNode instanceof Element) {
                        parameters.put(paramNode.getNodeName(), paramNode.getTextContent());
                    }
                }
                break;
            }
        }

        return parameters;
    }

    private static void DOMremoveLayer(final Document doc,final int index) {
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

    }

    private static void DOMupdateLayer(final Document doc,final MapLayer layer, final int index, final EventObject event) {
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
    }

    private static Node DOMencodeLayer(final Document doc, final MapLayer layer, final Node layerNode, final EventObject event) {

        final GISLayerSource source = (GISLayerSource) layer.getUserPropertie(GISLayerSource.KEY_LAYER_INFO);
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
            final Element layerTitle = doc.createElement(TAG_TITLE);
            layerTitle.setTextContent(title);
            layerNode.appendChild(layerTitle);

            //store layer source
            final int id = source.getSourceId();
            final Map<String, String> params = source.getParameters();
            final Element layerSource = doc.createElement(TAG_LAYER_SOURCE);
            final Element sourceId = doc.createElement(TAG_ID);
            final Element sourceParams = doc.createElement(TAG_PARAMETERS);
            final Element visibleNode = doc.createElement(TAG_LAYER_VISIBLE);
            final Element selectableNode = doc.createElement(TAG_LAYER_SELECTABLE);
            sourceId.setTextContent(String.valueOf(id));
            final Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                final String key = keys.next();
                final Element aParam = doc.createElement(key);
                aParam.setTextContent(params.get(key));
                sourceParams.appendChild(aParam);
            }
            visibleNode.setTextContent(Boolean.toString(layer.isVisible()));
            selectableNode.setTextContent(Boolean.toString(layer.isSelectable()));
            layerSource.appendChild(sourceId);
            layerSource.appendChild(sourceParams);
            layerNode.appendChild(layerSource);
            layerNode.appendChild(visibleNode);
            layerNode.appendChild(selectableNode);

            //store features filter
            if(layer instanceof FeatureMapLayer){
                final FeatureMapLayer fml = (FeatureMapLayer) layer;
                if (fml.getQuery() != null && fml.getQuery().getFilter() != null) {
                    final org.opengis.filter.Filter filter = fml.getQuery().getFilter();
                    if (!filter.equals(org.opengis.filter.Filter.INCLUDE) &&
                            !filter.equals(org.opengis.filter.Filter.EXCLUDE)) {
                        try {
                            new XMLUtilities().writeFilter(layerNode, fml.getQuery().getFilter(), org.geotoolkit.sld.xml.Specification.Filter.V_1_1_0);
                        } catch (JAXBException ex) {
                            Exceptions.printStackTrace(ex);
                        }
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

    private static Node encodeView(final Document doc, final GISViewInfo info, final Node viewNode) {

        //clear all store informations
        final NodeList children = viewNode.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            viewNode.removeChild(children.item(i));
        }


        //store view id
        final String id = String.valueOf(info.getID());
        final Element elementId = doc.createElement(TAG_ID);
        elementId.setTextContent(id);
        viewNode.appendChild(elementId);

        //store view title
        final String title = info.getTitle();
        final Element elementTitle = doc.createElement(TAG_TITLE);
        elementTitle.setTextContent(title);
        viewNode.appendChild(elementTitle);
        
        //store view service
        final String service = info.getServiceName();
        final Element elementService = doc.createElement(TAG_VIEW_SERVICE);
        elementService.setTextContent(service);
        viewNode.appendChild(elementService);

        

        //store layer source
        final Map<String, String> params = info.parameters();
        final Element sourceParams = doc.createElement(TAG_PARAMETERS);
        final Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            final String key = keys.next();
            final Element aParam = doc.createElement(key);
            aParam.setTextContent(params.get(key));
            sourceParams.appendChild(aParam);
        }
        viewNode.appendChild(sourceParams);

        return viewNode;
    }


    private static GISViewInfo parseView(Node node) {
        GISViewInfo info = null;
        int id = 0;
        Map<String, String> params = null;
        String title = "";
        String service = "";

        id = parseId(node);
        params = parseParameters(node);

        final NodeList elements = node.getChildNodes();
        for (int j = 0, m = elements.getLength(); j < m; j++) {
            final Node elementNode = elements.item(j);
            final String elementName = elementNode.getNodeName();

            if (TAG_TITLE.equals(elementName)) {
                title = elementNode.getTextContent();
            } else if (TAG_VIEW_SERVICE.equals(elementName)) {
                service = elementNode.getTextContent();
            }
        }

        //we have a "correct" view
        if (id > -1) {
            info = new GISViewInfo(id, service, title, params);
        }

        return info;
    }



    // global actions ----------------------------------------------------------

    public static synchronized void encodeLayers(final Document doc,final MapContext context) {
        final Node root = doc.getElementsByTagName(TAG_LAYERS).item(0);

        //we clear the layers list
        //TODO avoid doing so by using a more accurate listener method
        final NodeList children = root.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            root.removeChild(children.item(i));
        }

        //create layer nodes
        final List<MapLayer> layers = new ArrayList<MapLayer>(context.layers());
        for (final MapLayer layer : layers) {
            final Element layerNode = doc.createElement(TAG_LAYER);
            DOMencodeLayer(doc, layer, layerNode, null);
            root.appendChild(layerNode);
        }

    }

    public static synchronized void encodeViews(final Document doc, List<GISViewInfo> views){

        final Node root = doc.getElementsByTagName(TAG_VIEWS).item(0);

        //we clear the view list
        final NodeList children = root.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            root.removeChild(children.item(i));
        }

        //create view nodes
        for (final GISViewInfo view : views) {
            final Element layerNode = doc.createElement(TAG_VIEW);
            encodeView(doc, view, layerNode);
            root.appendChild(layerNode);
        }

    }
    
}
