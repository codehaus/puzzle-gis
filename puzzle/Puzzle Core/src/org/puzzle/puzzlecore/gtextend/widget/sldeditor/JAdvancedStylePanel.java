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
package org.puzzle.puzzlecore.gtextend.widget.sldeditor;

import java.awt.Component;
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
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

/**
 * @author johann sorel
 */
public class JAdvancedStylePanel extends javax.swing.JPanel implements PropertyPane, StyleElementEditor<Style> {

    private MapLayer layer = null;
    private Style style = null;
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

                if (val instanceof Style) {
                    Style style = (Style) val;
                    JStylePane pan = new JStylePane();
                    pan.setEdited(style);
                    editor = pan;
                    pan_info.add(pan);
                } else if (val instanceof FeatureTypeStyle) {
                    FeatureTypeStyle fts = (FeatureTypeStyle) val;
                    JFeatureTypeStylePane pan = new JFeatureTypeStylePane();
                    pan.setEdited(fts);
                    editor = pan;
                    pan_info.add(pan);
                } else if (val instanceof Rule) {
                    Rule rule = (Rule) val;
                    JRulePane pan = new JRulePane();
                    pan.setEdited(rule);
                    editor = pan;
                    pan_info.add(pan);
                } else if (val instanceof Symbolizer) {
                    Symbolizer symb = (Symbolizer) val;

                    if (symb instanceof RasterSymbolizer) {
                        JRasterSymbolizerPane p = new JRasterSymbolizerPane();
                        p.setLayer(layer);
                        p.setEdited((RasterSymbolizer) symb);
                        editor = p;
                    } else if (symb instanceof PolygonSymbolizer) {
                        JPolygonSymbolizerPane p = new JPolygonSymbolizerPane();
                        p.setLayer(layer);
                        p.setEdited((PolygonSymbolizer) symb);
                        editor = p;
                    } else if (symb instanceof LineSymbolizer) {
                        JLineSymbolizerPane p = new JLineSymbolizerPane();
                        p.setLayer(layer);
                        p.setEdited((LineSymbolizer) symb);
                        editor = p;
                    } else if (symb instanceof PointSymbolizer) {
                        JPointSymbolizerPane p = new JPointSymbolizerPane();
                        p.setLayer(layer);
                        p.setEdited((PointSymbolizer) symb);
                        editor = p;
                    } else if (symb instanceof TextSymbolizer) {
                        JTextSymbolizerPane p = new JTextSymbolizerPane();
                        p.setLayer(layer);
                        p.setEdited((TextSymbolizer) symb);
                        editor = p;
                    } else {
                        editor = null;
                    }

                    if(editor != null){
                        pan_info.add(editor.getComponent());
                    }
                }

                pan_info.revalidate();

            }
        }
    };

    /** Creates new form JAdvancedStylePanel */
    public JAdvancedStylePanel() {
        initComponents();
        init();
    }
    
    private void init(){
        tree.addTreeSelectionListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pan_info = new javax.swing.JPanel();
        jsp2 = new javax.swing.JScrollPane();
        tree = new org.puzzle.puzzlecore.gtextend.widget.sldeditor.JStyleTree();

        pan_info.setLayout(new java.awt.GridLayout(1, 1));
        jScrollPane1.setViewportView(pan_info);

        jsp2.setViewportView(tree);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jsp2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jsp2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    public void apply() {
        if (editor != null) {
            editor.apply();
        }

        style = tree.getStyle();

        if (layer != null) {
            layer.setStyle(style);
        }
    }

    public void setLayer(MapLayer layer) {
        this.layer = layer;
    }

    public MapLayer getLayer() {
        return layer;
    }

    public void setEdited(Style style) {
        this.style = style;
        parse();
    }

    public Style getEdited() {
        style = tree.getStyle();
        apply();
        return style;
    }

    private void parse() {
        tree.setStyle(style);
    }

    public ImageIcon getIcon() {
        return IconBundle.getResource().getIcon("16_style");
    }

    public String getTitle() {
        return "advanced";
    }

    public void setTarget(Object layer) {

        if (layer instanceof MapLayer) {
            setLayer((MapLayer) layer);
            setEdited(this.layer.getStyle());
        }
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
    private javax.swing.JScrollPane jsp2;
    private javax.swing.JPanel pan_info;
    private org.puzzle.puzzlecore.gtextend.widget.sldeditor.JStyleTree tree;
    // End of variables declaration//GEN-END:variables
}
