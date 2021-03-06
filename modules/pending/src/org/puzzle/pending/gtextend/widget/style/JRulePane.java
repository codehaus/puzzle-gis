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
package org.puzzle.pending.gtextend.widget.style;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.geotools.gui.swing.misc.FilterToCQL;
import org.geotools.gui.swing.propertyedit.filterproperty.JCQLPropertyPanel;
import org.geotools.gui.swing.style.StyleElementEditor;
import org.geotools.map.MapLayer;
import org.geotools.style.MutableRule;

/**
 *
 * @author johann sorel
 */
public class JRulePane extends javax.swing.JPanel{ //implements StyleElementEditor<Rule> {

    private MutableRule rule = null;
    
    private MapLayer layer = null;
    

    /** Creates new form JRulePanel */
    public JRulePane() {
        initComponents();
    }

//    private void parse() {
//        if (rule != null) {
//            rule.getAbstract();
//            rule.getLegendGraphic();
//            jsp_maxscale.setValue(rule.getMaxScaleDenominator());
//            jsp_minscale.setValue(rule.getMinScaleDenominator());
//            jtf_name.setText(rule.getName());
//            jtf_title.setText(rule.getTitle());
//
//            if (rule.getFilter() != null) {
//                FilterToCQL visitor = new FilterToCQL();
//                try {
//                    jtp_filter.setText(visitor.encodeToString(rule.getFilter()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void setEdited(Rule rule) {
//        this.rule = rule;
//        parse();
//    }
//
//    public Rule getEdited() {
//        apply();
//        return rule;
//    }
//
//    public void apply() {
//        rule.setMaxScaleDenominator((Double) jsp_maxscale.getValue());
//        rule.setMinScaleDenominator((Double) jsp_minscale.getValue());
//        rule.setName(jtf_name.getText());
//        rule.setTitle(jtf_title.getText());
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtf_title = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtf_name = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jsp_minscale = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jsp_maxscale = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtp_filter = new javax.swing.JTextPane();
        but_edit = new javax.swing.JButton();

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "title")); // NOI18N

        jtf_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_titleActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "name")); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "minscale")); // NOI18N

        jsp_minscale.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1000.0d)));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "maxscale")); // NOI18N

        jsp_maxscale.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1000.0d)));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "filter")); // NOI18N

        jtp_filter.setEditable(false);
        jScrollPane1.setViewportView(jtp_filter);

        but_edit.setText(org.openide.util.NbBundle.getMessage(JRulePane.class, "edit")); // NOI18N
        but_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but_editActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_name, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(jLabel4)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jsp_maxscale))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(jLabel3)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jsp_minscale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 211, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                            .add(but_edit))))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jtf_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jtf_name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jsp_minscale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jsp_maxscale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(but_edit))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jtf_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_titleActionPerformed
//        rule.setTitle(jtf_title.getText());
    }//GEN-LAST:event_jtf_titleActionPerformed

    private void but_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but_editActionPerformed

        JDialog dialog = new JDialog();
        JCQLPropertyPanel cql = new JCQLPropertyPanel();
        if(rule.getFilter() != null){
            cql.setFilter(rule.getFilter());
        }

        dialog.setContentPane(cql);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo((JButton) evt.getSource());
        dialog.setVisible(true);

        rule.setFilter(cql.getFilter());
                
    }//GEN-LAST:event_but_editActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton but_edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jsp_maxscale;
    private javax.swing.JSpinner jsp_minscale;
    private javax.swing.JTextField jtf_name;
    private javax.swing.JTextField jtf_title;
    private javax.swing.JTextPane jtp_filter;
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
