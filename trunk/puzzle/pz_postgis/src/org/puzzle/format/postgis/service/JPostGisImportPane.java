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
package org.puzzle.format.postgis.service;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.DataStoreFinder;
import org.geotoolkit.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.openide.util.Exceptions;
import org.puzzle.core.project.source.capabilities.JImportPane;
import org.puzzle.format.postgis.ui.JPostGISconfigPanel;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JPostGisImportPane extends JImportPane {

    private final JPostGISconfigPanel guiConfig = new JPostGISconfigPanel();
    private final JList guiLayerList = new JList();

    /** Creates new form JShapeImportPane */
    public JPostGisImportPane() {
        initComponents();

        final JButton guiSearch = new JButton(new AbstractAction(Utilities.getString("connect")) {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    guiLayerList.setModel(new DefaultComboBoxModel(getDataStore().getTypeNames()));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        final JPanel guiEast = new JPanel(new BorderLayout());
        guiEast.add(BorderLayout.NORTH,guiSearch);
        guiEast.add(BorderLayout.CENTER,new JScrollPane(guiLayerList));

        add(BorderLayout.CENTER,guiConfig);
        add(BorderLayout.EAST,guiEast);
    }

    private DataStore getDataStore() throws IOException{
        return DataStoreFinder.getDataStore(guiConfig.getParams());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public FeatureSource<SimpleFeatureType,SimpleFeature> getSource() {

        DataStore store = null;
        try {
            store = getDataStore();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        
        FeatureSource<SimpleFeatureType,SimpleFeature> featureSource = null;
        try {
            featureSource = store.getFeatureSource(guiLayerList.getSelectedValue().toString());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        
        return featureSource;
    }

}
