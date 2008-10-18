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

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

/**
 * DataBase model
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class DBModel implements TableModel{
    
    
    private ArrayList<String> datas = new ArrayList<String>();
    private JXTable tab;
    
    /** Creates a new instance of BasicTableModel 
     * @param tab 
     */
    public DBModel(JXTable tab) {
        super();
        this.tab = tab;
        init();
    }
    
    private void init(){
        tab.revalidate();
    }
    
    
    public void removeSelected(){
        for(int i=tab.getSelectedRows().length-1; i>=0; i--){            
            datas.remove(tab.getSelectedRows()[i]);
        }
        tab.revalidate();
        tab.repaint();
    }
    
    public void add(String name){
        datas.add(name);
        tab.revalidate();
        tab.repaint();
    }
    
    public void add(String[] names){
        for(int i=0;i<names.length;i++)
            datas.add(names[i]);
        
        tab.revalidate();
        tab.repaint();
    }
    
    public void clean(){
        datas.clear();
        tab.revalidate();
        tab.repaint();
    }
    
    public List<String> getNames(){
        return datas;
    }
    
    public int getColumnCount(){
        return 1;
    }
    
    public Class getColumnClass(int i){
        return String.class;
    }
    
    public String getColumnName(int column) {
        return "";
    }
    
    public int getRowCount() {
        return datas.size();
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return datas.get(rowIndex);
    }
            
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
    public void addTableModelListener(TableModelListener l) {}
    public void removeTableModelListener(TableModelListener l) {}
    
}
