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
import org.geotools.gui.swing.style.StyleElementEditor;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;

/**
 *
 * @author johann sorel
 */
public class JStylePane extends javax.swing.JPanel implements StyleElementEditor<Style>{
    
    private MapLayer layer = null;
    private Style style = null;
    
    /** Creates new form JRulePanel */
    public JStylePane() {
        initComponents();
    }
    
    private void parse(){
        if(style != null){
            style.getAbstract();
            style.getName();
            jtf_title.setText( style.getTitle() );
        }
    }
    
    public void setEdited(Style style){
        this.style = style;
        parse();
    }
    
    public Style getEdited(){
        return style;
    }
    
    public void apply() {
        style.setTitle( jtf_title.getText() );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtf_title = new javax.swing.JTextField();

        jLabel1.setText("title");

        jtf_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_titleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtf_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jtf_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(270, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtf_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_titleActionPerformed
        style.setTitle(jtf_title.getText());
    }//GEN-LAST:event_jtf_titleActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jtf_title;
    // End of variables declaration//GEN-END:variables

    public void setLayer(MapLayer layer) {
        this.layer = layer;
    }

    public MapLayer getLayer() {
        return layer;
    }


    public Component getComponent() {
        return this;
    }
    
}
