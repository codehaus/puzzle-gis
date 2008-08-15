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
package org.puzzle.puzzlecore.project.filetype;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xml.serialize.XMLSerializer;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.xml.XMLUtil;
import org.puzzle.puzzlecore.context.RichMapContext;
import org.puzzle.puzzlecore.context.RichMapLayer;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
 * @author  Johann Sorel
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
    
    private final ContextListener contextListener = new ContextListener();
    private MapContext context = null;


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
        
        Project prj = FileOwnerQuery.getOwner(pf);
        if(prj != null && prj instanceof GISProject){
            GISProject gisprj = (GISProject) prj;
            gisprj.addContext(getContext());
        }
        
        
        
        CookieSet cookies = getCookieSet();
        cookies.add((org.openide.nodes.Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    /**
     * This method is used to retrieve the {@code MapContext} associated with
     * the {@code GISContextDataObject}. If not context is currently associated,
     * it creates a new one and intialize it with layers described in the 
     * XML document.
     * @return  The {@code MapContext} associated.
     */
    public MapContext getContext(){
        
        if(context == null){
            context = parseContext(getDOM());
            context.addMapLayerListListener(contextListener);
            context.addPropertyChangeListener(contextListener);
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
    
    private Collection<? extends GISSource> getGISSources(){
        GISProject prj = (GISProject) FileOwnerQuery.getOwner(getPrimaryFile());
        return prj.getLookup().lookupAll(GISSource.class);
    }
    
    private MapContext parseContext(Document gisDoc) {
        context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        context.setTitle(getPrimaryFile().getName().replaceAll(".xml", ""));
        
        if (gisDoc != null) {
            Node rootNode = gisDoc.getFirstChild();

            NodeList layerNodes = gisDoc.getElementsByTagName(TAG_LAYER);

            for (int i = 0, n = layerNodes.getLength(); i < n; i++) {
                RichMapLayer layer = parseLayer(layerNodes.item(i));
                context.addLayer(layer);
            }
        }

        return context;
    }
    
    private RichMapLayer parseLayer(Node node){
        RichMapLayer layer = null;
        int id = 0;
        Map<String,String> params = null;
        String title = "";
                
        NodeList elements = node.getChildNodes();
        for (int j = 0, m = elements.getLength(); j < m; j++) {
            Node elementNode = elements.item(j);
            String elementName = elementNode.getNodeName();
            
            if(TAG_LAYER_SOURCE.equals(elementName)){
                id = parseSourceId(elementNode);
                params = parseSourceParams(elementNode);
            }else if(TAG_LAYER_TITLE.equals(elementName)) {
                title = elementNode.getTextContent();
            }
        }

        //we have a "correct" layer
        if (id > 0) {
            Collection<? extends GISSource> sources = getGISSources();
            for (GISSource src : sources) {
                if (src.getID() == id) {
                    layer = src.createLayer(params);
                    layer.setTitle(title);
                }
            }
        }

        return layer;
    }
    
    private int parseSourceId(Node sourceNode){
        int id = 0;
        NodeList elements = sourceNode.getChildNodes();
        for(int i = 0, n = elements.getLength(); i < n; i++) {
            Node elementNode = elements.item(i);
            String elementName = elementNode.getNodeName();
            
            if (TAG_LAYER_SOURCE_ID.equals(elementName)) {
                id = Integer.valueOf(elementNode.getTextContent());
                break;
            }
        }

        return id;
    }
    
    private Map<String,String> parseSourceParams(Node sourceNode){
        Map<String,String> parameters = new HashMap<String, String>();
        NodeList elements = sourceNode.getChildNodes();
        for(int i = 0, n = elements.getLength(); i < n; i++) {
            Node elementNode = elements.item(i);
            String elementName = elementNode.getNodeName();
            
            if (TAG_LAYER_SOURCE_PARAMS.equals(elementName)) {
                NodeList params = elementNode.getChildNodes();
                for(int j = 0, m = params.getLength(); j < m; j++) {
                    Node paramNode = params.item(j);
                    if(paramNode instanceof Element){
                        parameters.put(paramNode.getNodeName(), paramNode.getTextContent());
                    }
                }
                break;
            }
        }

        return parameters;
    }
    
    private void encodeLayers(){
        Document doc = getDOM();
        Node root = doc.getElementsByTagName(TAG_LAYERS).item(0);
        
        //we clear the layers list
        //TODO avoid doing so by using a more accurate listener method
        NodeList children = root.getChildNodes();
        for(int i=children.getLength()-1 ; i>=0; i--){
            root.removeChild(children.item(i));
        }
        
        //create layer nodes
        MapLayer[] layers = context.getLayers();
        for(MapLayer layer : layers){
            //check if we can save the layer
            if(layer instanceof RichMapLayer){
                RichMapLayer rich = (RichMapLayer) layer;
                Element layerNode = doc.createElement(TAG_LAYER);
                                
                //store layer title
                String title = rich.getTitle();
                Element layerTitle = doc.createElement(TAG_LAYER_TITLE);
                layerTitle.setTextContent(title);
                layerNode.appendChild(layerTitle);
                
                //store layer source
                int id = rich.getLayerSource().getSourceId();
                Map<String,String> params = rich.getLayerSource().getParameters();
                Element layerSource = doc.createElement(TAG_LAYER_SOURCE);
                Element sourceId = doc.createElement(TAG_LAYER_SOURCE_ID);
                Element sourceParams = doc.createElement(TAG_LAYER_SOURCE_PARAMS);
                sourceId.setTextContent(String.valueOf(id));
                Iterator<String> keys = params.keySet().iterator();
                while(keys.hasNext()){
                    String key = keys.next();
                    Element aParam = doc.createElement(key);
                    aParam.setTextContent(params.get(key));
                    sourceParams.appendChild(aParam);
                }
                layerSource.appendChild(sourceId);
                layerSource.appendChild(sourceParams);
                layerNode.appendChild(layerSource);
                
                //store features filter
                //TODO
                
                //store layer style
                //TODO
                
                root.appendChild(layerNode);
            }
        }

        try {
            OutputStream output = getPrimaryFile().getOutputStream(FileLock.NONE);
            XMLUtil.write(doc, output, "UTF-8");
            output.flush();
            output.close();
        } catch (FileAlreadyLockedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

//        setModified(true);
//
//        SaveCookie sc = getCookie(SaveCookie.class);
//        try {
//            sc.save();
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        
//        save(doc);
    }
    
    private void save(Document doc){
        try {
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputByteStream(getPrimaryFile().getOutputStream());
            serializer.serialize(doc);
        } catch (FileAlreadyLockedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private class ContextListener implements MapLayerListListener,PropertyChangeListener{

        @Override
        public void layerAdded(MapLayerListEvent evt) {
            encodeLayers();
        }

        @Override
        public void layerRemoved(MapLayerListEvent evt) {
            encodeLayers();
        }

        @Override
        public void layerChanged(MapLayerListEvent evt) {
            encodeLayers();
        }

        @Override
        public void layerMoved(MapLayerListEvent evt) {
            encodeLayers();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        }

    }

}
