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
package org.puzzle.format.postgis.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.geotools.data.DataAccessFactory.Param;

/**
 * Keys model
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class KeyModel implements TableModel {

    private Param[] param ={};
    private Object[] value = {};
    private JTable tab;

    /** Creates a new instance of BasicTableModel 
     * @param tab 
     */
    public KeyModel(JTable tab) {
        super();
        this.tab = tab;
        init();
    }

    private void init() {
        tab.revalidate();
    }

    public void setParam(Param[] param) {
        this.param = param;
        tab.revalidate();
        tab.repaint();
        
        value = new Object[param.length];
        for(int i=0;i<param.length;i++){
            value[i] = param[i].sample;
        }
        
    }

    public Map getProperties() {

        Map config = new HashMap();
        
        for(int i=0;i<param.length;i++){
            config.put(param[i].key, value[i]);
        }
        
        return config;
    }
    
    public void parse(Map map){
        
        for(int i=0;i<getRowCount();i++){            
            Object key = getValueAt(i, 0);            
            if(map.containsKey(key)){                
                setValueAt(map.get(key), i, 1);
            }            
        }
        
    }
    

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Class getColumnClass(int i) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "key";
        } else {
            return "value";
        }
    }

    @Override
    public int getRowCount() {
        return param.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0) {
            return param[rowIndex].key;
        } else {
            return value[rowIndex];
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        value[rowIndex] = aValue;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
