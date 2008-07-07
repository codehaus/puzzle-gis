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

package org.puzzle.format.shapefile.tool;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import org.puzzle.format.shapefile.tool.Data.TYPE;


/**
 *
 * @author johann sorel
 */
public class TypeEditor implements TableCellEditor{

    private final EventListenerList LISTENERS = new EventListenerList();
    private JComboBox box = new JComboBox();
    
    TypeEditor(){        
        box.addItem(TYPE.INTEGER);
        box.addItem(TYPE.LONG);
        box.addItem(TYPE.DOUBLE);
        box.addItem(TYPE.STRING);
        box.addItem(TYPE.DATE);
    }
    
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {        
        box.setSelectedItem(value);
        return box;
    }

    public Object getCellEditorValue() {
        return box.getSelectedItem();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        CellEditorListener[] lst = LISTENERS.getListeners(CellEditorListener.class);
        
        for(CellEditorListener l : lst){
            l.editingStopped(new ChangeEvent(this));
        }
        
        return true;
    }

    public void cancelCellEditing() {
        CellEditorListener[] lst = LISTENERS.getListeners(CellEditorListener.class);
        
        for(CellEditorListener l : lst){
            l.editingCanceled(new ChangeEvent(this));
        }
    }

    public void addCellEditorListener(CellEditorListener l) {
        LISTENERS.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        LISTENERS.remove(CellEditorListener.class, l);
    }

}
