/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.core.attributetable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.gui.swing.propertyedit.LayerFeaturePropertyPanel;
import org.geotoolkit.map.FeatureMapLayer;
import org.opengis.filter.Id;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class CopyAction extends AbstractDataBufferAction{

    public CopyAction(){
        setText(Utilities.getString("copy"));

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final LayerFeaturePropertyPanel panel = getFeaturePanel();
                if(panel == null) return;
                final FeatureMapLayer layer = panel.getTarget();
                if(layer == null) return;

                Id selection = layer.getSelectionFilter();
                if(selection != null){
                    Id copy = FactoryFinder.getFilterFactory(null).id(selection.getIdentifiers());
                    setLayer(layer);
                    setFilter(copy);
                }

            }
        });
    }

}
