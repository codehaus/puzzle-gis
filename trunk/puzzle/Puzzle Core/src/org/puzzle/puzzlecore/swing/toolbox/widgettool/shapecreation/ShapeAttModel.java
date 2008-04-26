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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.shapecreation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import org.puzzle.puzzlecore.swing.toolbox.widgettool.shapecreation.Data.TYPE;



/**
 *
 * @author johann sorel
 */
class ShapeAttModel extends AbstractTableModel{

    private List<Data> datas = new ArrayList<Data>();
    
    
    public int getRowCount() {
        return datas.size();
    }

    public int getColumnCount() {        
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex == 0){
            return "Name";
        }else{
            return "Type";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0){
            return String.class;
        }else{
            return Data.TYPE.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    Data getDataAt(int rowIndex){
        return datas.get(rowIndex);
    }
    
    Data[] getDatas(){
        return datas.toArray(new Data[datas.size()]);
    }
    
    int indexOf(Data data){
        return datas.indexOf(data);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return datas.get(rowIndex).name;
        }else{
            return datas.get(rowIndex).type;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            datas.get(rowIndex).name = (String) aValue;
        }else{
            datas.get(rowIndex).type = (TYPE) aValue;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    void addAttribut(){
        Data newData = new Data();
        datas.add(newData);
        fireTableRowsInserted(datas.indexOf(newData), datas.indexOf(newData));
    }
    
    void deleteAttribut(Data data){
        int index = datas.indexOf(data);
        datas.remove(data);
        fireTableRowsDeleted(index, index);
    }

    void moveUp(Data data){
        int index = datas.indexOf(data);
        if(index > 0){
            datas.remove(index);
            fireTableRowsDeleted(index, index);
            datas.add(index-1, data);
            fireTableRowsInserted(index-1, index-1);
        }
        
    }
    
    void moveDown(Data data){
        int index = datas.indexOf(data);
        if(index >= 0 && index < datas.size()-1 ){
            datas.remove(index);
            fireTableRowsDeleted(index, index);
            datas.add(index+1, data);
            fireTableRowsInserted(index+1, index+1);
        }
    }
    
    
}


