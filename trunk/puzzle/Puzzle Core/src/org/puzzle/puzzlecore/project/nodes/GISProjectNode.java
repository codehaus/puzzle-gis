/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.nodes;

import java.awt.Image;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.project.GISProject;

/**
 *
 * @author Administrateur
 */
public class GISProjectNode extends AbstractNode{

    private final String ICON_PATH = "org/puzzle/puzzlecore/project/boussole.png";
    private final ImageIcon ICON = new ImageIcon(Utilities.loadImage(ICON_PATH, true));
    
    
    private final GISProject project;
    
    public GISProjectNode(GISProject project){
        super(new Children.Array());
        this.project = project;
    }

    @Override
    public Image getIcon(int arg0) {
        return ICON.getImage();
    }

    @Override
    public Image getOpenedIcon(int arg0) {
        return ICON.getImage();
    }

    @Override
    public String getDisplayName() {
        ProjectInformation info = project.getLookup().lookup(ProjectInformation.class);
        return info.getDisplayName();
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
            CommonProjectActions.closeProjectAction(),
             CommonProjectActions.deleteProjectAction(),
            CommonProjectActions.setAsMainProjectAction(),
             CommonProjectActions.setProjectConfigurationAction()
        };
    }

    

    
    
    
    
//    private synchronized  static Children createChildren(GISProject project){
//        Children child = new Children.Array();
//        GISMapNode map = new GISMapNode(project);
//        
//        FileObject projectFolder = project.getProjectDirectory();
//        FileObject mapFolder = getFolder(projectFolder, MAP_FOLDER);
//        FileObject docFolder = getFolder(projectFolder, DOC_FOLDER);
//        FileObject srcFolder = getFolder(projectFolder, SRC_FOLDER);
//                
//        GISDocNode docs = new GISDocNode(docFolder);
//        GISSourceNode sources = new GISSourceNode(project);
//        
//        child.add(new Node[]{map,docs,sources});
//        
//        return child;
//    }
//    
//    private synchronized  static FileObject getFolder(final FileObject projectFolder, final String name){
//        FileObject folder = projectFolder.getFileObject(name);
//        
//        if(folder == null){
//            try {
//                projectFolder.createFolder(name);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//            folder = projectFolder.getFileObject(name);
//        }
//        
//        return folder;
//    }
    
}
