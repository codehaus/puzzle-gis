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
package org.puzzle.puzzlecore.project.nodes;

import java.awt.Image;
import javax.swing.Action;
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
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.SharedClassObject;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.action.source.AddMapContext;
import org.puzzle.puzzlecore.project.filetype.GISContextDataObject;

/**
 * This class provides a {@link org.openide.nodes.Node} for the "map" folder
 * defined in the {@link org.puzzle.puzzlecore.project.GISProject}.
 * This folder is intended to contain all files describing 
 * {@code MapContext}s for each project.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.nodes.AbstractNode
 * @see     org.openide.filesystems.FileChangeListener
 */
public class GISMapNode extends AbstractNode implements FileChangeListener{
    
    private static final String ICON_PATH = "org/puzzle/puzzlecore/project/map.png";
    private static final Image ICON = Utilities.loadImage(ICON_PATH, true);
    
    private final GISProject project;
    
    /**
     * Constructor.
     * @param folder 
     * @param   project   The current {@code GISProject}.
     * @throws  org.openide.loaders.DataObjectNotFoundException
     */
    public GISMapNode(DataFolder folder, GISProject project) throws DataObjectNotFoundException{
        super (createChildren(folder),
                //The projects system wants the project in the Node's lookup.
                //NewAction and friends want the original Node's lookup.
                //Make a merge of both
                new ProxyLookup (new Lookup[] { Lookups.singleton(project) }));
        
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
        return "Maps";
    }
    
    
    @Override
    public void fileFolderCreated(FileEvent arg0) {
    }

    @Override
    public void fileDataCreated(FileEvent event) {
        FileObject file = event.getFile();
        try{
            DataObject data = DataObject.find(file);
            getChildren().add(new Node[]{data.getNodeDelegate()});
        }catch(Exception e){e.printStackTrace();}
        
    }

    @Override
    public void fileChanged(FileEvent arg0) {
    }

    @Override
    public void fileDeleted(FileEvent event) {
    }

    @Override
    public void fileRenamed(FileRenameEvent arg0) {
    }

    @Override
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
        
    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
            SharedClassObject.findObject(AddMapContext.class, true),
        };
    }
    
}
