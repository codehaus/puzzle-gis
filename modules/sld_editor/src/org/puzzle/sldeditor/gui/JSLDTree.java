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
package org.puzzle.sldeditor.gui;

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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.jdesktop.swingx.JXTree;

/**
 *
 * @author johann sorel
 */
public class JSLDTree extends JXTree implements DragGestureListener, DragSourceListener, DropTargetListener {

    private static final RandomStyleFactory RANDOM_STYLE_FACTORY = new RandomStyleFactory();
    private static final Icon ICON_STYLE = IconBundle.getResource().getIcon("16_style");
    private static final Icon ICON_FTS = IconBundle.getResource().getIcon("16_style_fts");
    private static final Icon ICON_RULE = IconBundle.getResource().getIcon("16_style_rule");
    private static final Icon ICON_NEW = IconBundle.getResource().getIcon("16_add_data");
    private static final Icon ICON_DUPLICATE = IconBundle.getResource().getIcon("16_duplicate");
    private static final Icon ICON_DELETE = IconBundle.getResource().getIcon("16_delete");
    
    private StyledLayerDescriptor style = null;
    private SLDTreeModel treemodel = null;
    /** Variables needed for DnD */
    private DragSource dragSource = null;

    public JSLDTree() {
        super();
        
        putClientProperty("JTree.lineStyle", "Angled");
        
        setModel(treemodel);

        setEditable(false);
//        setPreferredSize(new Dimension(200, 100));

        StyleCellRenderer renderer = new StyleCellRenderer();
        setCellRenderer(renderer);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        setComponentPopupMenu(new StylePopup(this));


        dragSource = DragSource.getDefaultDragSource();

        DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_COPY_OR_MOVE, this);

        dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);
        DropTarget dropTarget = new DropTarget(this, this);

    }

    private void parseSLD() {
        if (style != null) {
            treemodel = new SLDTreeModel(style);
            setModel(treemodel);
            revalidate();
        }
        expandAll();
    }

    public StyledLayerDescriptor getSLD() {
        return treemodel.getSLD();
    }

    public void setSLD(StyledLayerDescriptor style) {
        this.style = style;
        parseSLD();
    }

    //-------------Drag & drop -------------------------------------------------
    public void dragGestureRecognized(DragGestureEvent e) {

        TreePath path = getSelectionModel().getSelectionPath();
        DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) path.getLastPathComponent();

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

        TreePath originPath = getSelectionModel().getSelectionPath();
        Point loc = dtde.getLocation();
        TreePath targetPath = getPathForLocation(loc.x, loc.y);


        if (targetPath != null && originPath != null) {
            DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) originPath.getLastPathComponent();
            DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) targetPath.getLastPathComponent();

            treemodel.moveNode(dragNode, targetNode);

//
//            DefaultMutableTreeNode dragNodeParent = (DefaultMutableTreeNode) dragNode.getParent();
//            DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode) dragNode.getParent();
//            Object parentObj = targetNode.getUserObject();
//            Transferable trans = dtde.getTransferable();



//            if (parentObj instanceof Style) {
//                Style style = (Style) parentObj;
//                if (trans.isDataFlavorSupported(FLAVOR_FTS)) {
//
//                }
//                dtde.rejectDrop();
//                return;
//            } else if (parentObj instanceof FeatureTypeStyle) {
//                dtde.rejectDrop();
//                return;
//            } else if (parentObj instanceof Rule) {
//                Rule rule = (Rule) parentObj;
//
//                try {
//                    Object obj = trans.getTransferData(FLAVOR_SYMBOL);
//                    if (trans.isDataFlavorSupported(FLAVOR_SYMBOL)) {
//                        Symbolizer symbol = (Symbolizer) obj;
//                        Symbolizer[] symls = rule.getSymbolizers();
//                        Symbolizer[] nsymls = new Symbolizer[symls.length + 1];
//
//                        for (int i = 0; i < symls.length; i++) {
//                            nsymls[i] = symls[i];
//                        }
//
//                        nsymls[nsymls.length - 1] = symbol;
//
//                        rule.setSymbolizers(nsymls);
//                    } else {
//                        dtde.rejectDrop();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (parentObj instanceof Symbolizer) {
//                if (parentObj instanceof Rule) {
//
//                    dragNodeParent.remove(dragNode);
//                }
//            } else {
//
//            }

