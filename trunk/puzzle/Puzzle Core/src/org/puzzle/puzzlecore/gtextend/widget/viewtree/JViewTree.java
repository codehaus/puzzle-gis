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
package org.puzzle.puzzlecore.gtextend.widget.viewtree;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.geotools.gui.swing.contexttree.renderer.DefaultCellRenderer;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.puzzle.puzzlecore.struct.MapGroup;
import org.puzzle.puzzlecore.struct.MapView;

/**
 *
 * @author johann sorel
 */
public class JViewTree extends JXTreeTable implements DragGestureListener, DragSourceListener, DropTargetListener {

    private static final RandomStyleFactory RANDOM_STYLE_FACTORY = new RandomStyleFactory();
    private static final Icon ICON_VIEW = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/view.png"));
    private static final Icon ICON_GROUP = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/group.png"));
    
    private ViewTreeModel treemodel = new ViewTreeModel();
    /** Variables needed for DnD */
    private DragSource dragSource = null;

    public JViewTree() {
        super();

        putClientProperty("JTree.lineStyle", "Angled");

        setTreeTableModel(treemodel);
        setRootVisible(true);
        setEditable(true);

        ViewCellRenderer renderer = new ViewCellRenderer();
        setTreeCellRenderer(renderer);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        for (int i = 1; i < 4; i++) {
            getColumnModel().getColumn(i).setCellRenderer(new DefaultCellRenderer(new ViewComponent()));
            getColumnModel().getColumn(i).setPreferredWidth(25);
            getColumnModel().getColumn(i).setMaxWidth(25);
            getColumnModel().getColumn(i).setMinWidth(25);
        }

        setComponentPopupMenu(new StylePopup(this));


        dragSource = DragSource.getDefaultDragSource();

        DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

        dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);
        DropTarget dropTarget = new DropTarget(this, this);

    }


    //-------------Drag & drop -------------------------------------------------
    public void dragGestureRecognized(DragGestureEvent e) {

//        TreePath path = getSelectionModel().getSelectionPath();
//        DefaultMutableTreeTableNode dragNode = (DefaultMutableTreeTableNode) path.getLastPathComponent();
//
//        if (dragNode != null) {
//            Transferable transferable = new StringSelection("");
//
//            e.startDrag(null, transferable);
//        }
    }

    //--------------------drag events-------------------------------------------
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    public void dragOver(DragSourceDragEvent dsde) {
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    public void dragExit(DragSourceEvent dse) {
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    //--------------------drop events-------------------------------------------
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {

        TreePath originPath = getTreeSelectionModel().getSelectionPath();
        Point loc = dtde.getLocation();
        TreePath targetPath = getPathForLocation(loc.x, loc.y);


        if (targetPath != null && originPath != null) {
            DefaultMutableTreeTableNode dragNode = (DefaultMutableTreeTableNode) originPath.getLastPathComponent();
            DefaultMutableTreeTableNode targetNode = (DefaultMutableTreeTableNode) targetPath.getLastPathComponent();

            treemodel.moveNode(dragNode, targetNode);
            expandAll();
        }

    }


    //-------------private classes----------------------------------------------
    class ViewCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

//            System.out.println(value);

            Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
                Object val = node.getUserObject();

                if (val instanceof MapView) {
                    MapView view = (MapView) val;
                    lbl.setText(view.getName());
                    lbl.setIcon(ICON_VIEW);
                } else if (val instanceof MapGroup) {
                    MapGroup group = (MapGroup) val;
                    lbl.setText(group.getTitle());
                    lbl.setIcon(ICON_GROUP);
                }else{
                    lbl.setIcon(ICON_GROUP);
                }

            }


            return comp;
        }
    }

    class StylePopup extends JPopupMenu {

        private final JXTreeTable tree;
        private Object buffer = null;

        StylePopup(JXTreeTable tree) {
            super();
            this.tree = tree;
        }

        @Override
        public void setVisible(boolean b) {

            TreePath path = tree.getTreeSelectionModel().getSelectionPath();

            if (path != null && b == true) {
                removeAll();


                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) path.getLastPathComponent();

                add(new DuplicateItem(node));
                add(new DeleteItem(node));


                super.setVisible(b);
            } else {
                super.setVisible(false);
            }
        }
    }

    class DuplicateItem extends JMenuItem {

        private final DefaultMutableTreeTableNode NODE;

        DuplicateItem(DefaultMutableTreeTableNode node) {
            this.NODE = node;
            setText("Duplicate");
            setIcon(ICON_GROUP);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
//                    DefaultMutableTreeTableNode copy = treemodel.duplicateNode(NODE);
                }
            });
        }
    }

    class DeleteItem extends JMenuItem {

        private final DefaultMutableTreeTableNode NODE;

        DeleteItem(DefaultMutableTreeTableNode node) {
            this.NODE = node;
            setText("Delete");
            setIcon(ICON_GROUP);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
//                    treemodel.deleteNode(NODE);
                }
            });
        }
    }
}
