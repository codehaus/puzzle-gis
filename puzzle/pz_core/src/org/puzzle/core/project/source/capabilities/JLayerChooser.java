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
package org.puzzle.core.project.source.capabilities;

import javax.swing.JPanel;
import org.geotoolkit.map.MapLayer;

/**
 * Swing component usd to select layer from the GISSource.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class JLayerChooser extends JPanel {

    private final LayerChooserMonitor monitor;

    protected JLayerChooser(LayerChooserMonitor monitor){
        if(monitor == null){
            throw new NullPointerException("monitor can not be null");
        }
        this.monitor = monitor;
    }

    public LayerChooserMonitor getMonitor(){
        return monitor;
    }

    public abstract MapLayer[] getLayers();

}
