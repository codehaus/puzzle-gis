/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.filetype;

import java.io.IOException;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;

public class GISContextDataObject extends MultiDataObject {

    public GISContextDataObject(FileObject pf, GISContextDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }

    
    public MapContext getContext(){
        return new DefaultMapContext(DefaultGeographicCRS.WGS84);
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
