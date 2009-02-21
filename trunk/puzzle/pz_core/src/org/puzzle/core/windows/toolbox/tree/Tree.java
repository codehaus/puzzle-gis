/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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
package org.puzzle.core.windows.toolbox.tree;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanInfo;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.geotools.gui.swing.resource.IconBundle;
import org.puzzle.core.tool.ToolDescriptor;

/**
 * Tool tree.
 * 
 * @author johann sorel (Puzzle-GIS)
 */
public class Tree extends JTree {

    static{
        IconBundle.getInstance().addBundle(ResourceBundle.getBundle("/org/puzzle/core/windows/toolbox/tree/IconBundle"));
    }

    protected final ToolDescriptor[] EMPTY_TREETOOLDESCRIPTOR_ARRAY = {};
    protected final EventListenerList LISTENERS = new EventListenerList();
    
    private final Map<ToolDescriptor, DefaultMutableTreeNode> tools = new WeakHashMap<ToolDescriptor, DefaultMutableTreeNode>();
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;

    /**
     * Tree widget to manage tools
     */
    public Tree() {
        super( new DefaultTreeModel(new DefaultMutableTreeNode("Root")));
        model = (DefaultTreeModel) getModel();
        root = (DefaultMutableTreeNode) getModel().getRoot();

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        setCellRenderer(new ToolCellRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);

        addMouseListener(new ToolSelectionListener());
    }

