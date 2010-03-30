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
package org.puzzle.format.wfs.service;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.Collections;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geotoolkit.data.DataStore;
import org.geotoolkit.storage.DataStoreException;
import org.geotoolkit.gui.swing.resource.IconBundle;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.DefaultStyleFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.openide.util.Exceptions;
import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.capabilities.LayerCreation;
import org.puzzle.format.wfs.resources.WFSResource;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class LayerCreationComponent extends JLayerChooser {

    private final DataStore store;
    private final WFSSource source;

    /** Creates new form LayerCreationComponent */
    LayerCreationComponent(final LayerChooserMonitor monitor, DataStore store, WFSSource source) {
        super(monitor);
        this.store = store;
        this.source = source;
        initComponents();

        guiLayerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        try {
            guiLayerList.setModel(new DefaultComboBoxModel(store.getTypeNames()));
            guiLayerList.revalidate();
        } catch (DataStoreException ex) {
            System.out.println(ex);
        }

        guiLayerList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object selection = guiLayerList.getSelectedValue();
                if(selection != null){
                    guiTitle.setText(selection.toString());
                    monitor.setReady(true);
                }
            }
        });

        guiLayerList.setCellRenderer(new TypeRender());

    }

    private String getType(){
        Object selection = guiLayerList.getSelectedValue();
        if(selection != null){
             return selection.toString();
        }else{
            return null;
        }
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
        jsp = new JScrollPane();
        guiLayerList = new JList();

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | Font.BOLD));
        jLabel2.setText(WFSResource.getString("title")); // NOI18N

        jsp.setViewportView(guiLayerList);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(jsp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(guiTitle, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(guiTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jsp, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * {@inheritDoc }
     */
    @Override
    public MapLayer[] getLayers() {
        String title = guiTitle.getText();
        String type = getType();
        if(type != null){
            MapLayer layer = source.getLookup().lookup(LayerCreation.class).createLayer(Collections.singletonMap(WFSSource.FEATURETYPENAME_PROP, type));
            layer.setDescription(new DefaultStyleFactory().description(title,"") );
            return new MapLayer[]{layer};
        }

        return new MapLayer[0];
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JList guiLayerList;
    private JTextField guiTitle;
    private JLabel jLabel2;
    private JScrollPane jsp;
    // End of variables declaration//GEN-END:variables

    private static final Icon POINT = IconBundle.getInstance().getIcon("16_single_point");
    private static final Icon LINE = IconBundle.getInstance().getIcon("16_single_line");
    private static final Icon POLYGON = IconBundle.getInstance().getIcon("16_single_polygon");
    private static final Icon MPOINT = IconBundle.getInstance().getIcon("16_multi_point");
    private static final Icon MLINE = IconBundle.getInstance().getIcon("16_multi_line");
    private static final Icon MPOLYGON = IconBundle.getInstance().getIcon("16_multi_polygon");

    private class TypeRender extends DefaultListCellRenderer{

        @Override
        public Component getListCellRendererComponent(JList arg0, Object value, int arg2, boolean arg3, boolean arg4) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(arg0, value, arg2, arg3, arg4);

            if(value instanceof String){
                try {
                    final SimpleFeatureType sft = (SimpleFeatureType) store.getFeatureType(value.toString());
                    final GeometryDescriptor desc = sft.getGeometryDescriptor();
                    if(desc == null){
                        //geometryless table
                        lbl.setIcon(null);
                    }else{
                        final Class binding = sft.getGeometryDescriptor().getType().getBinding();

                        if(binding.isAssignableFrom(Point.class)){
                            lbl.setIcon(POINT);
                        }else if(binding.isAssignableFrom(LineString.class)){
                            lbl.setIcon(LINE);
                        }else if(binding.isAssignableFrom(Polygon.class)){
                            lbl.setIcon(POLYGON);
                        }else if(binding.isAssignableFrom(MultiPoint.class)){
                            lbl.setIcon(MPOINT);
                        }else if(binding.isAssignableFrom(MultiLineString.class)){
                            lbl.setIcon(MLINE);
                        }else if(binding.isAssignableFrom(MultiPolygon.class)){
                            lbl.setIcon(MPOLYGON);
                        }
                    }

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            return lbl;
        }



    }

}
