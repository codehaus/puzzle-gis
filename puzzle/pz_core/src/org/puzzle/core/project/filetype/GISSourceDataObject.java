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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;

import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is used to represent XML files describing sources for the
 * project. It extends {@code XMLDocument} which provides main functionalities
 * for using XML documents.<br>
 * This class provides special support for understanding and using 
 * sources files.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.loaders.XMLDataObject
 */
public class GISSourceDataObject extends XMLDataObject {

    private GISSource source = null;

    /**
     * Constructor.
     * This contructor creates a {@code GISSourceDataObject} and make it
     * openable in an editor.
     * @param   pf      The {@code FileObject} representing XML document.
     * @param   loader  The loader to use for this {@code DataObject}.
     * @throws  org.openide.loaders.DataObjectExistsException
     * @throws  java.io.IOException
     */
    public GISSourceDataObject(FileObject pf, GISSourceDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        final CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        
    }

    /**
     * This method is used to retrieve the {@code GISSource} associated with
     * the {@code GISSourceDataObject}. If not source is currently associated,
     * it creates a new one and intialize it with parameters in the XML 
     * document.
     * @return  The {@code MapContext} associated.
     */
    public GISSource getSource(){

        if(source == null){

            ProgressHandle handle = ProgressHandleFactory.createHandle(Utilities.getString("loadingSource") +" : " + getName());
            handle.start(100);
            handle.switchToIndeterminate();

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
                final org.w3c.dom.Node rootNode = gisDoc.getFirstChild();

                final NodeList ids = gisDoc.getElementsByTagName("id");
                int id = 0;
                if(ids.getLength()>0){
                    //there is a set ID
                    id = Integer.valueOf( ids.item(0).getTextContent() );
                }

                String serviceId = "unknowned";
                final NodeList serviceIds = gisDoc.getElementsByTagName("serviceid");
                if(serviceIds.getLength()>0){
                    //there is a set service ID
                    serviceId = serviceIds.item(0).getTextContent();
                }

                final NodeList urls = gisDoc.getElementsByTagName("parameters");
                final Map<String,String> parameters = new HashMap<String,String>();

                for(int i=0, n = urls.getLength(); i<n; i++){
                    org.w3c.dom.Element paramsNode = (Element) urls.item(i);
                    final NodeList params = paramsNode.getElementsByTagName("parameter");

                    for(int j=0, l=params.getLength(); j<l; j++){
                        final org.w3c.dom.Element singleParam = (Element) params.item(j);
                        final String key = singleParam.getAttribute("name");
                        final String value = singleParam.getTextContent();
                        if(key != null && value != null){
                            parameters.put(key, value);
                        }
                    }
                }

                final GISSourceService service = getSourceService(serviceId);

                if(service != null){
                    final GISSourceInfo info = new GISSourceInfo(id, serviceId, parameters);
                    source = service.restoreSource(info);
                }

            }

            handle.finish();
        }

        return source;

    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
    }

    @Override
    public void dispose() {
        Project prj = FileOwnerQuery.getOwner(getPrimaryFile());
        if(prj != null && prj instanceof GISProject){
            GISProject gisprj = (GISProject) prj;
            gisprj.removeGISSource(getSource());
        }
        super.dispose();
    }

    @Override
    protected Node createNodeDelegate() {
        return new GISSourceDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    /**
     * Get the {@code GISSourceService} which will be used to manage this kind
     * of source if it exists.
     * @param   id    The identifier of the service needed.
     * @return  The {@code GISSourceService} if it exists or {@code null}.
     */
    protected static final GISSourceService getSourceService(String id){
        Collection<? extends GISSourceService> services = Lookup.getDefault().lookupAll(GISSourceService.class);

        for(GISSourceService service : services){
            if(service.getIdentifier().equals(id)) return service;
        }

        return null;
    }


}
