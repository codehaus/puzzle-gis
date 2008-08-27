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
package org.puzzle.puzzlecore.gtextend.widget.viewtree;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;
import javax.swing.DefaultListCellRenderer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.geotools.gui.swing.contexttree.renderer.RenderAndEditComponent;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.Lookup;
import org.puzzle.puzzlecore.context.ContextService;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author johann sorel
 */
final class ContextComponent extends RenderAndEditComponent implements TableCellRenderer {

    private final JComboBox box = new JComboBox();

    ContextComponent() {
        super();
        setLayout(new GridLayout(1, 1));
        box.setOpaque(false);


        box.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value != null && value instanceof MapContext) {
                    MapContext context = (MapContext) value;
                    lbl.setText(context.getTitle());
                }

                return lbl;
            }
            });

    }

    @Override
    public void parse(Object obj) {
        
        removeAll();
        if (obj instanceof MapContext) {
            box.removeAllItems();
            
            Collection<MapContext> contexts = (Collection<MapContext>) Lookup.getDefault().lookup(ContextService.class).getLookup().lookupAll(MapContext.class);
            
            for(MapContext context : contexts){
                box.addItem(context);
            }

            box.setSelectedItem(obj);
            add(box);
        }

    }

    @Override
    public Object getValue() {
        return box.getSelectedItem();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        parse(value);
        return this;
    }
}
