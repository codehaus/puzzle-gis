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

package org.puzzle.format;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.geotoolkit.data.DataStore;
import org.geotoolkit.storage.DataStoreException;

import org.openide.util.Exceptions;

import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.resources.CoreResource;

/**
 * Abstract creation panel for distant datastore sources.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class AbstractDataStoreCreationPane extends SourceCreationPane {

    protected AbstractDataStoreCreationPane(){
        setLayout(new BorderLayout());

        final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        final JPanel east = new JPanel(new BorderLayout());
        final JList typeList = new JList();
        final JButton button = new JButton(CoreResource.getString("test"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeList.setModel(new DefaultListModel());
                final DataStore store = createDataStore();
                if (store != null) {
                    try {
                        typeList.setModel(new DefaultComboBoxModel(store.getTypeNames()));
                    } catch (DataStoreException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });

        east.add(BorderLayout.NORTH,button);
        east.add(BorderLayout.CENTER,new JScrollPane(typeList));
        pane.setLeftComponent(createConfigurationPane());
        pane.setRightComponent(east);
        pane.setDividerSize(3);
        add(BorderLayout.CENTER,pane);
    }

    protected abstract JComponent createConfigurationPane();

    protected abstract DataStore createDataStore();

}
