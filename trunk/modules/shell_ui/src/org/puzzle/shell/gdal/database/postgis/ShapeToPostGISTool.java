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

package org.puzzle.shell.gdal.database.postgis;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.jdesktop.swingx.JXTitledSeparator;
import org.openide.util.NbBundle;
import org.puzzle.shell.gdal.ENV;
import org.puzzle.shell.gdal.GDALTool;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * panneau pour l'import/export de donnees POSTGIS
 * @author johann sorel
 */
public class ShapeToPostGISTool extends JPanel implements GDALTool {
    
    private final ResourceBundle bundle = ResourceBundle.getBundle("org/puzzle/shell/gdal/database/postgis/Bundle");
    
    String repertoire = "";
    
    
    
    /** Creates new form DBPostGIS */
    public ShapeToPostGISTool() {
        initComponents();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {


        jPanel5 = new JPanel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jtf_login = new JTextField();
        jtf_db = new JTextField();
        jtf_ip = new JTextField();
        jtf_mdp = new JPasswordField();
        jLabel9 = new JLabel();
        jtf_table = new JTextField();
        jXTitledSeparator1 = new JXTitledSeparator();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        sp_jtf_entree = new JTextField();
        sp_chercher = new JButton();

        jPanel5.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ShapeToPostGISTool.class, "database"))); // NOI18N
        ResourceBundle bundle = ResourceBundle.getBundle("org/puzzle/shell/gdal/database/postgis/Bundle"); // NOI18N
        jLabel5.setText(bundle.getString("id")); // NOI18N

        jLabel6.setText(bundle.getString("password")); // NOI18N

        jLabel7.setText(bundle.getString("ip")); // NOI18N


        jLabel8.setText(NbBundle.getMessage(ShapeToPostGISTool.class, "thedatabase")); // NOI18N
        jtf_ip.setText("127.0.0.1");

        jLabel9.setText(bundle.getString("table")); // NOI18N

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
                        .addComponent(jtf_login, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_mdp, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_table, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_db, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_ip, GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtf_login, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtf_mdp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtf_table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtf_db, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtf_ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledSeparator1.setTitle(NbBundle.getMessage(ShapeToPostGISTool.class, "title2")); // NOI18N
        jPanel1.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ShapeToPostGISTool.class, "file"))); // NOI18N
        jLabel1.setText(NbBundle.getMessage(ShapeToPostGISTool.class, "input")); // NOI18N
        sp_chercher.setText(bundle.getString("...")); // NOI18N
        sp_chercher.setPreferredSize(new Dimension(45, 20));
        sp_chercher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                spActionChercher(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(sp_jtf_entree, GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(sp_chercher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(sp_chercher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sp_jtf_entree, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(jPanel1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTitledSeparator1, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
                            
    private void spActionChercher(ActionEvent evt) {//GEN-FIRST:event_spActionChercher
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.MICROSTATION_DGN));
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.AUTOCAD_DWG));
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.GEOGRAPHY_MARKUP_LANGUAGE));
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.KEYHOLE_MARKUP_LANGUAGE));
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.MAPINFO_EXCHANGE));
        jfc.addChoosableFileFilter(FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE));
        
        jfc.setFileFilter(jfc.getChoosableFileFilters()[1]);
        
        jfc.setCurrentDirectory(ENV.getPath());
        
        int val = jfc.showOpenDialog(this);
        
        if(val == JFileChooser.APPROVE_OPTION){
            File f = jfc.getSelectedFile();
            sp_jtf_entree.setText( f.getPath() );
            ENV.updatePath(f.getAbsoluteFile());
        }
    }//GEN-LAST:event_spActionChercher
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel5;
    private JXTitledSeparator jXTitledSeparator1;
    private JTextField jtf_db;
    private JTextField jtf_ip;
    private JTextField jtf_login;
    private JPasswordField jtf_mdp;
    private JTextField jtf_table;
    private JButton sp_chercher;
    private JTextField sp_jtf_entree;
    // End of variables declaration//GEN-END:variables
    

    public ProcessObject[] createProcesses() {
        
        ProcessObject po = null;
        
        int i = sp_jtf_entree.getText().lastIndexOf(File.separator);
        repertoire = sp_jtf_entree.getText().substring(0,i-1);
        String nom = sp_jtf_entree.getText().substring(i+1,sp_jtf_entree.getText().length());
        String login = jtf_login.getText();
        String mdp = String.valueOf( jtf_mdp.getPassword() );
        String table = jtf_table.getText();
        String base = jtf_db.getText();
        String ip = jtf_ip.getText();
        String fichier = sp_jtf_entree.getText();
        
        if(ip.equals(bundle.getString(""))){
            ip = bundle.getString("127.0.0.1");
        }
        
        
        if(table.equals(bundle.getString("")) || base.equals(bundle.getString("")) || fichier.equals(bundle.getString(""))){
            JOptionPane.showMessageDialog(this,bundle.getString("please_fill_all"),bundle.getString("missing_data"),JOptionPane.ERROR_MESSAGE);
            
        } else {
            
        /*String[] parametres = new String[7];
        parametres[0] = ENV.OGR2OGR;
        parametres[1] = "-f";
        parametres[2] = "PostgreSQL";
        parametres[3] = "PG:\"host="+ip+" user="+login+" dbname="+base+" password="+mdp+"\" " ;
        parametres[4] = fichier;
        parametres[5] = "-nln";
        parametres[6] = table;*/
            
            String parametres =
                    ENV.getOGR2OGRPath()  + " " +
                    "-f " +
                    "\"" + "PostgreSQL" + "\" " +
                    "PG:\"host=" + ip + " user=" + login + " dbname=" + base + " password=" + mdp + "\" " +
                    "\"" + fichier + "\" " +
                    "-nln " + table ;
            
            System.out.println(parametres);
            
            po = new ProcessObject(parametres,nom);
            
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
    
    
}