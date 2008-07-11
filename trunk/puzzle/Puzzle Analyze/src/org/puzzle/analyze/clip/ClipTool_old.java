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
package org.puzzle.analyze.clip;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JFileDataPanel;
import org.geotools.gui.swing.misc.Render.LayerListRenderer;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.Serializable;
import javax.swing.JPanel;
import org.geotools.gui.swing.datachooser.JOracleDataPanel;
import org.geotools.gui.swing.datachooser.JPostGISDataPanel;

/**
 *
 * @author johann sorel
 */
public class ClipTool_old extends JPanel {

    private final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private LayerListModel inModel = new LayerListModel();
    private LayerListModel clipModel = new LayerListModel();
    private File outFile = null;
    private MapLayer inLayer = null;
    private MapLayer inExternLayer = null;
    private MapLayer clipLayer = null;
    private MapLayer clipExternLayer = null;
    private Map<String, String> attlink = null;
    private String error = ResourceBundle.getBundle("org/geotools/gui/swing/toolbox/tools/clipping/Bundle").getString("error");

    /** 
     * Creates new form ClippingTool 
     * @param parameters 
     */
    public ClipTool_old(Map parameters) {
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

            Class jtsClass = clipLayer.getFeatureSource().getSchema().getGeometryDescriptor().getType().getBinding();

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
            Map<String, Serializable> map = Collections.singletonMap("url", (Serializable)file.toURI().toURL());

            // Create the ShapefileDataStore from our factory based on our Map object
            myData = (ShapefileDataStore) factory.createNewDataStore(map);

            AttributeDescriptor geodesc = type.getGeometryDescriptor();
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


            List<AttributeDescriptor> lst = type.getAttributeDescriptors();
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

    private void fillLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> lstFeatures, FeatureSource<SimpleFeatureType, SimpleFeature> datastore) {
        FeatureStore<SimpleFeatureType, SimpleFeature> store;

        if (datastore instanceof FeatureStore) {
            store = (FeatureStore<SimpleFeatureType, SimpleFeature>) datastore;

            DefaultTransaction transaction = new DefaultTransaction();
            store.setTransaction(transaction);

            try {
                store.addFeatures(lstFeatures);
                transaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    store.getTransaction().rollback();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                transaction.close();
            }
        }
    }

    private void clip(
            CoordinateReferenceSystem inCRS,
            CoordinateReferenceSystem clipCRS,
            FeatureCollection<SimpleFeatureType, SimpleFeature> outCol,
            SimpleFeature inSF,
            SimpleFeatureType outType)
            throws IllegalArgumentException {

        boolean sameCRS = inCRS.equals(clipCRS);
        MathTransform transformToClipCRS = null;
        MathTransform transformToInCRS = null;

        Geometry inGeom = null;



        if (sameCRS) {
            inGeom = (Geometry) inSF.getDefaultGeometry();
        } else {

            try {
                transformToClipCRS = CRS.findMathTransform(inCRS, clipCRS, true);
                transformToInCRS = CRS.findMathTransform(clipCRS, inCRS, true);
            } catch (FactoryException ex) {
                throw new IllegalArgumentException();
            }

            try {
                inGeom = JTS.transform((Geometry) inSF.getDefaultGeometry(), transformToClipCRS);
            } catch (MismatchedDimensionException ex) {
                throw new IllegalArgumentException();
            } catch (TransformException ex) {
                throw new IllegalArgumentException();
            }

        }

        if (inGeom != null) {

            //We should make a second filter here to reduce the feature list ans speed up
            //JTSGeometryFactory geoFact = new JTSGeometryFactory();
            //FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
            //Filter filter = ff.intersects(ff.property(geoAtt), ff.literal(env));

            FeatureCollection<SimpleFeatureType, SimpleFeature> clipCol = null;
            try {
                clipCol = (FeatureCollection<SimpleFeatureType, SimpleFeature>) clipLayer.getFeatureSource().getFeatures();
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }


            if (clipCol != null) {
                FeatureIterator<SimpleFeature> ite = clipCol.features();

                //we create the clipped geometry
                Geometry outGeom = null;
                while (ite.hasNext()) {
                    Geometry clipGeom = (Geometry) ite.next().getDefaultGeometry();

                    if (inGeom.intersects(clipGeom)) {
                        Geometry partGeom = inGeom.intersection(clipGeom);
                        outGeom = (outGeom == null) ? partGeom : outGeom.union(partGeom);
                    }
                }

                if (outGeom != null) {

                    //we project back to original CRS if needed
                    if (!sameCRS) {
                        try {
                            outGeom = JTS.transform( outGeom, transformToInCRS );
                        } catch (MismatchedDimensionException ex) {
                            throw new IllegalArgumentException();
                        } catch (TransformException ex) {
                            throw new IllegalArgumentException();
                        }
                    }

                    //we verify the geometry is a Multi-Geometry
                    if (outGeom instanceof Point) {
                        outGeom = GEOMETRY_FACTORY.createMultiPoint(new Point[]{(Point) outGeom});
                    } else if (outGeom instanceof LineString) {
                        outGeom = GEOMETRY_FACTORY.createMultiLineString(new LineString[]{(LineString) outGeom});
                    } else if (outGeom instanceof Polygon) {
                        outGeom = GEOMETRY_FACTORY.createMultiPolygon(new Polygon[]{(Polygon) outGeom});
                    }

                    //we copy each attribut and replace the geometry
                    Object[] values = new Object[outType.getAttributeCount()];
                    AttributeDescriptor geomAttribut = outType.getGeometryDescriptor();
                    List<AttributeDescriptor> attributes = outType.getAttributeDescriptors();

                    for (int i = 0,  max = attributes.size(); i < max; i++) {
                        AttributeDescriptor oneAttribut = attributes.get(i);

                        if (oneAttribut.equals(geomAttribut)) {
                            values[i] = outGeom;
                        } else {
                            Object obj = inSF.getAttribute(attlink.get(oneAttribut.getName().toString()));
                            values[i] = (obj != null) ? obj : oneAttribut.getDefaultValue();
                        }
                    }

                    SimpleFeature myFeature = SimpleFeatureBuilder.build(outType, values, null);
                    outCol.add(myFeature);
                }

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

        jXTitledSeparator1.setTitle("null");

        jLabel1.setText("null");

        gui_jcb_inlayer.setModel(inModel);

        gui_but_infile.setText("null");
        gui_but_infile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_infileActionPerformed(evt);
            }
        });

