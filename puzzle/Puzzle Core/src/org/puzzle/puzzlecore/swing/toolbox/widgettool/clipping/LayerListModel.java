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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.geotools.map.MapLayer;

/**
 *
 * @author johann sorel
 */
class LayerListModel extends AbstractListModel implements ComboBoxModel {

    static final String OUT_FILE = ResourceBundle.getBundle("org/geotools/gui/swing/toolbox/tools/clipping/Bundle").getString("file");
    private List layers = new ArrayList();
    private Object selected = null;

    LayerListModel() {
        layers.add(OUT_FILE);
    }

    public int getSize() {
        return layers.size();
    }

    public Object getElementAt(int index) {
        return layers.get(index);
    }

    public void setLayers(MapLayer[] layers) {
        this.layers = new ArrayList();
        for(MapLayer layer : layers){
                this.layers.add(layer);
            }
        this.layers.add(OUT_FILE);
    }

    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    public Object getSelectedItem() {
        return selected;
    }

    
}
