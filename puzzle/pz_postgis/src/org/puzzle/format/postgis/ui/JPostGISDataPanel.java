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
package org.puzzle.format.postgis.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapLayer;
import org.geotools.map.MapBuilder;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import org.openide.util.Exceptions;
import org.puzzle.core.project.source.SourceCreationPane;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * PostGIS databaseChooser
 * 
 * @author Johann Sorel
 */
public class JPostGISDataPanel extends SourceCreationPane {

    private DataStore store;
    private final Map<String,Object> params = new HashMap<String, Object>();

    /** Creates new form DefaultShapeTypeChooser */
    public JPostGISDataPanel() {
        initComponents();

        params.put("dbtype", "postgis");

        params.put(PostgisDataStoreFactory.HOST.key, PostgisDataStoreFactory.HOST.sample);
        params.put(PostgisDataStoreFactory.PORT.key, PostgisDataStoreFactory.PORT.sample);
        params.put(PostgisDataStoreFactory.SCHEMA.key, PostgisDataStoreFactory.SCHEMA.sample);
        params.put(PostgisDataStoreFactory.DATABASE.key, PostgisDataStoreFactory.DATABASE.sample);
        params.put(PostgisDataStoreFactory.USER.key, PostgisDataStoreFactory.USER.sample);
        params.put(PostgisDataStoreFactory.PASSWD.key, PostgisDataStoreFactory.PASSWD.sample);
        params.put(PostgisDataStoreFactory.MAXCONN.key, PostgisDataStoreFactory.MAXCONN.sample);
        params.put(PostgisDataStoreFactory.MINCONN.key, PostgisDataStoreFactory.MINCONN.sample);
        params.put(PostgisDataStoreFactory.NAMESPACE.key, PostgisDataStoreFactory.NAMESPACE.sample);
        params.put(PostgisDataStoreFactory.VALIDATECONN.key, PostgisDataStoreFactory.VALIDATECONN.sample);
        params.put(PostgisDataStoreFactory.ESTIMATEDEXTENT.key, PostgisDataStoreFactory.ESTIMATEDEXTENT.sample);
        params.put(PostgisDataStoreFactory.LOOSEBBOX.key, PostgisDataStoreFactory.LOOSEBBOX.sample);
        params.put(PostgisDataStoreFactory.WKBENABLED.key, PostgisDataStoreFactory.WKBENABLED.sample);

        setProperties(params);
                
//        jtf_host.setToolTipText(PostgisDataStoreFactory.HOST.description.toString());
//        jtf_port.setToolTipText(PostgisDataStoreFactory.PORT.description.toString());
//        jtf_schema.setToolTipText(PostgisDataStoreFactory.SCHEMA.description.toString());
//        jtf_database.setToolTipText(PostgisDataStoreFactory.DATABASE.description.toString());
//        jtf_user.setToolTipText(PostgisDataStoreFactory.USER.description.toString());
//        jtf_password.setToolTipText(PostgisDataStoreFactory.PASSWD.description.toString());
//        jsp_max_connects.setToolTipText(PostgisDataStoreFactory.MAXCONN.description.toString());
//        jsp_min_connects.setToolTipText(PostgisDataStoreFactory.MINCONN.description.toString());
//        jtf_namespace.setToolTipText(PostgisDataStoreFactory.NAMESPACE.description.toString());
//        chk_validate.setToolTipText(PostgisDataStoreFactory.VALIDATECONN.description.toString());
//        chk_estimated.setToolTipText(PostgisDataStoreFactory.ESTIMATEDEXTENT.description.toString());
//        chk_loose.setToolTipText(PostgisDataStoreFactory.LOOSEBBOX.description.toString());
//        chk_wkb.setToolTipText(PostgisDataStoreFactory.WKBENABLED.description.toString());

        tab_table.setTableHeader(null);
        tab_table.setModel(new DBModel(tab_table));

    }

    public Map getProperties() {
        return params;
    }

