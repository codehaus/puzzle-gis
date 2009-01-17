/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

package org.puzzle.core.project.source.capabilities;

import org.puzzle.core.project.source.*;
import javax.swing.JPanel;
import org.geotools.map.MapLayer;

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
