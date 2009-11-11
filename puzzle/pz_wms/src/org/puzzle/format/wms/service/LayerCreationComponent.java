/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.format.wms.service;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.geotoolkit.wms.WebMapServer;
import org.geotoolkit.wms.xml.AbstractWMSCapabilities;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.wms.xml.v111.Layer;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.openide.util.Exceptions;

import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.capabilities.LayerCreation;
import org.puzzle.format.wms.resources.WMSResource;
import org.puzzle.format.wms.ui.ListCellLayerRenderer;
import org.puzzle.format.wms.ui.TreeCellLayerRenderer;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class LayerCreationComponent extends JLayerChooser {

    private final WebMapServer server;
    private final WMSSource source;
    private final List<Object> possibleLayers = new ArrayList<Object>();
    private final List<Object> selectedLayers = new ArrayList<Object>();

    /** Creates new form LayerCreationComponent */
    public LayerCreationComponent(final LayerChooserMonitor monitor, WebMapServer store, WMSSource source) {
        super(monitor);
        this.server = store;
        this.source = source;
        initComponents();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("-");

        if (server != null) {
            possibleLayers.clear();
            try {
                final AbstractWMSCapabilities capa = server.getCapabilities();

                if(capa instanceof org.geotoolkit.wms.xml.v130.WMSCapabilities){
                    final org.geotoolkit.wms.xml.v130.WMSCapabilities cp13 =
                            (org.geotoolkit.wms.xml.v130.WMSCapabilities) capa;
                    root = explore(cp13.getCapability().getLayer(), root);
                }else if(capa instanceof org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities){
                    final org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities cp11 =
                            (org.geotoolkit.wms.xml.v111.WMT_MS_Capabilities) capa;
                    root = explore(cp11.getCapability().getLayer(), root);
                }

            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        
        guiLstSelected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        guiLstSelected.setCellRenderer(new ListCellLayerRenderer());

        guiLstData.setModel(new DefaultTreeModel(root));
        guiLstData.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        guiLstData.setCellRenderer(new TreeCellLayerRenderer());
        guiLstData.setRootVisible(false);
        guiLstData.expandAll();
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


    private String getLayerNames(){

        if(!selectedLayers.isEmpty()){
            String name = "";
            for(Object obj : selectedLayers){
                if(obj instanceof Layer){
                    name += ((Layer)obj).getName() + ',';
                }else if(obj instanceof org.geotoolkit.wms.xml.v130.Layer){
                    name +=((org.geotoolkit.wms.xml.v130.Layer)obj).getName() + ',';
                }
            }

            return name.substring(0, name.length()-1);
        }else{
            return null;
        }
    }

    public void refreshName(){
        String name = getLayerNames();
        this.guiTitle.setText( ((name == null)?"":name) );
    }

    private void check(){
        getMonitor().setReady(!selectedLayers.isEmpty());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {


        jLabel2 = new JLabel();
        guiTitle = new JTextField();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jButton6 = new JButton();
        jsp2 = new JScrollPane();
        guiLstSelected = new JList();
        jScrollPane1 = new JScrollPane();
        guiLstData = new JXTree();

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | Font.BOLD));
        jLabel2.setText(WMSResource.getString("title")); // NOI18N
        jButton1.setText(">>");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addAll(evt);
            }
        });

        jButton2.setText(">");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addSelected(evt);
            }
        });

        jButton3.setText("<");
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeSelected(evt);
            }
        });

        jButton4.setText("<<");
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeAll(evt);
            }
        });

        jButton5.setText(WMSResource.getString("up")); // NOI18N
        jButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                moveUp(evt);
            }
        });

        jButton6.setText(WMSResource.getString("down")); // NOI18N
        jButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                moveDown(evt);
            }
        });

        jsp2.setViewportView(guiLstSelected);

        jScrollPane1.setViewportView(guiLstData);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jButton3)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(jsp2, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jButton6)
                            .addComponent(jButton5)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(guiTitle, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jButton1, jButton2, jButton3, jButton4});

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jButton5, jButton6});

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(guiTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jsp2, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addAll(ActionEvent evt) {//GEN-FIRST:event_addAll
        selectedLayers.clear();
        selectedLayers.addAll(possibleLayers);
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        check();
        refreshName();
    }//GEN-LAST:event_addAll

    private void addSelected(ActionEvent evt) {//GEN-FIRST:event_addSelected
        TreePath[] selections = guiLstData.getSelectionPaths();
        for(TreePath obj : selections){
            Object candidate = ((DefaultMutableTreeNode)obj.getLastPathComponent()).getUserObject();
            if(!selectedLayers.contains(candidate) ){
                if(candidate instanceof Layer){
                    Layer l = (Layer) candidate;
                    if(l.getName() != null && !l.getName().isEmpty()){
                        selectedLayers.add(l);
                    }
                }else if(candidate instanceof org.geotoolkit.wms.xml.v130.Layer){
                    org.geotoolkit.wms.xml.v130.Layer l = (org.geotoolkit.wms.xml.v130.Layer) candidate;
                    if(l.getName() != null && !l.getName().isEmpty()){
                        selectedLayers.add(l);
                    }
                }
                
            }
        }
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        check();
        refreshName();
    }//GEN-LAST:event_addSelected

    private void removeSelected(ActionEvent evt) {//GEN-FIRST:event_removeSelected
        Object[] selections = guiLstSelected.getSelectedValues();
        for(Object obj : selections){
            selectedLayers.remove(obj);
        }
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        check();
        refreshName();
    }//GEN-LAST:event_removeSelected

    private void removeAll(ActionEvent evt) {//GEN-FIRST:event_removeAll
        selectedLayers.clear();
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        check();
        refreshName();
    }//GEN-LAST:event_removeAll

    private void moveUp(ActionEvent evt) {//GEN-FIRST:event_moveUp
        Object obj = guiLstSelected.getSelectedValue();
        if(obj != null){
            int index = selectedLayers.indexOf(obj);
            if(index != 0){
                selectedLayers.remove(obj);
                selectedLayers.add(index-1, obj);
                guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
                guiLstSelected.setSelectedValue(obj, true);
            }
        }
        refreshName();
    }//GEN-LAST:event_moveUp

    private void moveDown(ActionEvent evt) {//GEN-FIRST:event_moveDown
        Object obj = guiLstSelected.getSelectedValue();
        if(obj != null){
            int index = selectedLayers.indexOf(obj);
            if(index != selectedLayers.size()-1){
                selectedLayers.remove(obj);
                selectedLayers.add(index+1, obj);
                guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
                guiLstSelected.setSelectedValue(obj, true);
            }
        }
        guiLstSelected.setModel(new ListComboBoxModel<Object>(selectedLayers));
        refreshName();
    }//GEN-LAST:event_moveDown

    /**
     * {@inheritDoc }
     */
    @Override
    public MapLayer[] getLayers() {
        String title = guiTitle.getText();
        String type = getLayerNames();
        if(type != null){
            MapLayer layer = source.getLookup().lookup(LayerCreation.class).createLayer(Collections.singletonMap(WMSSource.LAYERS_PROP, type));
            layer.setDescription(new DefaultStyleFactory().description(title,"") );
            return new MapLayer[]{layer};
        }

        return new MapLayer[0];
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JXTree guiLstData;
    private JList guiLstSelected;
    private JTextField guiTitle;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jsp2;
    // End of variables declaration//GEN-END:variables

}
