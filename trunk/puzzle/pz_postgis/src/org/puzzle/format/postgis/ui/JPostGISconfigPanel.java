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

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.postgis.PostgisDataStoreFactory;

/**
 * @author Johann Sorel (Püzzle-GIS)
 */
public class JPostGISconfigPanel extends javax.swing.JPanel {

    public JPostGISconfigPanel() {
        initComponents();

        final Map<String,Object> params = new HashMap<String, Object>();
        params.put(PostgisDataStoreFactory.DBTYPE.key, PostgisDataStoreFactory.DBTYPE.sample);
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

        setParams(params);
        
    }

    public void setParams(Map map) {

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

    public Map<String,String> getParams(){
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
        return params;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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

        jXTitledPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jXTitledPanel2.setTitle(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jXTitledPanel2.title")); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel7.text")); // NOI18N

        jLabel8.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel8.text")); // NOI18N

        chk_validate.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.chk_validate.text")); // NOI18N

        chk_wkb.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.chk_wkb.text")); // NOI18N

        chk_loose.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.chk_loose.text")); // NOI18N

        chk_estimated.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.chk_estimated.text")); // NOI18N

        jLabel13.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel13.text")); // NOI18N

        javax.swing.GroupLayout jXTitledPanel2Layout = new javax.swing.GroupLayout(jXTitledPanel2);
        jXTitledPanel2.setLayout(jXTitledPanel2Layout);
        jXTitledPanel2Layout.setHorizontalGroup(
            jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chk_estimated)
                    .addComponent(chk_loose)
                    .addComponent(chk_wkb)
                    .addGroup(jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jsp_max_connects, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jsp_min_connects)))
                    .addComponent(chk_validate)
                    .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_namespace, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel2Layout.setVerticalGroup(
            jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jsp_max_connects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jsp_min_connects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_validate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_wkb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_loose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_estimated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtf_namespace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jXTitledPanel1.setTitle(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jXTitledPanel1.title")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel4.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel5.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getBundle(JPostGISconfigPanel.class).getString("JPostGISconfigPanel.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout jXTitledPanel1Layout = new javax.swing.GroupLayout(jXTitledPanel1);
        jXTitledPanel1.setLayout(jXTitledPanel1Layout);
        jXTitledPanel1Layout.setHorizontalGroup(
            jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_host, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_schema, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_database, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_user, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_password, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel1Layout.setVerticalGroup(
            jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtf_host, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtf_schema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtf_database, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtf_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXTitledPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jXTitledPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXTitledPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXTitledPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables

}
