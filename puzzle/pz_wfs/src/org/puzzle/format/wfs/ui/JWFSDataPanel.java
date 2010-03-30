/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.format.wfs.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.geotoolkit.wms.WebMapServer;
import org.geotoolkit.wms.xml.AbstractWMSCapabilities;

import org.jdesktop.swingx.JXTree;
import org.openide.util.Exceptions;

import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.wfs.resources.WFSResource;
import org.puzzle.format.wfs.service.WFSSourceService;

/**
 * WMS Chooser
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JWFSDataPanel extends SourceCreationPane {

    private WebMapServer server;
    private final Map<String,Object> params = new HashMap<String, Object>();

    /** Creates new form DefaultShapeTypeChooser */
    public JWFSDataPanel() {
        initComponents();

        params.put(WFSSourceService.URL_PROP, "http://www2.demis.nl/WMS/wms.asp?wms=WorldMap");
        params.put(WFSSourceService.VERSION_PROP, "1.3.0");
        jtf_name.setText("Demis");

        setProperties(params);

        refreshTable();
    }

    public Map getProperties() {
        return params;
    }

    public void setProperties(Map map) {

        if(map == null){
            throw new NullPointerException();
        }
        
        Object val = null;
        
        val = map.get(WFSSourceService.URL_PROP);
        jtf_url.setText( (val == null) ? "" : val.toString());
        val = map.get(WFSSourceService.VERSION_PROP);
        jtf_version.setText((val == null) ? "" : val.toString());
        
    }

    private void refreshTable() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("-");

        if (server != null) {
            try {
                final AbstractWMSCapabilities capa = server.getCapabilities();

                if(capa instanceof org.geotoolkit.wms.xml.v130.WMSCapabilities){
                    final org.geotoolkit.wms.xml.v130.WMSCapabilities cp13 =
                            (org.geotoolkit.wms.xml.v130.WMSCapabilities) capa;
                    root = explore(cp13.getCapability().getLayer(), null);
                }else if(capa instanceof org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities){
                    final org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities cp11 =
                            (org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities) capa;
                    root = explore(cp11.getCapability().getLayer(), root);
                }

            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        guiLayerList.setModel(new DefaultTreeModel(root));
        guiLayerList.setCellRenderer(new TreeCellLayerRenderer());
        guiLayerList.setRootVisible(false);
        guiLayerList.expandAll();
        guiLayerList.revalidate();
    }

    private DefaultMutableTreeNode explore(org.geotoolkit.wms.xml.v130.Layer layer, DefaultMutableTreeNode parent){
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(layer);
        
        for (org.geotoolkit.wms.xml.v130.Layer child : layer.getLayer()) {
            explore(child, node);
        }

        if(parent != null){
            parent.add(node);
            return parent;
        }else{
            return node;
        }
    }

    private DefaultMutableTreeNode explore(org.geotoolkit.wms.xml.v111.Layer layer, DefaultMutableTreeNode parent){
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(layer);

        for (org.geotoolkit.wms.xml.v111.Layer child : layer.getLayer()) {
            explore(child, node);
        }

        if(parent != null){
            parent.add(node);
            return parent;
        }else{
            return node;
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
        guiLayerList = new JXTree();
        jPanel1 = new JPanel();
        jtf_name = new JTextField();
        jLabel1 = new JLabel();
        jtf_url = new JTextField();
        jLabel2 = new JLabel();
        jtf_version = new JTextField();
        jLabel3 = new JLabel();

        but_refresh.setText(WFSResource.getString("test")); // NOI18N
        but_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                actionRefresh(evt);
            }
        });

        jScrollPane1.setViewportView(guiLayerList);





        jPanel1.setBorder(BorderFactory.createTitledBorder(WFSResource.getString("wfsTitle"))); // NOI18N
        jLabel1.setText(WFSResource.getString("url")); // NOI18N
        jLabel2.setText(WFSResource.getString("version")); // NOI18N
        jLabel3.setText(WFSResource.getString("name")); // NOI18N
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_url, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_version, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jtf_name, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtf_url, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtf_version, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtf_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(but_refresh, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(but_refresh)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void actionRefresh(ActionEvent evt) {//GEN-FIRST:event_actionRefresh

        params.clear();
        params.put(WFSSourceService.URL_PROP, jtf_url.getText());
        params.put(WFSSourceService.VERSION_PROP, jtf_version.getText());

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
    private JXTree guiLayerList;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField jtf_name;
    private JTextField jtf_url;
    private JTextField jtf_version;
    // End of variables declaration//GEN-END:variables
    
    public Map<String,GISSourceInfo> createSources() {
        final Map<String,Serializable> params = new HashMap<String,Serializable>();
        params.put(WFSSourceService.URL_PROP, jtf_url.getText());
        params.put(WFSSourceService.VERSION_PROP, jtf_version.getText());
        final GISSourceInfo info = new GISSourceInfo(-1, WFSSourceService.SERVICE_ID, params);
        final Map<String,GISSourceInfo> sources = new HashMap<String, GISSourceInfo>();

        String name = jtf_name.getText();
        if(name.trim().isEmpty()){
            name = "noname";
        }

        sources.put(name, info);
        return sources;
    }
}
