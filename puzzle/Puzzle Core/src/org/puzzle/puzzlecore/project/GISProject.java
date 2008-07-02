/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.puzzle.puzzlecore.project.filetype.GISSourceDataObject;
import org.puzzle.puzzlecore.project.source.GISSource;

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
    
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    public Lookup getLookup() {
        return lookUp;
    }
    
    public void addGISSource(GISSource src){
        lookUpContent.add(src);
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