    public void addTool(final ToolDescriptor tool) {

        if (!tools.containsKey(tool)) {

            final DefaultMutableTreeNode toolnode = new DefaultMutableTreeNode();
            toolnode.setUserObject(new WeakReference<ToolDescriptor>(tool));

            tools.put(tool, toolnode);

            final String[] path = tool.getPath();

            DefaultMutableTreeNode origine = root;
            for (final String nodeName : path) {
                boolean found = false;
                for (int i = 0, max = origine.getChildCount(); (i < max && !found); i++) {
                    final DefaultMutableTreeNode n = (DefaultMutableTreeNode) origine.getChildAt(i);
                    final Object userObj           = n.getUserObject();

                    if (userObj instanceof String) {
                        String title = (String) userObj;
                        if (title.equals(nodeName)) {
                            origine = n;
                            found = true;
                        }
                    }
                }

                if (!found) {
                    final DefaultMutableTreeNode n = new DefaultMutableTreeNode(nodeName);

                    //finding the right index, compare alpabeticly
                    int insertIndex = origine.getChildCount();
                    if (origine.getChildCount() != 0) {
                        for (int i = 0, max = origine.getChildCount(); i < max; i++) {
                            final DefaultMutableTreeNode candidate = (DefaultMutableTreeNode) origine.getChildAt(i);
                            final Object userObj                   = candidate.getUserObject();

                            if(userObj instanceof String){
                                String name = (String) userObj;
                                if (name.compareTo(nodeName) >= 0) {
                                    insertIndex = i;
                                    break;
                                }
                            }else if(userObj instanceof WeakReference){
                                WeakReference<ToolDescriptor> weak = (WeakReference<ToolDescriptor>)userObj;
                                ToolDescriptor aTool = weak.get();
                                if(aTool != null){
                                    String name = aTool.getTitle();
                                    if (name.compareTo(nodeName) >= 0) {
                                        insertIndex = i;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    model.insertNodeInto(n, origine, insertIndex);
                    //origine.add(n);
                    origine = n;
                }
            }


            //finding the right index, compare alpabeticly
            final String nodeName = tool.getTitle();
            int insertIndex = origine.getChildCount();
            if (origine.getChildCount() != 0) {
                for (int i = 0, max = origine.getChildCount(); i < max; i++) {
                    final DefaultMutableTreeNode candidate = (DefaultMutableTreeNode) origine.getChildAt(i);
                    final Object userObj                   = candidate.getUserObject();

                    if(userObj instanceof String){
                        String name = (String) userObj;
                        if (name.compareTo(nodeName) >= 0) {
                            insertIndex = i;
                            break;
                        }
                    }else if(userObj instanceof WeakReference){
                        WeakReference<ToolDescriptor> weak = (WeakReference<ToolDescriptor>)userObj;
                        ToolDescriptor aTool = weak.get();
                        if(aTool != null){
                            String name = aTool.getTitle();
                            if (name.compareTo(nodeName) >= 0) {
                                insertIndex = i;
                                break;
                            }
                        }
                    }
                }
            }
            model.insertNodeInto(toolnode, origine, insertIndex);
            //origine.add(node);
            expandPath(new TreePath(root));
        }

    }

    public void removeTool(ToolDescriptor tool) {

        if (tools.containsKey(tool)) {
            DefaultMutableTreeNode node = tools.get(tool);
            removeNodeAndEmptyParent(node);
            tools.remove(tool);
        }
    }
    
    private void removeNodeAndEmptyParent(DefaultMutableTreeNode node){
        DefaultMutableTreeNode origine = (DefaultMutableTreeNode) node.getParent();
        model.removeNodeFromParent(node);
        //origine.remove(node);

        while (origine != root && origine.getChildCount() == 0) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) origine.getParent();
            model.removeNodeFromParent(origine);
            //parent.remove(origine);
            origine = parent;
        }

        revalidate();
    }
    
    public void removeWeakTools(){
        weakCheck(root);
    }
    
    private void weakCheck(DefaultMutableTreeNode node){
        
        if(node.getUserObject() instanceof String){
            
            for(int i=node.getChildCount()-1 ; i>=0 ; i--){
                weakCheck( (DefaultMutableTreeNode) node.getChildAt(i));
            }
            
        }else{
            WeakReference obj = (WeakReference) node.getUserObject();
            if( obj.get() == null){
                removeNodeAndEmptyParent((DefaultMutableTreeNode)node);
            }
        }        
    }
    
    public ToolDescriptor[] getTreeToolDescriptors() {
        return tools.keySet().toArray(EMPTY_TREETOOLDESCRIPTOR_ARRAY);
    }

    private void fireActivation(ToolDescriptor tool) {
        if (tool != null) {
            ToolTreeListener[] listeners = getToolTreeListeners();

            for (ToolTreeListener listener : listeners) {
                listener.treeToolActivated(tool);
            }
        }
    }

    public void addToolTreeListener(ToolTreeListener listener) {
        LISTENERS.add(ToolTreeListener.class, listener);
    }

    public void removeToolTreeListener(ToolTreeListener listener) {
        LISTENERS.remove(ToolTreeListener.class, listener);
    }

    public ToolTreeListener[] getToolTreeListeners() {
        return LISTENERS.getListeners(ToolTreeListener.class);
    }

    private class ToolCellRenderer extends DefaultTreeCellRenderer{

        private final ImageIcon ICON_TOOL     = IconBundle.getInstance().getIcon("16_tool");
        private final ImageIcon ICON_TOOLPACK = IconBundle.getInstance().getIcon("16_toolpack");

        /**
         * {@inheritDoc }
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row , hasFocus);

            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            final Object userObj = node.getUserObject();

            if(userObj instanceof String){
                label.setText((String)userObj);
                label.setIcon(ICON_TOOLPACK);
            }else if(userObj instanceof WeakReference){
                final WeakReference<ToolDescriptor> weak = (WeakReference<ToolDescriptor>) userObj;
                final ToolDescriptor tool                = weak.get();
                if(tool != null){
                    label.setText(tool.getTitle());
                    final Image img = tool.getIcon(BeanInfo.ICON_COLOR_16x16);
                    label.setIcon( (img == null) ? ICON_TOOL : new ImageIcon(img));
                }else{
                    label.setText("no-tool");
                    label.setIcon(ICON_TOOL);
                }
            }else{
                label.setText("unknowned");
                label.setIcon(IconBundle.EMPTY_ICON);
            }

            return label;
        }

    }

    private class ToolSelectionListener extends MouseAdapter{

        /**
         * {@inheritDoc }
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int nb = e.getClickCount();
            if (nb == 2) {
                final TreePath path = getSelectionModel().getSelectionPath();
                if (path != null) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    final Object userObj              = node.getUserObject();
                    if (userObj instanceof WeakReference) {
                        final WeakReference<ToolDescriptor> weak = (WeakReference<ToolDescriptor>) userObj;
                        final ToolDescriptor tool                = weak.get();
                        fireActivation(tool);
                    }
                }
            }
        }

    }
    
}
