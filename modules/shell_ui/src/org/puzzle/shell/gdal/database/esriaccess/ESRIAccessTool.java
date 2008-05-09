/*
 * DBPostGIS.java
 *
 * Created on 22 mars 2007, 09:30
 *
 * Copyright 2007 Johann Sorel, Romain Govaere
 *
 * AlterSIG-Convert is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * AlterSIG-Convert is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.puzzle.shell.gdal.database.esriaccess;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.puzzle.shell.gdal.ENV;
import org.puzzle.shell.gdal.GDALTool;
import org.puzzle.shell.misc.runtime.JRuntimePanel;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * Panneau pour l'extraction ESRI geodatabase
 * @author  johann sorel
 */
public class ESRIAccessTool extends JPanel implements GDALTool{

    private ResourceBundle bundle = ResourceBundle.getBundle("org/puzzle/shell/gdal/database/esriaccess/Bundle");
    
    
    String repertoire = "";

    /** Creates new form DBPostGIS */
    public ESRIAccessTool() {
        initComponents();
    }

    private void esTestName() {
        String nom_sortie = es_sortie.getText();
        String nouveaunom = "";
        String extension_fichier = "";
        int index = -1;


        if (es_jrb_toutes.isSelected()) {

            // on corrige l'extension
            if (!nom_sortie.toLowerCase().endsWith(File.separator)) {
                if (!nom_sortie.equals("")) {
                    nouveaunom = nom_sortie; //+ File.separator;
                }
            } else {
                nouveaunom = nom_sortie;
            }
        } else {

            //recuperation de l'extension du fichier en sortie
            index = nom_sortie.lastIndexOf(".");
            if (index != -1) {
                extension_fichier = nom_sortie.substring(index, nom_sortie.length());
            }

            // on corrige l'extension
            if (!nom_sortie.toLowerCase().endsWith(".shp")) {

                if (nom_sortie.endsWith(File.separator)) {
                    nom_sortie += "temp";
                }

                if (!nom_sortie.equals("")) {
                    if (index == -1) {
                        nouveaunom = nom_sortie + ".shp";
                    } else {
                        nouveaunom = nom_sortie.substring(0, index) + ".shp";
                    }
                }

            } else {
                nouveaunom = nom_sortie;
            }

        }

        es_sortie.setText(nouveaunom);
    }

    public ProcessObject[] createProcesses() {
        
        ProcessObject po = null;
        
        String nom = "";


        if (jtf_base.getText().equals("") || es_sortie.getText().equals("")) {
            JOptionPane.showMessageDialog(this, bundle.getString("please_fill_all"), bundle.getString("missing_data"), JOptionPane.ERROR_MESSAGE);
        } else {

            if (es_jrb_toutes.isSelected()) {
                //ogr2ogr -f "Esri Shapefile"  monRepertoire Ma_Base_de_donnees_Esri.mdb

                String[] parametres = new String[5];
                parametres[0] = ENV.getOGR2OGRPath();
                parametres[1] = "-f";
                parametres[2] = "Esri Shapefile";
                parametres[3] = es_sortie.getText();
                parametres[4] = jtf_base.getText();
                repertoire = es_sortie.getText();
                nom = bundle.getString("folder") + es_sortie.getText();

                 po = new ProcessObject(parametres, nom);


            } else {
                if (es_nomcouche.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, bundle.getString("fill_layer_name"), bundle.getString("missing_data"), JOptionPane.ERROR_MESSAGE);
                } else {

                    //ogr2ogr -f "Esri Shapefile"  maCouche.shp Ma_Base_de_donnees_Esri.mdb -sql "select  * from maCouche"

                    /*String[] parametres = new String[7];
                    parametres[0] = ENV.OGR2OGR;
                    parametres[1] = "-f";
                    parametres[2] = "Esri Shapefile";
                    parametres[3] = es_sortie.getText();
                    parametres[4] = jtf_base.getText();
                    parametres[5] = "-sql";
                    parametres[6] = "\"select  * from "+es_nomcouche.getText() +"\"";*/

                    StringBuffer buffer = new StringBuffer();
                    buffer.append( "\"" + ENV.getOGR2OGRPath() + "\" " );
                    buffer.append( "-f \"Esri Shapefile\" \"" + es_sortie.getText() + "\" ");
                    buffer.append( "\"" + jtf_base.getText() + "\" " );
                    buffer.append( "\" -sql \"select * from " + es_nomcouche.getText() + "\" " );
                    
                    //String parametres = "\"" + ENV.FWOGR2OGR + "\" " + "-f \"Esri Shapefile\" \"" + es_sortie.getText() + "\" \"" + jtf_base.getText() + "\" -sql \"select * from " + es_nomcouche.getText() + "\" ";
                    String parametres = buffer.toString();
                    System.out.println(parametres);
                    String separateur = File.separator;
                    int i = es_sortie.getText().lastIndexOf(separateur);

                    repertoire = es_sortie.getText().substring(0, i - 1);
                    nom = es_sortie.getText().substring(i + 1, es_sortie.getText().length());

                    po = new ProcessObject(parametres, nom);

                }
            }

        }
        
