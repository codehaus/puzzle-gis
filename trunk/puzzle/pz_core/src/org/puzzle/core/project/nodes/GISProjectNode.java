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
package org.puzzle.core.project.nodes;

import java.awt.Image;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.puzzle.core.project.GISProject;

/**
 * This class provides a {@link org.openide.nodes.Node} for the 
 * {@link org.puzzle.core.project.GISProject}.
 * <br>
 * This is the root node used in the the "Projects" window provided
 * by NetBeans.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.nodes.AbstractNode
 */
public class GISProjectNode extends AbstractNode{
    
    private final String ICON_PATH = "org/puzzle/core/project/boussole.png";
    private final ImageIcon ICON = new ImageIcon(Utilities.loadImage(ICON_PATH, true));
    
    private final GISProject project;
    
    /**
     * Constructor.<br>
     * This constructor only initialize a new array to contain project node
     * childrens ("src", "doc", "map"). These children are not created at
     * this time. This is deported to 
     * {@link org.puzzle.core.project.GISLogicalView#createLogicalView}.
     * @param   project The current {@code GISProject}.
     */
    public GISProjectNode(GISProject project){
        super(new Children.Array(), Lookups.singleton(project) );
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
            CommonProjectActions.setAsMainProjectAction()
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