//            parseStyle();
            expandAll();
        }


    }


    //-------------private classes----------------------------------------------
    class StyleCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object val = node.getUserObject();

                if (val instanceof StyledLayerDescriptor) {
                    StyledLayerDescriptor style = (StyledLayerDescriptor) val;
                    lbl.setText(style.getTitle());
                    lbl.setIcon(ICON_STYLE);
                } else if (val instanceof Style) {
                    Style style = (Style) val;
                    lbl.setText(style.getTitle());
                    lbl.setIcon(ICON_STYLE);
                } else if (val instanceof FeatureTypeStyle) {
                    FeatureTypeStyle fts = (FeatureTypeStyle) val;
                    lbl.setText(fts.getTitle());
                    lbl.setIcon(ICON_FTS);
                } else if (val instanceof Rule) {
                    Rule r = (Rule) val;
                    lbl.setText(r.getTitle());
                    lbl.setIcon(ICON_RULE);
                } else if (val instanceof Symbolizer) {
                    Symbolizer symb = (Symbolizer) val;
                    Icon ico = new ImageIcon(RANDOM_STYLE_FACTORY.createGlyph(symb));
                    lbl.setText(" - ");
                    lbl.setIcon(ico);
                }

            }


            return comp;
        }
    }

    class StylePopup extends JPopupMenu {

        private final JXTree tree;
        private Object buffer = null;

        StylePopup(JXTree tree) {
            super();
            this.tree = tree;
        }

        @Override
        public void setVisible(boolean b) {

            TreePath path = tree.getSelectionModel().getSelectionPath();

            if (path != null && b == true) {
                removeAll();

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object val = node.getUserObject();

                if (val instanceof StyledLayerDescriptor) {
                    StyledLayerDescriptor sld = (StyledLayerDescriptor) val;
                    add(new NewStyleItem(node));
                } else if (val instanceof Style) {
                    Style style = (Style) val;
                    add(new NewFTSItem(node));
                    add(new JSeparator(SwingConstants.HORIZONTAL));
                    add(new DuplicateItem(node));
                } else if (val instanceof FeatureTypeStyle) {
                    FeatureTypeStyle fts = (FeatureTypeStyle) val;
                    add(new NewRuleItem(node));
                    add(new JSeparator(SwingConstants.HORIZONTAL));
                    add(new DuplicateItem(node));
                } else if (val instanceof Rule) {
                    Rule rule = (Rule) val;
                    add(new NewPointSymbolizerItem(node));
                    add(new NewLineSymbolizerItem(node));
                    add(new NewPolygonSymbolizerItem(node));
                    add(new NewRasterSymbolizerItem(node));
                    add(new NewTextSymbolizerItem(node));
                    add(new JSeparator(SwingConstants.HORIZONTAL));
                    add(new DuplicateItem(node));
                } else if (val instanceof Symbolizer) {
                    Symbolizer symb = (Symbolizer) val;
                    add(new DuplicateItem(node));
                }
                                
                if(treemodel.isDeletable(node)){
                    add(new JSeparator(SwingConstants.HORIZONTAL));
                    add(new DeleteItem(node));
                }
                
                super.setVisible(b);
            } else {
                super.setVisible(false);
            }
        }
    }

    
    class NewStyleItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;
        
        NewStyleItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("new Style");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newStyle(NODE);
                }
            });
        }
    }
    
    class NewFTSItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;
        
        NewFTSItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("new FTS");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newFeatureTypeStyle(NODE);
                }
            });
        }
    }

    class NewRuleItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewRuleItem(DefaultMutableTreeNode node) {

            this.NODE = node;
            setText("New Rule");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newRule(NODE);
                }
            });
        }
    }

    class DuplicateItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        DuplicateItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Duplicate");
            setIcon(ICON_DUPLICATE);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    DefaultMutableTreeNode copy = treemodel.duplicateNode(NODE);
                }
            });
        }
    }
    
    class DeleteItem extends JMenuItem {
        
        private final DefaultMutableTreeNode NODE;

        DeleteItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Delete");
            setIcon(ICON_DELETE);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.deleteNode(NODE);
                }
            });
        }
        
    }

    class NewPointSymbolizerItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewPointSymbolizerItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Point Symbolizer");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newPointSymbolizer(NODE);
                }
            });
        }
    }

    class NewLineSymbolizerItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewLineSymbolizerItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Line Symbolizer");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newLineSymbolizer(NODE);
                }
            });
        }
    }

    class NewPolygonSymbolizerItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewPolygonSymbolizerItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Polygon Symbolizer");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newPolygonSymbolizer(NODE);
                }
            });
        }
    }
    
    class NewTextSymbolizerItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewTextSymbolizerItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Text Symbolizer");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newTextSymbolizer(NODE);
                    
                }
            });
        }
    }

    class NewRasterSymbolizerItem extends JMenuItem {

        private final DefaultMutableTreeNode NODE;

        NewRasterSymbolizerItem(DefaultMutableTreeNode node) {
            this.NODE = node;
            setText("Raster Symbolizer");
            setIcon(ICON_NEW);
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    treemodel.newRasterSymbolizer(NODE);
                }
            });
        }
    }
    
}