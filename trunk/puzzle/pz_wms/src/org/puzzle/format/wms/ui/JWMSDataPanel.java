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
package org.puzzle.format.wms.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import org.geotools.data.wms.WebMapServer;

import org.geotools.data.wms.backend.AbstractWMSCapabilities;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.wms.service.WMSSourceService;

/**
 * WMS Chooser
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JWMSDataPanel extends SourceCreationPane {

    private WebMapServer server;
    private final Map<String,Object> params = new HashMap<String, Object>();

    /** Creates new form DefaultShapeTypeChooser */
    public JWMSDataPanel() {
        initComponents();

        params.put(WMSSourceService.URL_PROP, "http://www2.demis.nl/WMS/wms.asp?wms=WorldMap");
        params.put(WMSSourceService.VERSION_PROP, "1.1.1");

        setProperties(params);

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
        
        val = map.get(WMSSourceService.URL_PROP);
        jtf_url.setText( (val == null) ? "" : val.toString());
        val = map.get(WMSSourceService.VERSION_PROP);
        jtf_version.setText((val == null) ? "" : val.toString());
        
    }

    private void refreshTable() {

        if (server != null) {
            ((DBModel) tab_table.getModel()).clean();
            try {
                final AbstractWMSCapabilities capa = server.getJaxbCapabilities();
                final List<String> layerNames = new ArrayList<String>();


                if(capa instanceof org.geotools.data.wms.backend.v130.WMSCapabilities){
                    org.geotools.data.wms.backend.v130.WMSCapabilities cp13 =
                            (org.geotools.data.wms.backend.v130.WMSCapabilities) capa;
                    for(org.geotools.data.wms.backend.v130.Layer layer : cp13.getCapability().getLayer().getLayer()){
                        layerNames.add(layer.getName());
                    }
                }else if(capa instanceof org.geotools.data.wms.backend.v111.WMT_MS_Capabilities){
                    org.geotools.data.wms.backend.v111.WMT_MS_Capabilities cp11 =
                            (org.geotools.data.wms.backend.v111.WMT_MS_Capabilities) capa;
                    for(org.geotools.data.wms.backend.v111.Layer layer : cp11.getCapability().getLayer().getLayer()){
                        layerNames.add(layer.getName());
                    }
                }

                ((DBModel) tab_table.getModel()).add(layerNames.toArray(new String[layerNames.size()]));
            } catch (Exception ex) {
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
        jScrollPane1 = new JScrollPane();
        tab_table = new JXTable();
        jXTitledPanel1 = new JXTitledPanel();
        jLabel1 = new JLabel();
        jtf_url = new JTextField();
        jLabel2 = new JLabel();
        jtf_version = new JTextField();
        jLabel3 = new JLabel();
        jtf_name = new JTextField();

        but_refresh.setText(NbBundle.getBundle(JWMSDataPanel.class).getString("test")); // NOI18N
        but_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                actionRefresh(evt);
            }
        });

        tab_table.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tab_table);

        jXTitledPanel1.setBorder(BorderFactory.createEtchedBorder());
        jXTitledPanel1.setTitle("null");




        jLabel1.setText(NbBundle.getBundle(JWMSDataPanel.class).getString("url")); // NOI18N
        jLabel2.setText(NbBundle.getBundle(JWMSDataPanel.class).getString("version")); // NOI18N
        jLabel3.setText(NbBundle.getMessage(JWMSDataPanel.class, "name")); // NOI18N
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
                        .addComponent(jtf_url, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_version, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                    .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_name, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jXTitledPanel1Layout.setVerticalGroup(
            jXTitledPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXTitledPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtf_url, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtf_version, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jXTitledPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtf_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXTitledPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(but_refresh, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(but_refresh)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXTitledPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void actionRefresh(ActionEvent evt) {//GEN-FIRST:event_actionRefresh

        params.clear();
        params.put(WMSSourceService.URL_PROP, jtf_url.getText());
        params.put(WMSSourceService.VERSION_PROP, jtf_version.getText());

        but_refresh.setEnabled(false);
        new Thread() {

            @Override
            public void run() {
                try {
                    server = new WebMapServer(new URL(jtf_url.getText()),jtf_version.getText());
                    refreshTable();
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                    server = null;
                }

            but_refresh.setEnabled(true);
            }
        }.start();
        
    }//GEN-LAST:event_actionRefresh

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton but_refresh;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JXTitledPanel jXTitledPanel1;
    private JTextField jtf_name;
    private JTextField jtf_url;
    private JTextField jtf_version;
    private JXTable tab_table;
    // End of variables declaration//GEN-END:variables
    
    public Map<String,GISSourceInfo> createSources() {
        final Map<String,String> params = new HashMap<String,String>();
        params.put(WMSSourceService.URL_PROP, jtf_url.getText());
        params.put(WMSSourceService.VERSION_PROP, jtf_version.getText());
        final GISSourceInfo info = new GISSourceInfo(-1, WMSSourceService.SERVICE_ID, params);
        final Map<String,GISSourceInfo> sources = new HashMap<String, GISSourceInfo>();
        sources.put(jtf_name.getText(), info);
        return sources;
    }
}
