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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import static org.geotools.data.postgis.PostgisNGDataStoreFactory.*;

import org.jdesktop.swingx.JXTitledPanel;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.postgis.service.PostGISSourceService;

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

        params.put(DBTYPE.key, "postgisng");
        params.put(HOST.key, HOST.sample);
        params.put(PORT.key, PORT.sample);
        params.put(SCHEMA.key, SCHEMA.sample);
        params.put(DATABASE.key, DATABASE.sample);
        params.put(USER.key, USER.sample);
        params.put(PASSWD.key, PASSWD.sample);
        params.put(MAXCONN.key, MAXCONN.sample);
        params.put(MINCONN.key, MINCONN.sample);
        params.put(NAMESPACE.key, NAMESPACE.sample);
        params.put(VALIDATECONN.key, VALIDATECONN.sample);
        params.put(LOOSEBBOX.key, LOOSEBBOX.sample);

        setProperties(params);
                
//        jtf_host.setToolTipText(HOST.description.toString());
//        jtf_port.setToolTipText(PORT.description.toString());
//        jtf_schema.setToolTipText(SCHEMA.description.toString());
//        jtf_database.setToolTipText(DATABASE.description.toString());
//        jtf_user.setToolTipText(USER.description.toString());
//        jtf_password.setToolTipText(PASSWD.description.toString());
//        jsp_max_connects.setToolTipText(MAXCONN.description.toString());
//        jsp_min_connects.setToolTipText(MINCONN.description.toString());
//        jtf_namespace.setToolTipText(NAMESPACE.description.toString());
//        chk_validate.setToolTipText(VALIDATECONN.description.toString());
//        chk_estimated.setToolTipText(ESTIMATEDEXTENT.description.toString());
//        chk_loose.setToolTipText(LOOSEBBOX.description.toString());
//        chk_wkb.setToolTipText(WKBENABLED.description.toString());

    }

    public Map getProperties() {
        return params;
    }

    private void setProperties(Map map) {

        if(map == null){
            throw new NullPointerException();
        }
        
        Object val = null;
        
        val = map.get(HOST.key);
        jtf_host.setText( (val == null) ? "" : val.toString());
        val = map.get(PORT.key);
        jtf_port.setText((val == null) ? "" : val.toString());
        val = map.get(SCHEMA.key);
        jtf_schema.setText((val == null) ? "" : val.toString());
        val = map.get(DATABASE.key);
        jtf_database.setText((val == null) ? "" : val.toString());
        val = map.get(USER.key);
        jtf_user.setText((val == null) ? "" : val.toString());
        val = map.get(PASSWD.key);
        jtf_password.setText((val == null) ? "" : val.toString());
        val = map.get(MAXCONN.key);
        jsp_max_connects.setValue((val == null) ? 10 : val);
        val = map.get(MINCONN.key);
        jsp_min_connects.setValue((val == null) ? 4 : val);
        val = map.get(NAMESPACE.key);
        jtf_namespace.setText((val == null) ? "" : val.toString());
        val = map.get(VALIDATECONN.key);
        chk_validate.setSelected( (val == null) ? (Boolean) VALIDATECONN.sample : (Boolean)val);
        val = map.get(LOOSEBBOX.key);
        chk_loose.setSelected( (val == null) ? (Boolean) LOOSEBBOX.sample : (Boolean)val);
        
    }

    private void refreshTable() {

        if (store != null) {
            try {
                guiLayerList.setModel(new DefaultComboBoxModel(store.getTypeNames()));
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

        but_refresh = new JButton();
        jXTitledPanel1 = new JXTitledPanel();
        jLabel1 = new JLabel();
        jtf_host = new JTextField();
        jLabel2 = new JLabel();
        jtf_port = new JTextField();
        jLabel3 = new JLabel();
        jtf_schema = new JTextField();
        jLabel4 = new JLabel();
        jtf_database = new JTextField();
        jLabel5 = new JLabel();
        jtf_user = new JTextField();
        jLabel6 = new JLabel();
        jtf_password = new JPasswordField();
        jXTitledPanel2 = new JXTitledPanel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jsp_min_connects = new JSpinner();
        jsp_max_connects = new JSpinner();
        chk_validate = new JCheckBox();
        chk_wkb = new JCheckBox();
        chk_loose = new JCheckBox();
        chk_estimated = new JCheckBox();
        jLabel13 = new JLabel();
        jtf_namespace = new JTextField();
        jScrollPane1 = new JScrollPane();
        guiLayerList = new JList();

        but_refresh.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("test")); // NOI18N
        but_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                actionRefresh(evt);
            }
        });

        jXTitledPanel1.setBorder(null);







        jXTitledPanel1.setTitle(NbBundle.getBundle(JPostGISDataPanel.class).getString("connection")); // NOI18N
        jLabel1.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("host")); // NOI18N
        jLabel2.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("port")); // NOI18N
        jLabel3.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("schema")); // NOI18N
        jLabel4.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("database")); // NOI18N
        jLabel5.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("user")); // NOI18N
        jLabel6.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("password")); // NOI18N
        GroupLayout jXTitledPanel1Layout = new GroupLayout(jXTitledPanel1);
        jXTitledPanel1.setLayout(jXTitledPanel1Layout);
        jXTitledPanel1Layout.setHorizontalGroup(
            jXTitledPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_host, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_port, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_schema, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_database, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_user, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_password, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel1Layout.setVerticalGroup(
            jXTitledPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtf_host, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtf_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtf_schema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtf_database, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtf_user, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtf_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTitledPanel2.setBorder(null);








        jXTitledPanel2.setTitle(NbBundle.getBundle(JPostGISDataPanel.class).getString("advanced")); // NOI18N
        jLabel7.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("max_connects")); // NOI18N
        jLabel8.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("min_connects")); // NOI18N
        jsp_min_connects.setModel(new SpinnerNumberModel(0, 0, 255, 1));

        jsp_max_connects.setModel(new SpinnerNumberModel(0, 0, 255, 1));

        chk_validate.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("validate_connects")); // NOI18N
        chk_wkb.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("wkb_enabled")); // NOI18N
        chk_loose.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("loose_bbox")); // NOI18N
        chk_estimated.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("estimated_extend")); // NOI18N
        jLabel13.setText(NbBundle.getBundle(JPostGISDataPanel.class).getString("namespace")); // NOI18N
        GroupLayout jXTitledPanel2Layout = new GroupLayout(jXTitledPanel2);
        jXTitledPanel2.setLayout(jXTitledPanel2Layout);
        jXTitledPanel2Layout.setHorizontalGroup(
            jXTitledPanel2Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel2Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(chk_estimated)
                    .addComponent(chk_loose)
                    .addComponent(chk_wkb)
                    .addGroup(jXTitledPanel2Layout.createParallelGroup(Alignment.LEADING, false)
                        .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jsp_max_connects, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jsp_min_connects)))
                    .addComponent(chk_validate)
                    .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_namespace, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel2Layout.setVerticalGroup(
            jXTitledPanel2Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXTitledPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel2Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jsp_max_connects, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel2Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jsp_min_connects, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(chk_validate)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(chk_wkb)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(chk_loose)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(chk_estimated)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel2Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtf_namespace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(guiLayerList);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                    .addComponent(jXTitledPanel1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTitledPanel2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addComponent(but_refresh, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jXTitledPanel1, jXTitledPanel2});

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(but_refresh)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXTitledPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jXTitledPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void actionRefresh(ActionEvent evt) {//GEN-FIRST:event_actionRefresh

        params.clear();
        params.put(DBTYPE.key, "postgisng");
        params.put(HOST.key, jtf_host.getText());
        params.put(PORT.key, jtf_port.getText());
        params.put(SCHEMA.key, jtf_schema.getText());
        params.put(DATABASE.key, jtf_database.getText());
        params.put(USER.key, jtf_user.getText());
        params.put(PASSWD.key, new String(jtf_password.getPassword()));
        params.put(MAXCONN.key, jsp_max_connects.getValue());
        params.put(MINCONN.key, jsp_min_connects.getValue());
        params.put(NAMESPACE.key, jtf_namespace.getText());
        params.put(VALIDATECONN.key, chk_validate.isSelected());
        params.put(LOOSEBBOX.key, chk_loose.isSelected());

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
    private JButton but_refresh;
    private JCheckBox chk_estimated;
    private JCheckBox chk_loose;
    private JCheckBox chk_validate;
    private JCheckBox chk_wkb;
    private JList guiLayerList;
    private JLabel jLabel1;
    private JLabel jLabel13;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JScrollPane jScrollPane1;
    private JXTitledPanel jXTitledPanel1;
    private JXTitledPanel jXTitledPanel2;
    private JSpinner jsp_max_connects;
    private JSpinner jsp_min_connects;
    private JTextField jtf_database;
    private JTextField jtf_host;
    private JTextField jtf_namespace;
    private JPasswordField jtf_password;
    private JTextField jtf_port;
    private JTextField jtf_schema;
    private JTextField jtf_user;
    // End of variables declaration//GEN-END:variables
    
    public Map<String,GISSourceInfo> createSources() {
        final Map<String,String> params = new HashMap<String,String>();
        params.put(DBTYPE.key, "postgisng");
        params.put(HOST.key, jtf_host.getText());
        params.put(PORT.key, jtf_port.getText());
        params.put(SCHEMA.key, jtf_schema.getText());
        params.put(DATABASE.key, jtf_database.getText());
        params.put(USER.key, jtf_user.getText());
        params.put(PASSWD.key, new String(jtf_password.getPassword()));
        params.put(MAXCONN.key, jsp_max_connects.getValue().toString());
        params.put(MINCONN.key, jsp_min_connects.getValue().toString());
        params.put(NAMESPACE.key, jtf_namespace.getText());
        params.put(VALIDATECONN.key, Boolean.valueOf(chk_validate.isSelected()).toString());
        params.put(LOOSEBBOX.key, Boolean.valueOf(chk_loose.isSelected()).toString());
        final GISSourceInfo info = new GISSourceInfo(-1, PostGISSourceService.SERVICE_ID, params);
        final String name = jtf_host.getText() +"_"+jtf_schema.getText()+"_"+jtf_database.getText();
        final Map<String,GISSourceInfo> sources = new HashMap<String, GISSourceInfo>();
        sources.put(name, info);
        return sources;
    }
}
