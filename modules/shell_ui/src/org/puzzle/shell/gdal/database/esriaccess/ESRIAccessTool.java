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
package org.puzzle.shell.gdal.database.esriaccess;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileFilter;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.jdesktop.swingx.JXTitledSeparator;
import org.openide.util.NbBundle;
import org.puzzle.shell.gdal.ENV;
import org.puzzle.shell.gdal.GDALTool;
import org.puzzle.shell.misc.runtime.JRuntimePanel;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * Panneau pour l'extraction ESRI geodatabase
 * @author johann sorel
 * @author romain govaere
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


        buttonGroup1 = new ButtonGroup();
        jPanel5 = new JPanel();
        jLabel5 = new JLabel();
        jtf_base = new JTextField();
        detail = new JButton();
        chercher = new JButton();
        jPanel4 = new JPanel();
        lbl_sortie = new JLabel();
        es_sortie = new JTextField();
        es_jrb_toutes = new JRadioButton();
        es_jrb_une = new JRadioButton();
        es_nomcouche = new JTextField();
        jLabel1 = new JLabel();
        es_chercher = new JButton();
        jXTitledSeparator1 = new JXTitledSeparator();

        jPanel5.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ESRIAccessTool.class, "database"))); // NOI18N
        ResourceBundle bundle = ResourceBundle.getBundle("org/puzzle/shell/gdal/database/esriaccess/Bundle"); // NOI18N
        jLabel5.setText(bundle.getString("access_db")); // NOI18N

        detail.setText(NbBundle.getMessage(ESRIAccessTool.class, "detail")); // NOI18N
        detail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                actionDetail(evt);
            }
        });

        chercher.setText(NbBundle.getMessage(ESRIAccessTool.class, "...")); // NOI18N
        chercher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                actionChercher(evt);
            }
        });

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);

        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_base, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(chercher))
                    .addComponent(detail, Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtf_base, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(chercher))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(detail))
        );

        jPanel4.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ESRIAccessTool.class, "settings"))); // NOI18N
        lbl_sortie.setText(bundle.getString("output")); // NOI18N

        es_sortie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                esActionEdit(evt);
            }
        });
        es_sortie.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                esActionLost(evt);
            }
        });

        buttonGroup1.add(es_jrb_toutes);
        es_jrb_toutes.setSelected(true);
        es_jrb_toutes.setText(bundle.getString("all_layers")); // NOI18N
        es_jrb_toutes.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        es_jrb_toutes.setMargin(new Insets(0, 0, 0, 0));
        es_jrb_toutes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                esActionToutes(evt);
            }
        });

        buttonGroup1.add(es_jrb_une);
        es_jrb_une.setText(bundle.getString("one_layer")); // NOI18N

        es_jrb_une.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        es_jrb_une.setMargin(new Insets(0, 0, 0, 0));
        es_jrb_une.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                esActionUne(evt);
            }
        });

        jLabel1.setText(NbBundle.getMessage(ESRIAccessTool.class, "name")); // NOI18N
        es_chercher.setText(NbBundle.getMessage(ESRIAccessTool.class, "...")); // NOI18N
        es_chercher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                esActionChercher(evt);
            }
        });

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);

        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(es_jrb_toutes)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(es_jrb_une)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(es_nomcouche, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                    .addComponent(lbl_sortie)
                    .addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(es_sortie, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(es_chercher)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(es_jrb_toutes)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(es_jrb_une)
                    .addComponent(jLabel1)
                    .addComponent(es_nomcouche, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(lbl_sortie)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(es_chercher)
                    .addComponent(es_sortie, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledSeparator1.setTitle(NbBundle.getMessage(ESRIAccessTool.class, "title")); // NOI18N
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jXTitledSeparator1, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTitledSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void esActionUne(ItemEvent evt) {//GEN-FIRST:event_esActionUne
        esTestName();
    }//GEN-LAST:event_esActionUne

    private void esActionToutes(ItemEvent evt) {//GEN-FIRST:event_esActionToutes
        esTestName();
    }//GEN-LAST:event_esActionToutes

    private void actionDetail(ActionEvent evt) {//GEN-FIRST:event_actionDetail

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

    private void actionChercher(ActionEvent evt) {//GEN-FIRST:event_actionChercher

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

    private void esActionLost(FocusEvent evt) {//GEN-FIRST:event_esActionLost
        esTestName();
    }//GEN-LAST:event_esActionLost

    private void esActionEdit(ActionEvent evt) {//GEN-FIRST:event_esActionEdit
        esTestName();
    }//GEN-LAST:event_esActionEdit

    private void esActionChercher(ActionEvent evt) {//GEN-FIRST:event_esActionChercher
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
    private ButtonGroup buttonGroup1;
    private JButton chercher;
    private JButton detail;
    private JButton es_chercher;
    private JRadioButton es_jrb_toutes;
    private JRadioButton es_jrb_une;
    private JTextField es_nomcouche;
    private JTextField es_sortie;
    private JLabel jLabel1;
    private JLabel jLabel5;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JXTitledSeparator jXTitledSeparator1;
    private JTextField jtf_base;
    private JLabel lbl_sortie;
    // End of variables declaration//GEN-END:variables
    
    
}
