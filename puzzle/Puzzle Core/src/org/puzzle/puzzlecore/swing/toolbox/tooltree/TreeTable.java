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
package org.puzzle.puzzlecore.swing.toolbox.tooltree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.event.EventListenerList;
import javax.swing.tree.TreePath;

import org.geotools.gui.swing.toolbox.widgettool.WidgetToolDescriptor;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 *
 * @author johann sorel
 */
final class TreeTable extends JXTreeTable implements MouseListener {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org/geotools/gui/swing/toolbox/tooltree/Bundle");
    
    
    protected final WidgetToolDescriptor[] EMPTY_TREETOOLDESCRIPTOR_ARRAY = {};
    protected final EventListenerList LISTENERS = new EventListenerList();
    private final ToolPackTreeNode root;
    private final Map<WidgetToolDescriptor, ToolTreeNode> tools = new HashMap<WidgetToolDescriptor, ToolTreeNode>();
    private DefaultTreeTableModel model;

    /**
     * Tree widget to manage tools
     */
    TreeTable(JToolTree frame) {
        super(new DefaultTreeTableModel(new ToolPackTreeNode("Root")));
        model = (DefaultTreeTableModel) getTreeTableModel();

        getSelectionModel().setSelectionMode(getSelectionModel().SINGLE_SELECTION);

        setTreeCellRenderer(new DefaultTreeRenderer(new ToolTreeNodeProvider(frame)));

        String name = BUNDLE.getString("tools");
        getColumnModel().getColumn(0).setHeaderValue(name);

        root = (ToolPackTreeNode) getTreeTableModel().getRoot();

        expandAll();

        addMouseListener(this);
    }

    void addTool(WidgetToolDescriptor tool) {

        if (!tools.containsKey(tool)) {

            ToolTreeNode node = new ToolTreeNode(tool.getTitle());
            node.setUserObject(tool);

            tools.put(tool, node);

            String[] path = tool.getPath();

            ToolPackTreeNode origine = root;
            for (String nodeName : path) {
                boolean found = false;
                for (int i = 0,  max = origine.getChildCount(); (i < max && !found); i++) {
                    TreeTableNode n = origine.getChildAt(i);

                    if (n instanceof ToolPackTreeNode) {
                        if (((ToolPackTreeNode) n).getTitle().equals(nodeName)) {
                            origine = (ToolPackTreeNode) n;
                            found = true;
                        }
                    }
                }

                if (!found) {
                    ToolPackTreeNode n = new ToolPackTreeNode(nodeName);

                    //finding the right index, compare alpabeticly
                    int insertIndex = origine.getChildCount();
                    if (origine.getChildCount() != 0) {
                        for (int i = 0,  max = origine.getChildCount(); i < max; i++) {
                            TreeTableNode test = origine.getChildAt(i);
                            String name = test.getValueAt(0).toString();
                            if (name.compareTo(nodeName) >= 0) {
                                insertIndex = i;
                                break;
                            }
                        }
                    }

                    model.insertNodeInto(n, origine, insertIndex);
                    //origine.add(n);
                    origine = n;
                }
            }


            //finding the right index, compare alpabeticly
            String nodeName = node.getValueAt(0).toString();
            int insertIndex = origine.getChildCount();
            if (origine.getChildCount() != 0) {
                for (int i = 0,  max = origine.getChildCount(); i < max; i++) {
                    TreeTableNode test = origine.getChildAt(i);
                    String name = test.getValueAt(0).toString();
                    if (name.compareTo(nodeName) >= 0) {
                        insertIndex = i;
                        break;
                    }
                }
            }
            model.insertNodeInto(node, origine, insertIndex);
            //origine.add(node);
            expandPath(new TreePath(root));
        }



    }

    void removeTool(WidgetToolDescriptor tool) {

        if (tools.containsKey(tool)) {
            ToolTreeNode node = tools.get(tool);
            MutableTreeTableNode origine = (MutableTreeTableNode) node.getParent();
            model.removeNodeFromParent(node);
            //origine.remove(node);

            while (origine != root && origine.getChildCount() == 0) {
                MutableTreeTableNode parent = (MutableTreeTableNode) origine.getParent();
                model.removeNodeFromParent(origine);
                //parent.remove(origine);
                origine = parent;
            }

            revalidate();

            tools.remove(tool);
        }
    }

    WidgetToolDescriptor[] getTreeToolDescriptors() {
        return tools.keySet().toArray(EMPTY_TREETOOLDESCRIPTOR_ARRAY);
    }

    public void mouseClicked(MouseEvent e) {
        int nb = e.getClickCount();
        if (nb == 2) {
            TreePath path = getTreeSelectionModel().getSelectionPath();
            Object node = path.getLastPathComponent();
            if (node instanceof ToolTreeNode) {
                WidgetToolDescriptor tool = (WidgetToolDescriptor) ((ToolTreeNode) node).getUserObject();
                fireActivation(tool);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void fireActivation(WidgetToolDescriptor tool) {
        ToolTreeListener[] listeners = getToolTreeListeners();

        for (ToolTreeListener listener : listeners) {
            listener.treeToolActivated(tool);
        }
    }

    void addToolTreeListener(ToolTreeListener listener) {
        LISTENERS.add(ToolTreeListener.class, listener);
    }

    void removeToolTreeListener(ToolTreeListener listener) {
        LISTENERS.remove(ToolTreeListener.class, listener);
    }

    ToolTreeListener[] getToolTreeListeners() {
        return LISTENERS.getListeners(ToolTreeListener.class);
    }
}

