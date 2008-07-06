/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.xerces.dom.DocumentImpl;
import org.geotools.feature.visitor.CollectionUtil;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.puzzle.puzzlecore.project.filetype.GISSourceDataLoader;
import org.puzzle.puzzlecore.project.filetype.GISSourceDataObject;
import org.puzzle.puzzlecore.project.source.GISSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author johann sorel
 */
public class GISProject implements Project { 

    public static final String MAP_DIR = "map";
    public static final String DOC_DIR = "doc";
    public static final String SRC_DIR = "src";
    
    private final FileObject projectDir;
    private final ProjectState state;
    private final LogicalViewProvider logicalView = new GISLogicalView(this);
    
    
    private final InstanceContent lookUpContent = new InstanceContent();
    private final Lookup lookUp = new AbstractLookup(lookUpContent);
    
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

    public int getNextSourceID() {
        
        Collection<? extends GISSource> sources = getLookup().lookupAll(GISSource.class);
        
        number_loop:
        for(int i=1; i<Integer.MAX_VALUE; i++){
            for(GISSource src : sources){
                if(src.getID() == i) continue number_loop;
            }
            return i;
        }
        
        return 1;
    }
    
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    public Lookup getLookup() {
        return lookUp;
    }
    
    public void addGISSource(GISSource src){
        if(checkSourceExist(src)) return;
        
        if(src != null){
            lookUpContent.add(src);
        }
    }
    
    public void appendGISSource(GISSource src){
        if(checkSourceExist(src)) return;
        GISSource source = createPersistantSource(src);
        if(source != null) lookUpContent.add(source);
    }
    
    private boolean checkSourceExist(GISSource source){
         
        Collection<? extends GISSource> sources = getLookup().lookupAll(GISSource.class);
        
        //check is the object is already in the lookup
        if(sources.contains(source)){
            //source already in the project
            return true;
        }
        
        Map<String,String> sourceParameters = source.getParameters();
        //check if another source have the same parameters
        for(GISSource src : sources){
            Map<String,String> srcParameters = src.getParameters();

            if(srcParameters.equals(sourceParameters)){
                return true;
            }
        }
        
        
        return false;
    }
    
    private GISSource createPersistantSource(GISSource source){
        
        try{            
            Document doc = fill(source);
            System.out.println("PATH >>>>>> " + getSourceFolder(true).getPath() + File.separator + source.getTitle() + ".xml");
            File xml = new File(getSourceFolder(true).getPath() + File.separator + source.getTitle() + ".xml");
            transformerXml(doc, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return source;
    }
    
    public Document fill(GISSource source) throws Exception {

        //try to read the xml file
        DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        // création d'un constructeur de documents
        DocumentBuilder constructeur = fabrique.newDocumentBuilder();

        Document gisDoc = constructeur.newDocument();
        
        gisDoc.setXmlVersion("1.0");
        gisDoc.setXmlStandalone(true);

        // Création de l'arborescence du DOM
        Element racine = gisDoc.createElement("root");
        racine.setAttribute("xmlns","gissource");
        gisDoc.appendChild(racine);
        


        //if file is valid
        if (gisDoc != null) {
            org.w3c.dom.Node rootNode = gisDoc.getFirstChild();

            org.w3c.dom.Node idNode = gisDoc.createElement("id");
            idNode.setTextContent(String.valueOf(source.getID()));
            rootNode.appendChild(idNode);

            org.w3c.dom.Node serviceIDNode = gisDoc.createElement("serviceid");
            serviceIDNode.setTextContent(source.getServiceName());
            rootNode.appendChild(serviceIDNode);

            org.w3c.dom.Node parametersNode = gisDoc.createElement("parameters");
            rootNode.appendChild(parametersNode);


            Map<String, String> parameters = source.getParameters();
            Set<String> keys = parameters.keySet();

            for (String key : keys) {
                org.w3c.dom.Element paramNode = gisDoc.createElement(key);
                String value = parameters.get(key);
//                paramNode.setNodeValue(key);
//                paramNode.setAttribute(key, value);
                paramNode.setTextContent(value);
                parametersNode.appendChild(paramNode);
            }
        }
        
        return gisDoc;
    }
    
    public static void transformerXml(Document document, File output) {
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

    
    FileObject getMapFolder(boolean create) {
        FileObject result = projectDir.getFileObject(MAP_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(MAP_DIR);
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
            }
        }
        return result;
    }

    FileObject getDocFolder(boolean create) {
        FileObject result = projectDir.getFileObject(DOC_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(DOC_DIR);
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
            }
        }
        return result;
    }
    
    FileObject getSourceFolder(boolean create) {
        FileObject result = projectDir.getFileObject(SRC_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(SRC_DIR);
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
            }
        }
        return result;
    }
        
    private Properties loadProperties() {
        FileObject fob = projectDir.getFileObject(GISProjectFactory.PROJECT_DIR +
            "/" + GISProjectFactory.PROJECT_PROPFILE);
        Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                ErrorManager.getDefault().notify(e);
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
        
        public String[] getSupportedActions() {
            return new String[0];
        }

        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            //do nothing
        }

        public boolean isActionEnabled(String string, Lookup lookup) throws IllegalArgumentException {
            return false;
        }
    }

    /** Implementation of project system's ProjectInformation class */
    private final class Info implements ProjectInformation {
        
        private final String ICON_PATH = "org/puzzle/puzzlecore/project/boussole.png";
        private final ImageIcon ICON = new ImageIcon(Utilities.loadImage(ICON_PATH, true));
        private final PropertyChangeSupport support = new PropertyChangeSupport(this);
        
        public String getName() {
            return projectDir.getName();
        }

        public String getDisplayName() {
            return getProjectDirectory().getName();
        }

        public ImageIcon getImageIcon() {
            return ICON;
        }

        public Icon getIcon() {
            return ICON;
        }

        public Project getProject() {
            return GISProject.this;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }

    }
    
    
    
}
