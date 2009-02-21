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
package org.puzzle.core.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.filetype.GISContextDataObject;
import org.puzzle.core.project.filetype.GISSourceDataObject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.ViewService;

/**
 * This class is the project. the project allow to manage the
 * datas. These datas are not necessary georeferenced datas.
 * It provides support for managing :
 * <ul>
 *  <li>maps (saving {@code MapContext}s...),</li>
 *  <li>sources (adding, removing, organizing),</li>
 *  <li>documents (reports...).</li>
 * </ul>
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.netbeans.api.project.Project
 */
public class GISProject implements Project { 
    /** The name of the folder containing maps files. */
    public static final String MAP_DIR = "map";
    /** The name of the folder containing documents files. */
    public static final String DOC_DIR = "doc";
    /** The name of the folder containing sources files. */
    public static final String SRC_DIR = "src";
    
    private final FileObject projectDir;
    private final ProjectState state;
    private final LogicalViewProvider logicalView = new GISLogicalView(this);
    private final InstanceContent lookUpContent = new InstanceContent();
    private final Lookup lookUp = new AbstractLookup(lookUpContent);
    
    /**
     * Constructor.
     * This constructor initializes the project by adding to its {@code Lookup}
     * <ul>
     *  <li>The project ({@code this}),</li>
     *  <li>The project's state,</li>
     *  <li>The project personal {@code ActionProvider},</li>
     *  <li>The project properties,</li>
     *  <li>The project personal {@code ProjectInformation},</li>
     *  <li>The {@link GISLogicalView} associated to the project.</li>
     * </ul>
     * @param root  The folder containing the project.
     * @param state The state of the project. This state describes if the
     *              the project needs or not to be saved.
     */
    public GISProject(FileObject root, ProjectState state){
        this.projectDir = root;
        this.state = state;
        
        lookUpContent.add(this);
        lookUpContent.add(state);
        lookUpContent.add(new ActionProviderImpl());
        lookUpContent.add(loadProperties());
        lookUpContent.add(new Info());
        lookUpContent.add(logicalView);
        
    }

    private List<Integer> alreadyGiven = new ArrayList<Integer>();

    /**
     * Get an ID for a new source.<br>
     * When adding a source to the project, an ID is allocated to him. This
     * ID is simply an integer raised each time we add a source. This method
     * read each source an get the highest ID. Next it adds 1 to this ID and
     * return the number.
     * @return  An {@code int} representing the ID for the new source.
     */
    public int getNextSourceID() {
        
        number_loop:
        for(int i=1; i<Integer.MAX_VALUE; i++){
            if(alreadyGiven.contains(i))continue;

            for(final GISSource src : getGISSources()){
                if(src.getInfo().getID() == i) continue number_loop;
            }
            alreadyGiven.add(i);
            return i;
        }
        
        return 1;
    }
    
    /**
     * Get the project folder.
     * @return  A {@code FileObject} representing the project folder.
     */
    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    /**
     * Get the {@code Lookup} of the project.
     * @return  The {@code Lookup} of the project.
     */
    @Override
    public Lookup getLookup() {
        return lookUp;
    }
        
    /**
     * Remove a {@code MapContext}, will remove it from the lookup, close all related view and
      * remove it from the contextservice.
     * @param map   The {@code MapContext} to add to the project's {@code Lookup}.
     */
    public void removeContext(MapContext context) {

        //close existing views using this mapcontext
        final ViewService viewService = Lookup.getDefault().lookup(ViewService.class);
        if(viewService != null){
            for(final MapView view : ViewService.getViews()){
                if(view.getContext().equals(context)){
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.close();
                        }
                    });
                }
            }
        }
        
        //finally remove context from project
