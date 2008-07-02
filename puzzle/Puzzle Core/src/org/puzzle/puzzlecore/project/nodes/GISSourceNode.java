/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.puzzle.puzzlecore.project.GISProject;

/**
 *
 * @author Administrateur
 */
public class GISSourceNode extends AbstractNode implements FileChangeListener{
    
    private static final String ICON_PATH = "org/puzzle/puzzlecore/project/source.png";
    private static final Image ICON = Utilities.loadImage(ICON_PATH, true);
    
    private final GISProject project;
    
    public GISSourceNode(DataFolder folder, GISProject project) throws DataObjectNotFoundException{
        super (createChildren(folder));
//                , new FilterNode.Children (node),
//                    //The projects system wants the project in the Node's lookup.
//                    //NewAction and friends want the original Node's lookup.
//                    //Make a merge of both
//                    new ProxyLookup (new Lookup[] { Lookups.singleton(project),
//                    node.getLookup() }));
        
        this.project = project;
        folder.getPrimaryFile().addFileChangeListener(this);
    }
    
    @Override
    public Image getIcon(int type) {
        return ICON;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    @Override
    public String getDisplayName() {
        return "Sources";
    }
    

    public void fileFolderCreated(FileEvent arg0) {
    }

    public void fileDataCreated(FileEvent event) {
        FileObject file = event.getFile();
        try{
            DataObject data = DataObject.find(file);
            getChildren().add(new Node[]{data.getNodeDelegate()});
        }catch(Exception e){e.printStackTrace();}
        
    }

    public void fileChanged(FileEvent arg0) {
    }

    public void fileDeleted(FileEvent arg0) {
    }

    public void fileRenamed(FileRenameEvent arg0) {
    }

    public void fileAttributeChanged(FileAttributeEvent arg0) {
    }
    
    private static final synchronized Children createChildren(DataFolder folder){
        
        Children childs = new Children.Array();
                
        DataObject[] datas = folder.getChildren();
        for(DataObject data : datas){
            childs.add(new Node[]{data.getNodeDelegate()});
        }
        
        return childs;
    }
    
}