    public void setProperties(Map map) {

        if(map == null){
            throw new NullPointerException();
        }
        
        Object val = null;
        
        val = map.get(PostgisDataStoreFactory.HOST.key);
        jtf_host.setText( (val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.PORT.key);
        jtf_port.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.SCHEMA.key);
        jtf_schema.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.DATABASE.key);
        jtf_database.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.USER.key);
        jtf_user.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.PASSWD.key);
        jtf_password.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.MAXCONN.key);
        jsp_max_connects.setValue((val == null) ? 10 : val);
        val = map.get(PostgisDataStoreFactory.MINCONN.key);
        jsp_min_connects.setValue((val == null) ? 4 : val);
        val = map.get(PostgisDataStoreFactory.NAMESPACE.key);
        jtf_namespace.setText((val == null) ? "" : val.toString());
        val = map.get(PostgisDataStoreFactory.VALIDATECONN.key);
        chk_validate.setSelected( (val == null) ? (Boolean) PostgisDataStoreFactory.VALIDATECONN.sample : (Boolean)val);
        val = map.get(PostgisDataStoreFactory.ESTIMATEDEXTENT.key);
        chk_estimated.setSelected((val == null) ? (Boolean) PostgisDataStoreFactory.ESTIMATEDEXTENT.sample : (Boolean)val);
        val = map.get(PostgisDataStoreFactory.LOOSEBBOX.key);
        chk_loose.setSelected( (val == null) ? (Boolean) PostgisDataStoreFactory.LOOSEBBOX.sample : (Boolean)val);
        val = map.get(PostgisDataStoreFactory.WKBENABLED.key);
        chk_wkb.setSelected( (val == null) ? (Boolean) PostgisDataStoreFactory.WKBENABLED.sample : (Boolean)val);
        
    }

    private void refreshTable() {

        if (store != null) {
            ((DBModel) tab_table.getModel()).clean();
            try {
                ((DBModel) tab_table.getModel()).add(store.getTypeNames());
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        but_refresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab_table = new org.jdesktop.swingx.JXTable();
        jXTitledPanel1 = new org.jdesktop.swingx.JXTitledPanel();
        jLabel1 = new javax.swing.JLabel();
        jtf_host = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtf_port = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtf_schema = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtf_database = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtf_user = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtf_password = new javax.swing.JPasswordField();
        jXTitledPanel2 = new org.jdesktop.swingx.JXTitledPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jsp_min_connects = new javax.swing.JSpinner();
        jsp_max_connects = new javax.swing.JSpinner();
        chk_validate = new javax.swing.JCheckBox();
        chk_wkb = new javax.swing.JCheckBox();
        chk_loose = new javax.swing.JCheckBox();
        chk_estimated = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jtf_namespace = new javax.swing.JTextField();

        setOpaque(false);

        but_refresh.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("test")); // NOI18N
        but_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionRefresh(evt);
            }
        });

        tab_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tab_table);

        jXTitledPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jXTitledPanel1.setTitle(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("connection")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("host")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("port")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("schema")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("database")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("user")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("password")); // NOI18N

        org.jdesktop.layout.GroupLayout jXTitledPanel1Layout = new org.jdesktop.layout.GroupLayout(jXTitledPanel1.getContentContainer());
        jXTitledPanel1.getContentContainer().setLayout(jXTitledPanel1Layout);
        jXTitledPanel1Layout.setHorizontalGroup(
            jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_host, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_port, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_schema, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_database, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_user, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                    .add(jXTitledPanel1Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel1Layout.setVerticalGroup(
            jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jtf_host, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jtf_port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jtf_schema, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jtf_database, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jtf_user, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jtf_password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jXTitledPanel2.setTitle(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("advanced")); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("max_connects")); // NOI18N

        jLabel8.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("min_connects")); // NOI18N

        jsp_min_connects.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));

        jsp_max_connects.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));

        chk_validate.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("validate_connects")); // NOI18N
        chk_validate.setOpaque(false);

        chk_wkb.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("wkb_enabled")); // NOI18N
        chk_wkb.setOpaque(false);

        chk_loose.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("loose_bbox")); // NOI18N
        chk_loose.setOpaque(false);

        chk_estimated.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("estimated_extend")); // NOI18N
        chk_estimated.setOpaque(false);

        jLabel13.setText(org.openide.util.NbBundle.getBundle(JPostGISDataPanel.class).getString("namespace")); // NOI18N

        org.jdesktop.layout.GroupLayout jXTitledPanel2Layout = new org.jdesktop.layout.GroupLayout(jXTitledPanel2.getContentContainer());
        jXTitledPanel2.getContentContainer().setLayout(jXTitledPanel2Layout);
        jXTitledPanel2Layout.setHorizontalGroup(
            jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chk_estimated)
                    .add(chk_loose)
                    .add(chk_wkb)
                    .add(jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jXTitledPanel2Layout.createSequentialGroup()
                            .add(jLabel7)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jsp_max_connects, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jXTitledPanel2Layout.createSequentialGroup()
                            .add(jLabel8)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jsp_min_connects)))
                    .add(chk_validate)
                    .add(jXTitledPanel2Layout.createSequentialGroup()
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtf_namespace, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel2Layout.setVerticalGroup(
            jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(jsp_max_connects, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jsp_min_connects, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chk_validate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chk_wkb)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chk_loose)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chk_estimated)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(jtf_namespace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jXTitledPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jXTitledPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .add(but_refresh, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
        );

        layout.linkSize(new java.awt.Component[] {jXTitledPanel1, jXTitledPanel2}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(but_refresh)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(jXTitledPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXTitledPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void actionRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionRefresh

        params.put(PostgisDataStoreFactory.HOST.key, jtf_host.getText());
        params.put(PostgisDataStoreFactory.PORT.key, jtf_port.getText());
        params.put(PostgisDataStoreFactory.SCHEMA.key, jtf_schema.getText());
        params.put(PostgisDataStoreFactory.DATABASE.key, jtf_database.getText());
        params.put(PostgisDataStoreFactory.USER.key, jtf_user.getText());
        params.put(PostgisDataStoreFactory.PASSWD.key, new String(jtf_password.getPassword()));
        params.put(PostgisDataStoreFactory.MAXCONN.key, jsp_max_connects.getValue());
        params.put(PostgisDataStoreFactory.MINCONN.key, jsp_min_connects.getValue());
        params.put(PostgisDataStoreFactory.NAMESPACE.key, jtf_namespace.getText());
        params.put(PostgisDataStoreFactory.VALIDATECONN.key, chk_validate.isSelected());
        params.put(PostgisDataStoreFactory.ESTIMATEDEXTENT.key, chk_estimated.isSelected());
        params.put(PostgisDataStoreFactory.LOOSEBBOX.key, chk_loose.isSelected());
        params.put(PostgisDataStoreFactory.WKBENABLED.key, chk_wkb.isSelected());


        but_refresh.setEnabled(false);
        new Thread() {

            @Override
            public void run() {
                try {
                    store = DataStoreFinder.getDataStore(params);
                    refreshTable();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    store = null;
                }
            but_refresh.setEnabled(true);
            }
        }.start();
        
    }//GEN-LAST:event_actionRefresh

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton but_refresh;
    private javax.swing.JCheckBox chk_estimated;
    private javax.swing.JCheckBox chk_loose;
    private javax.swing.JCheckBox chk_validate;
    private javax.swing.JCheckBox chk_wkb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel1;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel2;
    private javax.swing.JSpinner jsp_max_connects;
    private javax.swing.JSpinner jsp_min_connects;
    private javax.swing.JTextField jtf_database;
    private javax.swing.JTextField jtf_host;
    private javax.swing.JTextField jtf_namespace;
    private javax.swing.JPasswordField jtf_password;
    private javax.swing.JTextField jtf_port;
    private javax.swing.JTextField jtf_schema;
    private javax.swing.JTextField jtf_user;
    private org.jdesktop.swingx.JXTable tab_table;
    // End of variables declaration//GEN-END:variables
    
    public MapLayer[] getLayers() {
        ArrayList<MapLayer> layers = new ArrayList<MapLayer>();
        RandomStyleFactory rsf = new RandomStyleFactory();

        if (store != null) {

            for (int i = 0; i < tab_table.getSelectedRows().length; i++) {
                try {
                    DBModel model = (DBModel) tab_table.getModel();
                    String name = (String) model.getValueAt(tab_table.getSelectedRows()[i], 0);
                    FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(name);
                    MutableStyle style = rsf.createRandomVectorStyle(fs);

                    MapBuilder mlb = new MapBuilder();
                    MapLayer layer = mlb.create(fs, style);
                    layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription("postgis - " + name, ""));
                    layers.add(layer);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

        }

        return layers.toArray(new MapLayer[layers.size()]);
    }

    public Map<String,GISSourceInfo> createSources() {
        final Map<String,String> params = new HashMap<String,String>();
        params.put(PostgisDataStoreFactory.HOST.key, jtf_host.getText());
        params.put(PostgisDataStoreFactory.PORT.key, jtf_port.getText());
        params.put(PostgisDataStoreFactory.SCHEMA.key, jtf_schema.getText());
        params.put(PostgisDataStoreFactory.DATABASE.key, jtf_database.getText());
        params.put(PostgisDataStoreFactory.USER.key, jtf_user.getText());
        params.put(PostgisDataStoreFactory.PASSWD.key, new String(jtf_password.getPassword()));
        params.put(PostgisDataStoreFactory.MAXCONN.key, jsp_max_connects.getValue().toString());
        params.put(PostgisDataStoreFactory.MINCONN.key, jsp_min_connects.getValue().toString());
        params.put(PostgisDataStoreFactory.NAMESPACE.key, jtf_namespace.getText());
        params.put(PostgisDataStoreFactory.VALIDATECONN.key, Boolean.valueOf(chk_validate.isSelected()).toString());
        params.put(PostgisDataStoreFactory.ESTIMATEDEXTENT.key, Boolean.valueOf(chk_estimated.isSelected()).toString());
        params.put(PostgisDataStoreFactory.LOOSEBBOX.key, Boolean.valueOf(chk_loose.isSelected()).toString());
        params.put(PostgisDataStoreFactory.WKBENABLED.key, Boolean.valueOf(chk_wkb.isSelected()).toString());
        final GISSourceInfo info = new GISSourceInfo(-1, TOOL_TIP_TEXT_KEY, params);
        final String name = jtf_host.getText() +"/"+jtf_schema.getText()+"/"+jtf_database.getText();
        final Map<String,GISSourceInfo> sources = new HashMap<String, GISSourceInfo>();
        sources.put(name, info);
        return sources;
    }
}
