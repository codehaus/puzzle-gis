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

import java.io.IOException;
import java.util.Collection;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
 * @author  Thomas Bonavia
 * 
 * @see     org.openide.loaders.XMLDataObject
 */
public class GISContextDataObject extends XMLDataObject {

    private final RandomStyleFactory RANDOM_STYLE_FACTORY = new RandomStyleFactory();
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
        
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
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
            
            context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            
            //try to read the xml file
            Document gisDoc = null;
            try {
                gisDoc = getDocument();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            }

            //if file is valid
            if(gisDoc != null){
                org.w3c.dom.Node rootNode = gisDoc.getFirstChild();
                
                NamedNodeMap attributs = rootNode.getAttributes();
//                String val = attributs.getNamedItem("crs").getTextContent();
//                System.out.println("crs = " + val);
                
                
                GISProject prj = (GISProject) FileOwnerQuery.getOwner(getPrimaryFile());
                
//                GISProject prj = Lookup.getDefault().lookup(GISProject.class);
                
                Collection<? extends GISSource> sources = prj.getLookup().lookupAll(GISSource.class);
                
                
                
                NodeList layerNodes = gisDoc.getElementsByTagName("layer");   
                
                for(int i=0,n=layerNodes.getLength(); i<n; i++){
                                        
                    int id = 0;
                    String name = "";
                    org.w3c.dom.Node node = layerNodes.item(i);
                    NodeList params = node.getChildNodes();
                    for(int j=0,m=params.getLength(); j<m;j++){
                        org.w3c.dom.Node paramNode = params.item(j);
                        if(paramNode.getNodeName().equals("sourceid")){
                            id = Integer.valueOf(paramNode.getTextContent());
                        }
                        if(paramNode.getNodeName().equals("name")){
                            name = paramNode.getTextContent();
                        }
                    }
                    
                    //we have a "correct" layer
                    if(id>0){
                        for(GISSource src : sources){
                            if(src.getID() == id){
                                MapLayer layer = src.createLayer(null);
                                layer.setTitle(name);
                                context.addLayer(layer);
                            }
                        }
                    }
                    
                }
            }
            
            context.setTitle(getPrimaryFile().getName().replaceAll(".xml", ""));
            
        }
        
        return context;
    }
    
    
    @Override
    protected Node createNodeDelegate() {
        return new GISContextDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
}
