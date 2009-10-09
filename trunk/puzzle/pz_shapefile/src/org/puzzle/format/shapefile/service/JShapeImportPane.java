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
package org.puzzle.format.shapefile.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.geotoolkit.data.DataStore;
import org.geotoolkit.data.DataStoreFinder;
import org.geotoolkit.data.FeatureSource;
import org.geotoolkit.gui.swing.misc.FileFilterFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.openide.util.Exceptions;
import org.puzzle.core.project.source.capabilities.JImportPane;

/**
 *
 * @author eclesia
 */
public class JShapeImportPane extends JImportPane {

    /** Creates new form JShapeImportPane */
    public JShapeImportPane() {
        initComponents();
        
        FileFilter filter = FileFilterFactory.createFileFilter(FileFilterFactory.FORMAT.ESRI_SHAPEFILE);

        guiChooser.addChoosableFileFilter( filter);
        guiChooser.setAcceptAllFileFilterUsed(false);
        guiChooser.setMultiSelectionEnabled(false);
        guiChooser.setFileFilter(filter);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guiChooser = new JFileChooser();

        guiChooser.setControlButtonsAreShown(false);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(guiChooser, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(guiChooser, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFileChooser guiChooser;
    // End of variables declaration//GEN-END:variables

    @Override
    public FeatureSource<SimpleFeatureType,SimpleFeature> getSource() {
        File shapefile = guiChooser.getSelectedFile();

        if(shapefile == null) return null;

        DataStore store = null;
        try {
            store = DataStoreFinder.getDataStore(Collections.singletonMap("url",(Serializable)shapefile.toURI().toURL()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        
        FeatureSource<SimpleFeatureType,SimpleFeature> featureSource = null;
        try {
            featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        
        return featureSource;
    }

}