        if(po!=null){
            return new ProcessObject[]{po};
        }else{
            return EMPTY_PROCESS_OBJECT_ARRAY;
        }
        
    }

    public JComponent getComponent() {
        return this;
    }
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtf_base = new javax.swing.JTextField();
        detail = new javax.swing.JButton();
        chercher = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lbl_sortie = new javax.swing.JLabel();
        es_sortie = new javax.swing.JTextField();
        es_jrb_toutes = new javax.swing.JRadioButton();
        es_jrb_une = new javax.swing.JRadioButton();
        es_nomcouche = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        es_chercher = new javax.swing.JButton();
        jXTitledSeparator1 = new org.jdesktop.swingx.JXTitledSeparator();

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "database"))); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/puzzle/shell/gdal/database/esriaccess/Bundle"); // NOI18N
        jLabel5.setText(bundle.getString("access_db")); // NOI18N

        detail.setText(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "detail")); // NOI18N
        detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionDetail(evt);
            }
        });

        chercher.setText(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "...")); // NOI18N
        chercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionChercher(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_base, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chercher))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, detail))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jtf_base, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chercher))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(detail))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "settings"))); // NOI18N

        lbl_sortie.setText(bundle.getString("output")); // NOI18N

        es_sortie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                esActionEdit(evt);
            }
        });
        es_sortie.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                esActionLost(evt);
            }
        });

        buttonGroup1.add(es_jrb_toutes);
        es_jrb_toutes.setSelected(true);
        es_jrb_toutes.setText(bundle.getString("all_layers")); // NOI18N
        es_jrb_toutes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        es_jrb_toutes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        es_jrb_toutes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                esActionToutes(evt);
            }
        });

        buttonGroup1.add(es_jrb_une);
        es_jrb_une.setText(bundle.getString("one_layer")); // NOI18N
        es_jrb_une.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        es_jrb_une.setMargin(new java.awt.Insets(0, 0, 0, 0));
        es_jrb_une.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                esActionUne(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "name")); // NOI18N

        es_chercher.setText(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "...")); // NOI18N
        es_chercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                esActionChercher(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(es_jrb_toutes)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(es_jrb_une)
                        .add(18, 18, 18)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(es_nomcouche, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                    .add(lbl_sortie)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .add(es_sortie, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(es_chercher)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(es_jrb_toutes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(es_jrb_une)
                    .add(jLabel1)
                    .add(es_nomcouche, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_sortie)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(es_chercher)
                    .add(es_sortie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledSeparator1.setTitle(org.openide.util.NbBundle.getMessage(ESRIAccessTool.class, "title")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jXTitledSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void esActionUne(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_esActionUne
        esTestName();
    }//GEN-LAST:event_esActionUne

    private void esActionToutes(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_esActionToutes
        esTestName();
    }//GEN-LAST:event_esActionToutes

    private void actionDetail(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionDetail

        //ogrinfo  Ma_Base_de_donnees_Esri.mdb > C:/temp/monFichier.txt

        String parametres[] = {
            ENV.getOGRINFOPath(),
            jtf_base.getText()
        };

        ProcessObject po = new ProcessObject(parametres, "");

        JDialog dia = new JDialog();
        JRuntimePanel jrp = new JRuntimePanel(po, JRuntimePanel.TYPE.LISTEN_BOTH);
        dia.add(jrp);
        dia.setSize(400, 300);
        dia.setLocationRelativeTo(null);
        po.start();

        dia.setVisible(true);
    }//GEN-LAST:event_actionDetail

    private void actionChercher(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionChercher

        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ACCESS_DATABASE));

        jfc.setFileFilter(jfc.getChoosableFileFilters()[1]);

        jfc.setCurrentDirectory(ENV.getPath());

        int val = jfc.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            jtf_base.setText(f.getPath());
            ENV.updatePath(f.getAbsoluteFile());
        }
    }//GEN-LAST:event_actionChercher

    private void esActionLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_esActionLost
        esTestName();
    }//GEN-LAST:event_esActionLost

    private void esActionEdit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_esActionEdit
        esTestName();
    }//GEN-LAST:event_esActionEdit

    private void esActionChercher(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_esActionChercher
        JFileChooser jfc = new JFileChooser();


        if (es_jrb_toutes.isSelected()) {
            jfc.addChoosableFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "folder";
                }
            }
            );
            jfc.setFileFilter(jfc.getChoosableFileFilters()[1]);
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            jfc.setCurrentDirectory(ENV.getPath());

            int val = jfc.showSaveDialog(this);

            if (val == JFileChooser.APPROVE_OPTION) {
                File f = jfc.getSelectedFile();
                es_sortie.setText(f.getPath());
                ENV.updatePath(f.getAbsoluteFile());
            }
        } else {
            jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE));
            jfc.setFileFilter(jfc.getChoosableFileFilters()[1]);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            jfc.setCurrentDirectory(ENV.getPath());

            int val = jfc.showSaveDialog(this);

            if (val == JFileChooser.APPROVE_OPTION) {
                File f = jfc.getSelectedFile();
                es_sortie.setText(f.getPath());
                ENV.updatePath(f.getAbsoluteFile());
            }
        }

        esTestName();
    }//GEN-LAST:event_esActionChercher
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton chercher;
    private javax.swing.JButton detail;
    private javax.swing.JButton es_chercher;
    private javax.swing.JRadioButton es_jrb_toutes;
    private javax.swing.JRadioButton es_jrb_une;
    private javax.swing.JTextField es_nomcouche;
    private javax.swing.JTextField es_sortie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator1;
    private javax.swing.JTextField jtf_base;
    private javax.swing.JLabel lbl_sortie;
    // End of variables declaration//GEN-END:variables
    
    
}
