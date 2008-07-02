/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project;

import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.puzzle.puzzlecore.project.nodes.GISDocNode;
import org.puzzle.puzzlecore.project.nodes.GISMapNode;
import org.puzzle.puzzlecore.project.nodes.GISProjectNode;
import org.puzzle.puzzlecore.project.nodes.GISSourceNode;

/**
 *
 * @author Administrateur
 */
public class GISLogicalView implements LogicalViewProvider{

    private final GISProject project;
    
    public GISLogicalView(GISProject project){
        this.project = project;
    }
    
    public Node createLogicalView() {
        
        GISProjectNode root = new GISProjectNode(project);
        
        Node mapNode = null;
        Node docNode = null;
        Node srcNode = null;
        
        try {
            FileObject maps = project.getMapFolder(true);
            DataFolder mapDataObject = DataFolder.findFolder (maps);
            Node mapsNode = mapDataObject.getNodeDelegate();
            mapNode = new GISMapNode (mapsNode, project);
        } catch (DataObjectNotFoundException donfe) {
            ErrorManager.getDefault().notify(donfe);
            mapNode = new AbstractNode (Children.LEAF);
        }
        
        try {
            FileObject docs = project.getDocFolder(true);
            DataFolder docDataObject = DataFolder.findFolder (docs);
            Node docsNode = docDataObject.getNodeDelegate();
            docNode = new GISDocNode (docsNode, project);
        } catch (DataObjectNotFoundException donfe) {
            ErrorManager.getDefault().notify(donfe);
            docNode = new AbstractNode (Children.LEAF);
        }
        
        try {
            FileObject sources = project.getSourceFolder(true);
            DataFolder sourceDataObject = DataFolder.findFolder (sources);
            srcNode = new GISSourceNode (sourceDataObject, project);
        } catch (DataObjectNotFoundException donfe) {
            ErrorManager.getDefault().notify(donfe);
            srcNode = new AbstractNode (Children.LEAF);
        }
        
        root.getChildren().add(new Node[]{mapNode,docNode,srcNode});
        return root;
    }

    public Node findPath(Node root, Object target) {
        return null;
    }

}
