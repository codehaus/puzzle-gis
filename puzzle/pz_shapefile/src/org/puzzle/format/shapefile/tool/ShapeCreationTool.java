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
package org.puzzle.format.shapefile.tool;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.geotoolkit.data.FileDataStoreFactorySpi;
import org.geotoolkit.data.shapefile.ShapefileDataStore;
import org.geotoolkit.data.shapefile.ShapefileDataStoreFactory;
import org.geotoolkit.feature.FeatureTypeUtilities;
import org.geotoolkit.feature.SchemaException;
import org.geotoolkit.gui.swing.crschooser.JCRSChooser;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledSeparator;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.openide.util.NbBundle;


/**
 * Widget Panel to create ShapeFiles.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeCreationTool extends JPanel {

    private ShapeAttModel model = new ShapeAttModel();
    private String geotype = "Point";
    private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
    private File file = new File("default.shp");

    /** 
     * Creates new form shapeCreationPanel 
     */
    public ShapeCreationTool() {
        initComponents();
        gui_tab.getSelectionModel().setSelectionMode(gui_tab.getSelectionModel().SINGLE_SELECTION);
        gui_tab.getColumn(1).setCellEditor(new TypeEditor());

        gui_jtf_crs.setText(crs.getName().toString());
        gui_jtf_name.setText(file.getAbsolutePath());
    }

    private void createShape(String name) {
        try {
            // Create the DataStoreFactory
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

            // Create a Map object used by our DataStore Factory
            // NOTE: file.toURI().toURL() is used because file.toURL() is deprecated
            Map<String, Serializable> map = Collections.singletonMap("url", (Serializable)file.toURI().toURL());

            // Create the ShapefileDataStore from our factory based on our Map object
            ShapefileDataStore myData = (ShapefileDataStore) factory.createNewDataStore(map);

            // Tell this shapefile what type of data it will store
            StringBuffer buffer = new StringBuffer();
            buffer.append("geom:");
            buffer.append(geotype);

            Field[] datas = model.getDatas();

            for (Field data : datas) {
                buffer.append("," + data.getName());

                switch (data.getType()) {
                    case INTEGER:
                        buffer.append(":" + Integer.class.getName());
                        break;
                    case LONG:
                        buffer.append(":" + Long.class.getName());
                        break;
                    case DOUBLE:
                        buffer.append(":" + Double.class.getName());
                        break;
                    case STRING:
                        buffer.append(":" + String.class.getName());
                        break;
                    case DATE:
                        buffer.append(":" + Date.class.getName());
                        break;
                }
            }

            System.out.println(buffer);
            //SimpleFeatureType featureType = DataUtilities.createType(name, buffer.toString());
            SimpleFeatureType featureType = FeatureTypeUtilities.createType(name, buffer.toString());

            // Create the Shapefile (empty at this point)
            myData.createSchema(featureType);

            // Tell the DataStore what type of Coordinate Reference System (CRS) to use
            myData.forceSchemaCRS(crs);

            myData.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Incorrect File : " + e.getMessage());
        } catch (SchemaException se) {
            JOptionPane.showMessageDialog(this, "Incorrect Schema : " + se.getMessage());
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {



        grp_geom = new ButtonGroup();
        jLabel1 = new JLabel();
        jXTitledSeparator1 = new JXTitledSeparator();
        gui_jtf_name = new JTextField();
        gui_but_create = new JButton();
        gui_but_file = new JButton();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        gui_tab = new JXTable();
        gui_but_add = new JButton();
        gui_but_up = new JButton();
        gui_but_down = new JButton();
        gui_but_delete = new JButton();
        jPanel1 = new JPanel();
        gui_jrb_point = new JRadioButton();
        gui_jrb_multipoint = new JRadioButton();
        gui_jrb_multiline = new JRadioButton();
        gui_jrb_multipolygon = new JRadioButton();
        gui_but_crs = new JButton();
        jLabel2 = new JLabel();
        gui_jtf_crs = new JTextField();

        jLabel1.setText(NbBundle.getMessage(ShapeCreationTool.class, "file")); // NOI18N
        jXTitledSeparator1.setTitle(NbBundle.getMessage(ShapeCreationTool.class, "shapefile_creation")); // NOI18N
        gui_jtf_name.setEditable(false);


        gui_jtf_name.setText(NbBundle.getMessage(ShapeCreationTool.class, "default")); // NOI18N
        gui_but_create.setText(NbBundle.getMessage(ShapeCreationTool.class, "create")); // NOI18N
        gui_but_create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_createActionPerformed(evt);
            }
        });

        gui_but_file.setText(NbBundle.getMessage(ShapeCreationTool.class, "...")); // NOI18N
        gui_but_file.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_fileActionPerformed(evt);
            }
        });

        jPanel2.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ShapeCreationTool.class, "attributs"))); // NOI18N
        gui_tab.setModel(model);
        jScrollPane1.setViewportView(gui_tab);

        gui_but_add.setText(NbBundle.getMessage(ShapeCreationTool.class, "add")); // NOI18N
        gui_but_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_addActionPerformed(evt);
            }
        });

        gui_but_up.setText(NbBundle.getMessage(ShapeCreationTool.class, "up")); // NOI18N
        gui_but_up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_upActionPerformed(evt);
            }
        });

        gui_but_down.setText(NbBundle.getMessage(ShapeCreationTool.class, "down")); // NOI18N
        gui_but_down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_downActionPerformed(evt);
            }
        });

        gui_but_delete.setText(NbBundle.getMessage(ShapeCreationTool.class, "delete")); // NOI18N
        gui_but_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_deleteActionPerformed(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);

        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(gui_but_add)
                    .addComponent(gui_but_up)
                    .addComponent(gui_but_down)
                    .addComponent(gui_but_delete)))
        );

        jPanel2Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {gui_but_add, gui_but_delete, gui_but_down, gui_but_up});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(gui_but_add)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_but_delete)
                .addGap(24, 24, 24)
                .addComponent(gui_but_up)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_but_down)
                .addContainerGap(119, Short.MAX_VALUE))
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );

        jPanel1.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ShapeCreationTool.class, "geometry"))); // NOI18N
        grp_geom.add(gui_jrb_point);
        gui_jrb_point.setSelected(true);
        gui_jrb_point.setText(NbBundle.getMessage(ShapeCreationTool.class, "point")); // NOI18N
        gui_jrb_point.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_jrb_pointActionPerformed(evt);
            }
        });

        grp_geom.add(gui_jrb_multipoint);
        gui_jrb_multipoint.setText(NbBundle.getMessage(ShapeCreationTool.class, "multipoint")); // NOI18N
        gui_jrb_multipoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_jrb_multipointActionPerformed(evt);
            }
        });

        grp_geom.add(gui_jrb_multiline);
        gui_jrb_multiline.setText(NbBundle.getMessage(ShapeCreationTool.class, "multiline")); // NOI18N
        gui_jrb_multiline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_jrb_multilineActionPerformed(evt);
            }
        });

        grp_geom.add(gui_jrb_multipolygon);
        gui_jrb_multipolygon.setText(NbBundle.getMessage(ShapeCreationTool.class, "multipolygon")); // NOI18N
        gui_jrb_multipolygon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_jrb_multipolygonActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(gui_jrb_point)
                    .addComponent(gui_jrb_multipoint)
                    .addComponent(gui_jrb_multiline)
                    .addComponent(gui_jrb_multipolygon))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(gui_jrb_point)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_jrb_multipoint)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_jrb_multiline)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(gui_jrb_multipolygon)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gui_but_crs.setText(NbBundle.getMessage(ShapeCreationTool.class, "list")); // NOI18N
        gui_but_crs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gui_but_crsActionPerformed(evt);
            }
        });

        jLabel2.setText(NbBundle.getMessage(ShapeCreationTool.class, "crs")); // NOI18N
        gui_jtf_crs.setEditable(false);
        gui_jtf_crs.setText("EPSG:4326");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jXTitledSeparator1, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(gui_jtf_name, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(gui_but_file))
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                            .addComponent(gui_but_create)
                            .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(gui_jtf_crs, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(gui_but_crs)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTitledSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(gui_but_file)
                    .addComponent(gui_jtf_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(gui_but_crs)
                    .addComponent(gui_jtf_crs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                        .addComponent(gui_but_create))
                    .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void gui_but_createActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_createActionPerformed
        createShape(gui_jtf_name.getText());
    }//GEN-LAST:event_gui_but_createActionPerformed

    private void gui_but_addActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_addActionPerformed
        model.addAttribut();
    }//GEN-LAST:event_gui_but_addActionPerformed

    private void gui_but_deleteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_deleteActionPerformed

        int selected = gui_tab.getSelectionModel().getMinSelectionIndex();
        if (selected >= 0) {
            Field data = model.getDataAt(selected);
            model.deleteAttribut(data);
        }
        
    }//GEN-LAST:event_gui_but_deleteActionPerformed

    private void gui_but_upActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_upActionPerformed
        int selected = gui_tab.getSelectionModel().getMinSelectionIndex();
        if (selected >= 0) {
            Field data = model.getDataAt(selected);
            model.moveUp(data);
        }
    }//GEN-LAST:event_gui_but_upActionPerformed

    private void gui_but_downActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_downActionPerformed
        int selected = gui_tab.getSelectionModel().getMinSelectionIndex();
        if (selected >= 0) {
            Field data = model.getDataAt(selected);
            model.moveDown(data);
        }
    }//GEN-LAST:event_gui_but_downActionPerformed

    private void gui_jrb_pointActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_jrb_pointActionPerformed
        geotype = "Point";
    }//GEN-LAST:event_gui_jrb_pointActionPerformed

    private void gui_jrb_multipointActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_jrb_multipointActionPerformed
        geotype = "MultiPoint";
    }//GEN-LAST:event_gui_jrb_multipointActionPerformed

    private void gui_jrb_multilineActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_jrb_multilineActionPerformed
        geotype = "MultiLineString";
    }//GEN-LAST:event_gui_jrb_multilineActionPerformed

    private void gui_jrb_multipolygonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_jrb_multipolygonActionPerformed
        geotype = "MultiPolygon";
    }//GEN-LAST:event_gui_jrb_multipolygonActionPerformed

    private void gui_but_crsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_crsActionPerformed
        JCRSChooser jcrs = new JCRSChooser(null, true);
        jcrs.setCRS(crs);
        JCRSChooser.ACTION act = jcrs.showDialog();

        if (act == JCRSChooser.ACTION.APPROVE) {
            crs = jcrs.getCRS();
        }

        gui_jtf_crs.setText(crs.getName().toString());
                
    }//GEN-LAST:event_gui_but_crsActionPerformed

    private void gui_but_fileActionPerformed(ActionEvent evt) {//GEN-FIRST:event_gui_but_fileActionPerformed
        JFileChooser jfc = new JFileChooser(file);
        int act = jfc.showSaveDialog(null);

        if (act == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            
            if (f != null) {
                if (f.getAbsolutePath().endsWith(".shp")) {
                    file = f;
                    gui_jtf_name.setText(file.getAbsolutePath());
                } else {
                    int lastdot = f.getAbsolutePath().lastIndexOf(".");
                    if(lastdot>0){
                        f = new File(f.getAbsolutePath().substring(0,lastdot) +".shp");
                    }else{
                        f = new File(f.getAbsolutePath() +".shp");
                    }
                    
                    file = f;
                    gui_jtf_name.setText(file.getAbsolutePath());
                }
            }

        }
        
    }//GEN-LAST:event_gui_but_fileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup grp_geom;
    private JButton gui_but_add;
    private JButton gui_but_create;
    private JButton gui_but_crs;
    private JButton gui_but_delete;
    private JButton gui_but_down;
    private JButton gui_but_file;
    private JButton gui_but_up;
    private JRadioButton gui_jrb_multiline;
    private JRadioButton gui_jrb_multipoint;
    private JRadioButton gui_jrb_multipolygon;
    private JRadioButton gui_jrb_point;
    private JTextField gui_jtf_crs;
    private JTextField gui_jtf_name;
    private JXTable gui_tab;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JXTitledSeparator jXTitledSeparator1;
    // End of variables declaration//GEN-END:variables
}
