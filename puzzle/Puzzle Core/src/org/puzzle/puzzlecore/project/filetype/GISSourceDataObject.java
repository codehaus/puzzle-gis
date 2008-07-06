/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.filetype;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;
import org.puzzle.puzzlecore.project.source.GISSourceService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GISSourceDataObject extends XMLDataObject {

    private GISSource source = null;

    public GISSourceDataObject(FileObject pf, GISSourceDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));

        GISProject prj = (GISProject) FileOwnerQuery.getOwner(getPrimaryFile());
        GISSource src = getSource();
        prj.addGISSource(src);
    }

    public GISSource getSource(){

        if(source == null){

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

                NodeList ids = gisDoc.getElementsByTagName("id");
                int id = Integer.valueOf( ids.item(0).getTextContent() );

                NodeList serviceIds = gisDoc.getElementsByTagName("serviceid");
                String serviceId = serviceIds.item(0).getTextContent();

                NodeList urls = gisDoc.getElementsByTagName("parameters");
                Map<String,String> parameters = new HashMap<String,String>();

                for(int i=0, n = urls.getLength(); i<n; i++){
                    org.w3c.dom.Node paramsNode = urls.item(i);
                    NodeList params = paramsNode.getChildNodes();

                    for(int j=0, l=params.getLength(); j<l; j++){
                        org.w3c.dom.Node singleParam = params.item(j);
                        String key = singleParam.getNodeName();
                        String value = singleParam.getTextContent();
                        if(key != null && value != null){
                            parameters.put(key, value);
                        }
                    }
                }

                GISSourceService service = getSourceService(serviceId);

                if(service != null){
                    source = service.restoreSource(parameters,id);
                }

            }

        }

        return source;

    }

    @Override
    protected Node createNodeDelegate() {
        return new GISSourceDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    protected static final GISSourceService getSourceService(String id){
        Collection<? extends GISSourceService> services = Lookup.getDefault().lookupAll(GISSourceService.class);

        for(GISSourceService service : services){
            if(service.getIdentifier().equals(id)) return service;
        }

        return null;
    }


}
