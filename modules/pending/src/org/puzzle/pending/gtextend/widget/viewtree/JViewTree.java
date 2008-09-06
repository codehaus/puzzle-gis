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
package org.puzzle.pending.gtextend.widget.viewtree;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.geotools.gui.swing.contexttree.renderer.DefaultCellEditor;
import org.geotools.gui.swing.contexttree.renderer.DefaultCellRenderer;
import org.geotools.gui.swing.contexttree.renderer.DefaultContextTreeHeaderRenderer;
import org.geotools.gui.swing.contexttree.renderer.HeaderInfo;
import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.style.RandomStyleFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.puzzle.puzzlecore.view.MapGroup;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author johann sorel
 */
public class JViewTree extends JXTreeTable implements DragGestureListener, DragSourceListener, DropTargetListener {

    private static final RandomStyleFactory RANDOM_STYLE_FACTORY = new RandomStyleFactory();
    private static final Icon ICON_VIEW = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/view.png"));
    private static final Icon ICON_GROUP = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/group.png"));
    private static final Icon ICON_SCALE = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/scale.png"));
    private static final Icon ICON_ROTATION = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/rotation.png"));
    private static final Icon ICON_TRANSLATION = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/translation.png"));
    private final ViewTreeModel treemodel ;
    /** Variables needed for DnD */
    private DragSource dragSource = null;

    public JViewTree() {
        super(new ViewTreeModel());
        treemodel = getTreeTableModel();
        
        putClientProperty("JTree.lineStyle", "Angled");

        setRootVisible(false);
        setEditable(true);
        
//        setShowsRootHandles(false);
                       

        treemodel.addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
            }

            public void treeNodesInserted(TreeModelEvent e) {
                
//                expandPath( new TreePath(treemodel.getRoot()));
                
                for(int i=0,n=treemodel.getRoot().getChildCount();i<n;i++) {
                    TreeTableNode node = treemodel.getRoot().getChildAt(i);
                    
                    if(!node.isLeaf()){
                        for(int j=0,m=node.getChildCount();j<m;j++) {
                            expandPath( new TreePath (node.getChildAt(j)));
                        }
                    }else{
                        expandPath( new TreePath (node));
                    }
                }               
                    
//                System.out.println("ajout-------------------------");
//                expandAll();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
            }

            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
        
        ViewCellRenderer renderer = new ViewCellRenderer();
        setTreeCellRenderer(renderer);
        getTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getTableHeader().setDefaultRenderer(new DefaultContextTreeHeaderRenderer());

        for (int i = 1; i < 4; i++) {
            getColumnModel().getColumn(i).setCellRenderer(new DefaultCellRenderer(new ViewComponent()));
            getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new ViewComponent()));
            getColumnModel().getColumn(i).setPreferredWidth(25);
            getColumnModel().getColumn(i).setMaxWidth(25);
            getColumnModel().getColumn(i).setMinWidth(25);
            ((TableColumnExt) getColumnModel().getColumn(i)).setEditable(true);
        }

        getColumnModel().getColumn(4).setCellRenderer(new DefaultCellRenderer(new ContextComponent()));
        getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new ContextComponent()));
        getColumnModel().getColumn(4).setPreferredWidth(100);
        getColumnModel().getColumn(4).setMaxWidth(200);
        ((TableColumnExt) getColumnModel().getColumn(4)).setEditable(true);



        getColumnModel().getColumn(1).setHeaderValue(new HeaderInfo("", null, ICON_TRANSLATION));
        getColumnModel().getColumn(2).setHeaderValue(new HeaderInfo("", null, ICON_SCALE));
        getColumnModel().getColumn(3).setHeaderValue(new HeaderInfo("", null, ICON_ROTATION));
        getColumnModel().getColumn(4).setHeaderValue(new HeaderInfo("", "Context",null));
        setComponentPopupMenu(new StylePopup(this));


        dragSource = DragSource.getDefaultDragSource();

        DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

        dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);
        DropTarget dropTarget = new DropTarget(this, this);

        initCellEditAcceleration();
    }

    /**
     * add mouse listener to set cell in edit mode when mouseover
     */
    private void initCellEditAcceleration() {
        //listener to set cell in edit mode on mouse over
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (p != null) {
                    int row = rowAtPoint(p);
                    int col = columnAtPoint(p);

                    if (row != editingRow || col != editingColumn) {

                        if (isEditing()) {
                            TableCellEditor editor = cellEditor;
                            if (!editor.stopCellEditing()) {
                                editor.cancelCellEditing();
                            }
                        }

                        if (!isEditing() && col > 0 && row >= 0) {
                            editCellAt(row, col);
                        }
                    }
                }
            }
        });
    }

    //-------------Drag & drop -------------------------------------------------
    public void dragGestureRecognized(DragGestureEvent e) {

        TreePath path = getTreeSelectionModel().getSelectionPath();
        DefaultMutableTreeTableNode dragNode = (DefaultMutableTreeTableNode) path.getLastPathComponent();

        if (dragNode != null) {
            Transferable transferable = new StringSelection("");
            e.startDrag(null, transferable);
        }
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
        }

    }

    @Override
    public ViewTreeModel getTreeTableModel() {
        return (ViewTreeModel) super.getTreeTableModel();
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
                } else {
                    lbl.setText(" No group");
                    lbl.setIcon(IconBundle.EMPTY_ICON);
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

                if(node.getUserObject() != null){
                    add(new DeleteItem(node));
                }


                super.setVisible(b);
            } else {
                super.setVisible(false);
            }
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
                    Object obj = NODE.getUserObject();
                    if(obj != null){
                        if(obj instanceof MapView){
                            ((MapView)obj).close();
//                            treemodel.removeView((MapView)obj);
                        }
                        if(obj instanceof MapGroup){
                            treemodel.removeGroup((MapGroup)obj);
                        }
                    }
                }
            });
        }
    }
}