        jLabel2.setText("null");

        gui_jcb_cliplayer.setModel(clipModel);

        gui_but_clipfile.setText("null");
        gui_but_clipfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_clipfileActionPerformed(evt);
            }
        });

        jLabel3.setText("null");

        gui_jtf_outfile.setEditable(false);

        gui_but_outfile.setText("null");
        gui_but_outfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gui_but_outfileActionPerformed(evt);
            }
        });

        gui_ok.setText("null");
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
                        .add(gui_jcb_inlayer, 0, 362, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(gui_progress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_ok))
                    .add(layout.createSequentialGroup()
                        .add(gui_jtf_inexternal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_but_infile))
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_jcb_cliplayer, 0, 369, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(gui_jtf_clipexternal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gui_but_clipfile))
                    .add(jLabel3)
                    .add(layout.createSequentialGroup()
                        .add(gui_jtf_outfile, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
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
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 47, Short.MAX_VALUE)
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
            if (!(layers.length ==0)) {
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
                inExternLayer = layers[0];
                clipLayer = clipExternLayer;
                gui_jtf_clipexternal.setText(clipLayer.getTitle());
            }
        }

        verify();
    }//GEN-LAST:event_gui_but_clipfileActionPerformed

    private void gui_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gui_okActionPerformed




        new clipThread().start();                
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

    // private classes ---------------------------------------------------------
    private class clipThread extends Thread {

        private int x = 0;
        private int max = 0;

        @Override
        public void run() {
            gui_ok.setEnabled(false);
            gui_progress.setValue(0);

            FeatureSource<SimpleFeatureType, SimpleFeature> inFS = (FeatureSource<SimpleFeatureType, SimpleFeature>) inLayer.getFeatureSource();
            SimpleFeatureType inType = inFS.getSchema();
            CoordinateReferenceSystem inCRS = inLayer.getFeatureSource().getSchema().getCoordinateReferenceSystem();

            CoordinateReferenceSystem clipCRS = clipLayer.getFeatureSource().getSchema().getCoordinateReferenceSystem();

            DataStore outStore = createShapeFile(outFile, inType, inType.getCoordinateReferenceSystem(), inLayer.getTitle());

            String geoAtt = inLayer.getFeatureSource().getSchema().getGeometryDescriptor().getName().toString();

            //we make a first filter with envelope to limite the collection size
            ReferencedEnvelope env = clipLayer.getBounds();
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
            Filter filter = ff.intersects(ff.property(geoAtt), ff.literal(env));

            try {
                SimpleFeatureType outType = outStore.getSchema(outStore.getTypeNames()[0]);

                FeatureCollection<SimpleFeatureType, SimpleFeature> inCol = inFS.getFeatures(filter);
                FeatureIterator<SimpleFeature> ite = inCol.features();

                FeatureCollection<SimpleFeatureType, SimpleFeature> outCol = FeatureCollections.newCollection();
                max = inCol.size();
                gui_progress.setMaximum(max);

                x = 0;
                while (ite.hasNext()) {
                    x++;
                    SimpleFeature sf = ite.next();
                    clip(inCRS, clipCRS, outCol, sf, outType);

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            gui_progress.setValue(x);
                            gui_progress.setString(x + " / " + max);
                        }
                    });

                }

                String name = outStore.getTypeNames()[0];
                FeatureSource<SimpleFeatureType, SimpleFeature> source = outStore.getFeatureSource(name);
                fillLayer(outCol, source);

//                fireObjectCreation(new Object[]{outStore});

            } catch (IOException ex) {
                gui_progress.setString(error);
            } catch (IllegalArgumentException i) {
                gui_progress.setString(error);
            } finally {
                gui_ok.setEnabled(true);
            }


        }
    }
}