//        contexts.remove(context);
    }
    
    public Collection<MapContext> getContexts() {
        final Collection<MapContext> contexts = new ArrayList<MapContext>();
       findContext(getMapFolder(true), contexts);
        return Collections.unmodifiableCollection(contexts);
    }
      
    private void findContext(FileObject file, Collection<MapContext> contexts){
         DataObject data = null;
        try {
            data = DataObject.find(file);
        } catch (DataObjectNotFoundException ex) {
        }
         
         if(data != null){
             if(data instanceof DataFolder){
                 FileObject[] childs = file.getChildren();
                 for(FileObject obj : childs){
                     findContext(obj, contexts);
                 }
             }else if(data instanceof GISContextDataObject){
                GISContextDataObject src = (GISContextDataObject) data;
                contexts.add(src.getContext());
             }
         }
    }
    
    public void removeGISSource(final GISSource source) {
        if(source == null) return;
        Collection<MapContext> contexts = getContexts();
        for(MapContext context : contexts){
            List<MapLayer> layers = context.layers();
            for(int i=layers.size()-1; i>=0; i--){
                MapLayer layer = layers.get(i);
                if(layer.getUserPropertie(GISLayerSource.KEY_LAYER_INFO) != null){
                    GISSource ly = ((GISLayerSource)layer.getUserPropertie(GISLayerSource.KEY_LAYER_INFO)).getGISSource();
                    if(ly.equals(source)){
                        context.layers().remove(i);
                    }
                }
            }
        }
    }
    
    /**
     * Add a {@code GISSource} to the project.<br>
     * Contrary to {@link addGISSource(GISSource src)}, this method add a
     * persistant source, saved on the disk.
     * @param src   The {@code GISSource} to add to the project.
     */
    public void registerSource(final String name, final GISSourceInfo info){
        if(checkSourceExist(info)) return;
        createPersistantSource(info, name);
    }

    public Collection<GISSource> getGISSources(){
        final Collection<GISSource> sources = new ArrayList<GISSource>();
        findGISSource(getSourceFolder(true), sources);
        return Collections.unmodifiableCollection(sources);
    }

    private void findGISSource(FileObject file, Collection<GISSource> sources){
        DataObject data = null;
        try {
            data = DataObject.find(file);
        } catch (DataObjectNotFoundException ex) {}

        if (data != null) {
            if (data instanceof DataFolder) {
                final FileObject[] childs = file.getChildren();
                for (final FileObject obj : childs) {
                    findGISSource(obj, sources);
                }
            } else if (data instanceof GISSourceDataObject) {
                final GISSourceDataObject src = (GISSourceDataObject) data;
                sources.add(src.getSource());
            }
        }
    }

    /**
     * This method check if a source already exists in the project's 
     * {@code Lookup}. We can consider a {@code GISSource} to be present in
     * the project by two ways :
     * <ul>
     *  <li>The {@code GISSource} exists in the project's {@code Lookup},</li>
     *  <li>The {@code GISSource} does not exist in the project, but another
     *      {@code GISSource} owns the same parameters, so we consider that
     *      the two sources are identicals.
     * </ul>
     * @param   source    The {@code GISSource} we want to test.
     * @return  A {@code boolean} :
     * <ul>
     *  <li>{@code true} : The {@code GISSource} already exists in the project,</li>
     *  <li>{@code false}: The {@code GISSource} is not yet in the project.</li>
     * </ul>
     */
    private boolean checkSourceExist(GISSourceInfo info){
        final Collection<GISSource> sources = getGISSources();
        //check is the object is already in the lookup
        if(sources.contains(info)){
            //source already in the project
            return true;
        }
        
        final Map<String,String> sourceParameters = info.getParameters();
        //check if another source have the same parameters
        for(final GISSource src : sources){
            final Map<String,String> srcParameters = src.getInfo().getParameters();

            if(srcParameters.equals(sourceParameters)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * This method is used to create a persistant source to the project.
     * It is used by appendGISSource().
     * Indeed, when creating a persistant source, we first add the source
     * to  the Lookup of the project, then create the file on the disk.
     * This method is used to create the file.
     */
    private void createPersistantSource(GISSourceInfo info, final String name){
        //allow an ID to this GIS Source
        info = new GISSourceInfo(getNextSourceID(), info.getServiceName(), info.getParameters());

        try{            
            final Document doc = fill(info);
            /*
             * We need to use FileUtil.toFile().
             * Indeed, if we don't, getSourceFolder(true).getPath() don't return
             * an absolute path, which generates an error.
             */
            final File xml = new File(FileUtil.toFile(getSourceFolder(true)).getAbsolutePath() + File.separator + name + ".xml");
            transformerXml(doc, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Fill the source document with the suitable datas.
     * @param   source    The {@code GISSource} to save on the disk.
     * @return  A DOM {@code Document} containing the XML datas.
     * @throws  java.lang.Exception
     */
    private Document fill(GISSourceInfo info) throws Exception {
        //try to read the xml file
        final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        // création d'un constructeur de documents
        final DocumentBuilder constructeur = fabrique.newDocumentBuilder();

        final Document gisDoc = constructeur.newDocument();
        
        gisDoc.setXmlVersion("1.0");
        gisDoc.setXmlStandalone(true);

        // Création de l'arborescence du DOM
        final Element racine = gisDoc.createElement("root");
        racine.setAttribute("xmlns","gissource");
        gisDoc.appendChild(racine);

        //if file is valid
        if (gisDoc != null) {
            final org.w3c.dom.Node rootNode = gisDoc.getFirstChild();

            final org.w3c.dom.Node idNode = gisDoc.createElement("id");
            idNode.setTextContent(String.valueOf(info.getID()));
            rootNode.appendChild(idNode);

            final org.w3c.dom.Node serviceIDNode = gisDoc.createElement("serviceid");
            serviceIDNode.setTextContent(info.getServiceName());
            rootNode.appendChild(serviceIDNode);

            final org.w3c.dom.Node parametersNode = gisDoc.createElement("parameters");
            rootNode.appendChild(parametersNode);

            final Map<String, String> parameters = info.getParameters();
            final Set<String> keys = parameters.keySet();

            for (final String key : keys) {
                final org.w3c.dom.Element paramNode = gisDoc.createElement("parameter");
                final String value = parameters.get(key);
//                paramNode.setNodeValue(key);
                paramNode.setAttribute("name", key);
                paramNode.setTextContent(value);
                parametersNode.appendChild(paramNode);
            }
        }
        
        return gisDoc;
    }
    
    /**
     * Convert a DOM document to a file on the disk.
     * @param document  The DOM document do save.
     * @param output    The file where to write the DOM document.
     */
    private void transformerXml(Document document, File output) {
        try {
            // Création de la source DOM
            Source source = new DOMSource(document);

            // Création du fichier de sortie
            Result resultat = new StreamResult(output);

            // Configuration du transformer
            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            
            // Transformation
            transformer.transform(source, resultat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the maps folder.
     * @param   create    Create the folder if does not exists ?
     * @return  The {@code FileObject} representing the maps folder.
     */
    public FileObject getMapFolder(boolean create) {
        FileObject result = projectDir.getFileObject(MAP_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(MAP_DIR);
            } catch (IOException ioe) {
                Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to create folder "+MAP_DIR, ioe);
            }
        }
        return result;
    }

    /**
     * Get the documents folder.
     * @param   create    Create the folder if does not exists ?
     * @return  The {@code FileObject} representing the documents folder.
     */
    public FileObject getDocFolder(boolean create) {
        FileObject result = projectDir.getFileObject(DOC_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(DOC_DIR);
            } catch (IOException ioe) {
                Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to create folder "+DOC_DIR, ioe);
            }
        }
        return result;
    }
    
    /**
     * Get the sources folder.
     * @param   create    Create the folder if does not exists ?
     * @return  The {@code FileObject} representing the sources folder.
     */
    public FileObject getSourceFolder(boolean create) {
        FileObject result = projectDir.getFileObject(SRC_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(SRC_DIR);
            } catch (IOException ioe) {
                Logger.getLogger(GISProject.class.getName()).log(Level.SEVERE, 
                        "Unable to create folder "+SRC_DIR, ioe);
            }
        }
        return result;
    }
    
    /**
     * Load project properties.
     * @return  A {@code Properties} containing project properties.
     */
    private Properties loadProperties() {
        FileObject fob = projectDir.getFileObject(GISProjectFactory.PROJECT_DIR +
            "/" + GISProjectFactory.PROJECT_PROPFILE);
        Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                Logger.getLogger(GISProject.class.getName()).log(Level.WARNING, 
                        "Unable to load project properties",e);
            }
        }
        return properties;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    private static class NotifyProperties extends Properties {
        private final ProjectState state;
        NotifyProperties (ProjectState state) {
            this.state = state;
        }

        @Override
        public Object put(Object key, Object val) {
            Object result = super.put (key, val);
            if (((result == null) != (val == null)) || (result != null &&
                val != null && !val.equals(result))) {
                state.markModified();
            }
            return result;
        }
    }
    
    private final class ActionProviderImpl implements ActionProvider {
        
        @Override
        public String[] getSupportedActions() {
            return new String[0];
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            //do nothing
        }

        @Override
        public boolean isActionEnabled(String string, Lookup lookup) throws IllegalArgumentException {
            return false;
        }
    }

    /* Implementation of project system's ProjectInformation class */
    private final class Info implements ProjectInformation {
        
        private final String ICON_PATH = "org/puzzle/core/project/boussole.png";
        private final ImageIcon ICON = new ImageIcon(ImageUtilities.loadImage(ICON_PATH, true));
        private final PropertyChangeSupport support = new PropertyChangeSupport(this);
        
        @Override
        public String getName() {
            return projectDir.getName();
        }

        @Override
        public String getDisplayName() {
            return getProjectDirectory().getName();
        }

        public ImageIcon getImageIcon() {
            return ICON;
        }

        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public Project getProject() {
            return GISProject.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }

    }

}
