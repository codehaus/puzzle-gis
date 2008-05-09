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
import java.awt.ComponentOrientation;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.gui.swing.propertyedit.PropertyPane;
import org.geotools.gui.swing.style.JLineSymbolizerPane;
import org.geotools.gui.swing.style.JPointSymbolizerPane;
import org.geotools.gui.swing.style.JPolygonSymbolizerPane;
import org.geotools.gui.swing.style.JRasterSymbolizerPane;
import org.geotools.gui.swing.style.JTextSymbolizerPane;
import org.geotools.gui.swing.style.StyleElementEditor;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.puzzle.puzzlecore.gtextend.widget.style.JFeatureTypeStylePane;
import org.puzzle.puzzlecore.gtextend.widget.style.JRulePane;
import org.puzzle.puzzlecore.gtextend.widget.style.JStylePane;

/**
 * @author johann sorel
 */
public class JAdvancedSLDPanel extends javax.swing.JPanel implements PropertyPane, StyleElementEditor<StyledLayerDescriptor> {

    private MapLayer layer = null;
    private StyledLayerDescriptor style = null;
    private StyleElementEditor editor = null;
    private final TreeSelectionListener listener = new TreeSelectionListener() {

        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = tree.getSelectionModel().getSelectionPath(); 

            //we validate the previous edition pane
            if (editor != null) {
                editor.apply();
            }


            if (path != null) {
                Object val = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

                pan_info.removeAll();
                pan_info.revalidate();

                if (val instanceof StyledLayerDescriptor) {
                    StyledLayerDescriptor sld = (StyledLayerDescriptor) val;

                    if (guiSLDPane == null) {
                        guiSLDPane = new JSLDPane();
                    }
                    guiSLDPane.setEdited(sld);
                    editor = guiSLDPane;
                    pan_info.add(guiSLDPane);
                } else if (val instanceof Style) {
                    Style style = (Style) val;

                    if (guiStylePane == null) {
                        guiStylePane = new JStylePane();
                    }
                    guiStylePane.setEdited(style);
                    editor = guiStylePane;
                    pan_info.add(guiStylePane);
                } else if (val instanceof FeatureTypeStyle) {
                    FeatureTypeStyle fts = (FeatureTypeStyle) val;

                    if (guiFTSPane == null) {
                        guiFTSPane = new JFeatureTypeStylePane();
                    }
                    guiFTSPane.setEdited(fts);
                    editor = guiFTSPane;
                    pan_info.add(guiFTSPane);
                } else if (val instanceof Rule) {
                    Rule rule = (Rule) val;

                    if (guiRulePane == null) {
                        guiRulePane = new JRulePane();
                    }
                    guiRulePane.setEdited(rule);
                    editor = guiRulePane;
                    pan_info.add(guiRulePane);
                } else if (val instanceof Symbolizer) {
                    Symbolizer symb = (Symbolizer) val;

                    if (symb instanceof RasterSymbolizer) {
                        if (guiRasterPane == null) {
                            guiRasterPane = new JRasterSymbolizerPane();
                        }
                        guiRasterPane.setLayer(layer);
                        guiRasterPane.setEdited((RasterSymbolizer) symb);
                        editor = guiRasterPane;
                    } else if (symb instanceof PolygonSymbolizer) {
                        if (guiPolygonPane == null) {
                            guiPolygonPane = new JPolygonSymbolizerPane();
                        }
                        guiPolygonPane.setLayer(layer);
                        guiPolygonPane.setEdited((PolygonSymbolizer) symb);
                        editor = guiPolygonPane;
                    } else if (symb instanceof LineSymbolizer) {
                        if (guiLinePane == null) {
                            guiLinePane = new JLineSymbolizerPane();
                        }
                        guiLinePane.setLayer(layer);
                        guiLinePane.setEdited((LineSymbolizer) symb);
                        editor = guiLinePane;
                    } else if (symb instanceof PointSymbolizer) {
                        if (guiPointPane == null) {
                            guiPointPane = new JPointSymbolizerPane();
                        }
                        guiPointPane.setLayer(layer);
                        guiPointPane.setEdited((PointSymbolizer) symb);
                        editor = guiPointPane;
                    } else if (symb instanceof TextSymbolizer) {
                        if (guiTextPane == null) {
                            guiTextPane = new JTextSymbolizerPane();
                        }
                        guiTextPane.setLayer(layer);
                        guiTextPane.setEdited((TextSymbolizer) symb);
                        editor = guiTextPane;
                    } else {
                        editor = null;
                    }

                    if (editor != null) {
                        pan_info.add(editor.getComponent());
                    }
                }

                pan_info.revalidate();
                pan_info.repaint();


            }
        }
    };
    //keep reference to different panes, beacause those are heavy swing components and take
    //time create again and again, so we reuse them.
    private JSLDPane guiSLDPane = null;
    private JStylePane guiStylePane = null;
    private JFeatureTypeStylePane guiFTSPane = null;
    private JRulePane guiRulePane = null;
    private JPointSymbolizerPane guiPointPane = null;
    private JLineSymbolizerPane guiLinePane = null;
    private JPolygonSymbolizerPane guiPolygonPane = null;
    private JRasterSymbolizerPane guiRasterPane = null;
    private JTextSymbolizerPane guiTextPane = null;

    /** Creates new form JAdvancedStylePanel */
    public JAdvancedSLDPanel() {
        initComponents();
        init();
    }

    private void init() {
        jsp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); 
        tree.addTreeSelectionListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    pan_info = new javax.swing.JPanel();
    jsp = new javax.swing.JScrollPane();
    tree = new org.puzzle.sldeditor.gui.JSLDTree();

    jSplitPane1.setBorder(null);
    jSplitPane1.setDividerLocation(220);
    jSplitPane1.setDividerSize(1);

    pan_info.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
    pan_info.setLayout(new java.awt.GridLayout(1, 1));
    jScrollPane1.setViewportView(pan_info);

    jSplitPane1.setRightComponent(jScrollPane1);

    jsp.setViewportView(tree);

    jSplitPane1.setLeftComponent(jsp);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents
    public void apply() {
        if (editor != null) {
            editor.apply();
        }

        style = tree.getSLD();

    }

    public void setLayer(MapLayer layer) {
        this.layer = layer;
    }

    public MapLayer getLayer() {
        return layer;
    }

    public void setEdited(StyledLayerDescriptor style) {
        this.style = style;
        parse();
    }

    public StyledLayerDescriptor getEdited() {
        style = tree.getSLD(); 
        apply();
        return style;
    }

    private void parse() {
        tree.setSLD(style);
    }

    public ImageIcon getIcon() {
        return IconBundle.getResource().getIcon("16_style");
    }

    public String getTitle() {
        return "advanced";
    }

    public void setTarget(Object layer) {

//        if (layer instanceof MapLayer) {
//            setLayer((MapLayer) layer);
//            setEdited(this.layer.getStyle());
//        }
    }

    public void reset() {
        parse();
    }

    public String getToolTip() {
        return "";
    }

    public Component getComponent() {
        return this;
    }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JScrollPane jsp;
  private javax.swing.JPanel pan_info;
  private org.puzzle.sldeditor.gui.JSLDTree tree;
  // End of variables declaration//GEN-END:variables
}
