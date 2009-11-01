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
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;
import org.geotoolkit.map.ContextListener;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.CollectionChangeEvent;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.FileOwnerQuery;

import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.XMLDataObject;
import org.openide.nodes.CookieSet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.xml.XMLUtil;

import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.project.view.GISViewInfo;
import org.puzzle.core.resources.MessageBundle;
import org.puzzle.core.view.RenderingService;
import org.puzzle.core.view.ViewComponent;

import org.w3c.dom.Document;
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

    public static final String STATE_PROPERTY = "state";

    private final DefaultContextListener contextListener = new DefaultContextListener();
    private final SavingThread saver = new SavingThread();
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);

    private MapContext context = null;
    private boolean needDataSave = false;
    private boolean needViewSave = false;

    private GISContextState state = GISContextState.UNLOADED;

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

        List<GISViewInfo> infos = Encoder.parseViews(getDOM());

        for(GISViewInfo info : infos){
            createView(info, false);
        }

    }

    @Override
    public void dispose() {
        //close all views
        for(GISView view : getLookup().lookupAll(GISView.class)){
            removeView(view);
        }
        super.dispose();
    }

    private void setState(GISContextState state){
        if(state == null) throw new NullPointerException("State can not be null.");

        final GISContextState oldState = this.state;
        this.state = state;
        firePropertyChange(STATE_PROPERTY, oldState, this.state);
    }

    public GISContextState getState(){
        return state;
    }

    /**
     * This method is used to retrieve the {@code MapContext} associated with
     * the {@code GISContextDataObject}. If not context is currently associated,
     * it creates a new one and intialize it with layers described in the 
     * XML document.
     * @return  The {@code MapContext} associated.
     */
    public synchronized MapContext getContext() {

        if (context == null) {
            final ProgressHandle handle = ProgressHandleFactory.createHandle(MessageBundle.getString("loadingContext") +" : " + getPrimaryFile().getName().replaceAll(".xml", ""));
            handle.start(100);
            handle.setInitialDelay(1);
            handle.switchToIndeterminate();
            try{
                // at this point the task is finished and removed from status bar
                // it's not realy necessary to count all the way to the limit, finish can be called earlier.
                // however it has to be called at the end of the processing.

                context = Encoder.parseContext(getDOM(),getPrimaryFile().getName().replaceAll(".xml", ""),getGISSources());
                context.addContextListener(contextListener);
            }finally{
                handle.finish();
            }
            
            setState(GISContextState.LOADED);
        }

        return context;
    }

    public void createView(RenderingService service){
        if(service != null){
            //TODO replace the math random by a valid id finder
            final GISViewInfo info = new GISViewInfo((int)(Math.random()*100000), service.getIdentifier(),"", new HashMap<String, String>());
            info.setTitle(getContext().getDescription().getTitle().toString() + " - "+ service.getIdentifier());
            createView(info,true);
            setNeedViewSave(true);
        }
    }

    private void createView(GISViewInfo info, boolean open){
        final GISView view = new GISView(this, info);

        info.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setNeedViewSave(true);
            }
        });

        content.add(view);

        if(open){
            ViewComponent comp = view.getComponent(true);
            if(comp != null && !comp.isOpened()) comp.open();
        }
    }

    public void removeView(GISView view){
        if(view.isDisplayed()){
            final ViewComponent comp = view.getComponent(false);
            if(comp != null){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        comp.close();
                    }
                });
            }
        }
        content.remove(view);
        setNeedViewSave(true);
    }

    @Override
    protected org.openide.nodes.Node createNodeDelegate() {
        return new GISContextDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(getCookieSet().getLookup(),lookup);
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


    private class DefaultContextListener implements ContextListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setNeedDataSave(true);
//            DOMencodeLayers();
        }

        @Override
        public void layerChange(CollectionChangeEvent<MapLayer> evt) {
            setNeedDataSave(true);
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

    private void setNeedDataSave(boolean b) {
        needDataSave = b;
        if (needDataSave) {
            saver.wake();
        }

    }

    private void setNeedViewSave(boolean b) {
        needViewSave = b;
        if (needViewSave) {
            saver.wake();
        }

    }

    private boolean needDataSaving() {
        return needDataSave;
    }

    private boolean needViewSaving() {
        return needViewSave;
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
                while (needDataSaving() || needViewSaving()) {
                    final Document dom = getDOM();

                    if(needDataSaving()){
                        setNeedDataSave(false);

                        Encoder.encodeLayers(dom, getContext());
                    }

                    if(needViewSaving()){
                        setNeedViewSave(false);

                        final Collection<? extends GISView> views = getLookup().lookupAll(GISView.class);
                        final List<GISViewInfo> infos = new ArrayList<GISViewInfo>();
                        for(GISView view : views){
                            infos.add(view.getInfo());
                        }
                        Encoder.encodeViews(dom,infos);
                    }

                    
                    DOMsave(dom);
                    try {
                        //wait at least 30seconds before making a new save.
                        //this avoid consuming to much cpu while the user is making many changes
                        sleep(30000);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
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
