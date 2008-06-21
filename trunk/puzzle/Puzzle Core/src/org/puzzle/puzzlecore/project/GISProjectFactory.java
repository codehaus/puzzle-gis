/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author sorel
 */
public class GISProjectFactory implements ProjectFactory{
    
    public static final String PROJECT_DIR = "gisproject";
    public static final String PROJECT_PROPFILE = "project.properties";
    
    public boolean isProject(FileObject file) {
        FileObject folder = file.getFileObject(PROJECT_DIR);
        if(folder == null) return false;
        return folder.getFileObject(PROJECT_PROPFILE) != null;
    }

    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject (dir) ? new GISProject (dir, state) : null;
    }

    public void saveProject(Project project) throws IOException, ClassCastException {
        FileObject projectRoot = project.getProjectDirectory();
        if (projectRoot.getFileObject(PROJECT_DIR) == null) {
            throw new IOException ("Project dir " + projectRoot.getPath() + " deleted," +" cannot save project");
        }
        //Force creation of folders if it was deleted
        ((GISProject) project).getMapFolder(true);
        ((GISProject) project).getDocFolder(true);
        ((GISProject) project).getSourceFolder(true);

        //Find the properties file gisproject/project.properties,
        //creating it if necessary
        String propsPath = PROJECT_DIR + File.pathSeparator + PROJECT_PROPFILE;
        FileObject propertiesFile = projectRoot.getFileObject(propsPath);
        if (propertiesFile == null) {
            //Recreate the properties file if needed
            propertiesFile = projectRoot.createData(propsPath);
        }

        Properties properties = project.getLookup().lookup (Properties.class);

        File f = FileUtil.toFile(propertiesFile);
        properties.store(new FileOutputStream(f), "NetBeans GIS Project Properties");
    }

    
    
}
