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
package org.puzzle.puzzlecore.swing.toolbox.widgettool.clipping;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.map.MapLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JFileDataPanel;
import org.geotools.gui.swing.datachooser.JOracleDataPanel;
import org.geotools.gui.swing.datachooser.JPostGISDataPanel;
import org.geotools.gui.swing.misc.Render.LayerListRenderer;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.AbstractWidgetTool;

/**
 *
 * @author johann sorel
 */
public class ClippingTool extends AbstractWidgetTool {

    private LayerListModel inModel = new LayerListModel();
    private LayerListModel clipModel = new LayerListModel();
    private File outFile = null;
    private MapLayer inLayer = null;
    private MapLayer inExternLayer = null;
    private MapLayer clipLayer = null;
    private MapLayer clipExternLayer = null;
    private Map<String, String> attlink = null;
    private String error = NbBundle.getMessage(ClippingTool.class, "error");

    /** 
     * Creates new form ClippingTool 
     * @param parameters 
     */
    public ClippingTool(Map parameters) {
        initComponents();

        Object obj = parameters.get("layers");
        if (obj instanceof MapLayer[]) {

            MapLayer[] layers = filterLayers((MapLayer[]) obj);

            inModel.setLayers(layers);
            clipModel.setLayers(layers);
        }

        gui_jcb_inlayer.setRenderer(new LayerListRenderer());
        gui_jcb_cliplayer.setRenderer(new LayerListRenderer());
        gui_jcb_inlayer.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                verify();
                if (inModel.getSelectedItem() == LayerListModel.OUT_FILE) {
                    gui_but_infile.setEnabled(true);
                    inLayer = inExternLayer;
                    if (inLayer != null) {
                        gui_jtf_inexternal.setText(inLayer.getTitle());
                    }
                } else {
                    gui_but_infile.setEnabled(false);
                    inLayer = (MapLayer) inModel.getSelectedItem();
                    gui_jtf_inexternal.setText(inLayer.getTitle());
                }

            }
        });
        gui_jcb_cliplayer.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                verify();
                if (clipModel.getSelectedItem() == LayerListModel.OUT_FILE) {
                    gui_but_clipfile.setEnabled(true);
                    clipLayer = clipExternLayer;
                    if (clipLayer != null) {
                        gui_jtf_clipexternal.setText(clipLayer.getTitle());
                    }
                } else {
                    gui_but_clipfile.setEnabled(false);
                    clipLayer = (MapLayer) clipModel.getSelectedItem();
                    gui_jtf_clipexternal.setText(clipLayer.getTitle());
                }
            }
        });

        gui_jcb_inlayer.setSelectedItem(inModel.getElementAt(0));
        gui_jcb_cliplayer.setSelectedItem(clipModel.getElementAt(0));

    }

    private MapLayer[] filterLayers(MapLayer[] layers) {
        List<MapLayer> lst = new ArrayList<MapLayer>();
        for (MapLayer layer : layers) {
            if (!layer.getFeatureSource().getSchema().getName().getLocalPart().equals("GridCoverage")) {
                lst.add(layer);
            }
        }
        return lst.toArray(new MapLayer[lst.size()]);
    }

    private boolean verify() {
        if (outFile != null && inLayer != null && !inLayer.getFeatureSource().getSchema().getName().getLocalPart().equals("GridCoverage") && clipLayer != null && !clipLayer.getFeatureSource().getSchema().getName().getLocalPart().equals("GridCoverage")) {

            Class jtsClass = clipLayer.getFeatureSource().getSchema().getDefaultGeometry().getType().getBinding();

            if (jtsClass.equals(Polygon.class) || jtsClass.equals(MultiPolygon.class)) {
                gui_ok.setEnabled(true);
                return true;
            }

        }
        gui_ok.setEnabled(false);
        return false;
    }

    private DataStore createShapeFile(File file, SimpleFeatureType type, CoordinateReferenceSystem crs, String title) {

        attlink = new HashMap<String, String>();

        ShapefileDataStore myData = null;



        try {
            // Create the DataStoreFactory
            FileDataStoreFactorySpi factory = new IndexedShapefileDataStoreFactory();

            // Create a Map object used by our DataStore Factory
            // NOTE: file.toURI().toURL() is used because file.toURL() is deprecated
            Map<String, URL> map = Collections.singletonMap("url", file.toURI().toURL());

            // Create the ShapefileDataStore from our factory based on our Map object
            myData = (ShapefileDataStore) factory.createNewDataStore(map);

            AttributeDescriptor geodesc = type.getDefaultGeometry();
            Class jtsClass = geodesc.getType().getBinding();

            String geotype = "";

            if (jtsClass.equals(Point.class) || jtsClass.equals(MultiPoint.class)) {
                geotype = "MultiPoint";
            } else if (jtsClass.equals(LineString.class) || jtsClass.equals(MultiLineString.class)) {
                geotype = "MultiLineString";
            } else if (jtsClass.equals(Polygon.class) || jtsClass.equals(MultiPolygon.class)) {
                geotype = "MultiPolygon";
            }

            
            //make the simplefeaturetype
            // Tell this shapefile what type of data it will store
            StringBuffer buffer = new StringBuffer();
            buffer.append("geom:");
            buffer.append(geotype);


            List<AttributeDescriptor> lst = type.getAttributes();
            for (AttributeDescriptor att : lst) {




                if (att != geodesc) {
                    String name = att.getName().toString();
                    if (name.length() > 10) {
                        name = name.substring(0, 10);
                    }

                    attlink.put(name, att.getName().toString());

                    String classe = att.getType().getBinding().getName();
                    buffer.append("," + name);
                    buffer.append(":" + classe);
                }

            }

            SimpleFeatureType featureType = DataUtilities.createType("clip_" + title, buffer.toString());

            myData.createSchema(featureType);

            // Tell the DataStore what type of Coordinate Reference System (CRS) to use
            myData.forceSchemaCRS(crs);

            myData.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SchemaException se) {
            se.printStackTrace();
        }

        return myData;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXTitledSeparator1 = new org.jdesktop.swingx.JXTitledSeparator();
        jLabel1 = new javax.swing.JLabel();
        gui_jcb_inlayer = new javax.swing.JComboBox();
        gui_but_infile = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        gui_jcb_cliplayer = new javax.swing.JComboBox();
        gui_but_clipfile = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        gui_jtf_outfile = new javax.swing.JTextField();
        gui_but_outfile = new javax.swing.JButton();
        gui_ok = new javax.swing.JButton();
        gui_jtf_inexternal = new javax.swing.JTextField();
        gui_jtf_clipexternal = new javax.swing.JTextField();
        gui_progress = new javax.swing.JProgressBar();

        jXTitledSeparator1.setTitle(org.openide.util.NbBundle.getMessage(ClippingTool.class, "clip")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "input_layer")); // NOI18N

        gui_jcb_inlayer.setModel(inModel);

        gui_but_infile.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "...")); // NOI18N
        gui_but_infile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_infileActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "clip_geom")); // NOI18N

        gui_jcb_cliplayer.setModel(clipModel);

        gui_but_clipfile.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "...")); // NOI18N
        gui_but_clipfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_clipfileActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "output_layer")); // NOI18N

        gui_jtf_outfile.setEditable(false);

        gui_but_outfile.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "...")); // NOI18N
        gui_but_outfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_outfileActionPerformed(evt);
            }
        });

        gui_ok.setText(org.openide.util.NbBundle.getMessage(ClippingTool.class, "ok")); // NOI18N
        gui_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_okActionPerformed(evt);
            }
        });

        gui_jtf_inexternal.setEditable(false);

        gui_jtf_clipexternal.setEditable(false);

        gui_progress.setString("");
        gui_progress.setStringPainted(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jXTitledSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_jcb_inlayer, 0, 372, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(gui_progress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_ok))
                    .add(layout.createSequentialGroup()
                        .add(gui_jtf_inexternal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_but_infile))
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_jcb_cliplayer, 0, 332, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(gui_jtf_clipexternal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_but_clipfile))
                    .add(jLabel3)
                    .add(layout.createSequentialGroup()
                        .add(gui_jtf_outfile, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_but_outfile)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jXTitledSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(gui_jcb_inlayer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(gui_but_infile)
                    .add(gui_jtf_inexternal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(gui_jcb_cliplayer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(gui_but_clipfile)
                    .add(gui_jtf_clipexternal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(gui_but_outfile)
                    .add(gui_jtf_outfile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 43, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gui_progress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, gui_ok))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void gui_but_outfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gui_but_outfileActionPerformed
        JFileChooser jfc = new JFileChooser(outFile);
        int act = jfc.showSaveDialog(null);

        if (act == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();

            if (f != null) {
                if (f.getAbsolutePath().endsWith(".shp")) {
                    outFile = f;
                    gui_jtf_outfile.setText(outFile.getAbsolutePath());
                } else {
                    int lastdot = f.getAbsolutePath().lastIndexOf(".");
                    if (lastdot > 0) {
                        f = new File(f.getAbsolutePath().substring(0, lastdot) + ".shp");
                    } else {
                        f = new File(f.getAbsolutePath() + ".shp");
                    }

                    outFile = f;
                    gui_jtf_outfile.setText(outFile.getAbsolutePath());
                }
            }

        }

        verify();
}//GEN-LAST:event_gui_but_outfileActionPerformed

    private void gui_but_infileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gui_but_infileActionPerformed
        //liste des panneaux visibles
        List<DataPanel> lst = new ArrayList<DataPanel>();
        lst.add(new JFileDataPanel());
        lst.add(new JPostGISDataPanel());
        lst.add(new JOracleDataPanel());

        JDataChooser jdc = new JDataChooser(null, lst);
        JDataChooser.ACTION ret = jdc.showDialog();

        if (ret == JDataChooser.ACTION.APPROVE) {
            //tous les MapLayers correspondants aux fichiers/tables sélectionnées.
            MapLayer[] layers = jdc.getLayers();
            if (!(layers.length == 0)) {
                inExternLayer = layers[0];
                inLayer = inExternLayer;
                gui_jtf_inexternal.setText(inLayer.getTitle());
            }
        }

        verify();
    }//GEN-LAST:event_gui_but_infileActionPerformed

    private void gui_but_clipfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gui_but_clipfileActionPerformed
        //liste des panneaux visibles
        List<DataPanel> lst = new ArrayList<DataPanel>();
        lst.add(new JFileDataPanel());
        lst.add(new JPostGISDataPanel());
        lst.add(new JOracleDataPanel());

        JDataChooser jdc = new JDataChooser(null, lst);
        JDataChooser.ACTION ret = jdc.showDialog();

        if (ret == JDataChooser.ACTION.APPROVE) {
            //tous les MapLayers correspondants aux fichiers/tables sélectionnées.
            MapLayer[] layers = jdc.getLayers();
            if (!(layers.length ==0)) {
                clipExternLayer = layers[0];
                clipLayer = clipExternLayer;
                gui_jtf_clipexternal.setText(clipLayer.getTitle());
            }
        }

        verify();
    }//GEN-LAST:event_gui_but_clipfileActionPerformed

    private void gui_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gui_okActionPerformed



        FeatureSource<SimpleFeatureType, SimpleFeature> inFS = (FeatureSource<SimpleFeatureType, SimpleFeature>) inLayer
                .getFeatureSource();
        SimpleFeatureType inType = inFS.getSchema();

        DataStore outStore = createShapeFile(outFile, inType, inType.getCRS(), inLayer.getTitle());
        
        try{
            attlink.put(inType.getDefaultGeometry().getName().toString(), 
                    outStore.getSchema(outStore.getTypeNames()[0]).getDefaultGeometry().getName().toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            FeatureCollection<SimpleFeatureType, SimpleFeature> source = (FeatureCollection<SimpleFeatureType, SimpleFeature>) inLayer.getFeatureSource().getFeatures();
            FeatureCollection<SimpleFeatureType, SimpleFeature> clip = (FeatureCollection<SimpleFeatureType, SimpleFeature>) clipLayer.getFeatureSource().getFeatures();
            FeatureStore<SimpleFeatureType, SimpleFeature> out = (FeatureStore<SimpleFeatureType, SimpleFeature>) outStore.getFeatureSource(outStore.getTypeNames()[0]);


//            final ClipProcess process = new ClipProcess(source, clip, out,attlink);


            gui_ok.setEnabled(false);
            gui_progress.setValue(0);

//            process.setMonitor(new ProcessListener() {
//
//                public void processChanged(int val, int max, String desc) {
//                    gui_progress.setMaximum(max);
//                    gui_progress.setValue(val);
//                    gui_progress.setString(desc);
//                }
//
//                public void objectCreated(Object obj) {
//                }
//
//                public void processEnded(String desc) {
//                    gui_progress.setString(desc);
//                    gui_ok.setEnabled(true);
//                }
//
//                public void processInterrupted(Exception e) {
//                    gui_progress.setString(error);
//                    e.printStackTrace();
//                    gui_ok.setEnabled(true);
//                }
//            });
            
            
//            Process process = new ... ;
//            ProgressListener monitor = new ... ;
//            process.setMonitor(monitor);
//                 
//            new Thread(){
//                @Override
//                public void run(){
//                    process.process();
//                }
//            }.start();
            
//            ProcessMonitor monitor = new ProcessMonitor(null, true);            
//            process.setMonitor(monitor);
//            
//            new Thread(){
//                @Override
//                public void run(){
//                    process.process();
//                }
//            }.start();
            

            

        } catch (IOException e) {
            System.out.println(e);
        }
}//GEN-LAST:event_gui_okActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gui_but_clipfile;
    private javax.swing.JButton gui_but_infile;
    private javax.swing.JButton gui_but_outfile;
    private javax.swing.JComboBox gui_jcb_cliplayer;
    private javax.swing.JComboBox gui_jcb_inlayer;
    private javax.swing.JTextField gui_jtf_clipexternal;
    private javax.swing.JTextField gui_jtf_inexternal;
    private javax.swing.JTextField gui_jtf_outfile;
    private javax.swing.JButton gui_ok;
    private javax.swing.JProgressBar gui_progress;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator1;
    // End of variables declaration//GEN-END:variables
}
