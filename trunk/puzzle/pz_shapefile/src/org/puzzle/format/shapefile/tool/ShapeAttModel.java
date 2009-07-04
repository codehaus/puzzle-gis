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
package org.puzzle.format.shapefile.tool;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;


/**
 * TableModel for fields edition in the shapefile creation tool.
 * 
 * @author Johann Sorel
 */
class ShapeAttModel extends AbstractTableModel{

    private List<Field> datas = new ArrayList<Field>();
    
    /**
     * {@inheritDoc }
     */
    @Override
    public int getRowCount() {
        return datas.size();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getColumnCount() {        
        return 2;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex == 0){
            return NbBundle.getMessage(ShapeAttModel.class, "name");
        }else{
            return NbBundle.getMessage(ShapeAttModel.class, "type");
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0){
            return String.class;
        }else{
            return FieldType.class;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    Field getDataAt(int rowIndex){
        return datas.get(rowIndex);
    }
    
    Field[] getDatas(){
        return datas.toArray(new Field[datas.size()]);
    }
    
    int indexOf(Field data){
        return datas.indexOf(data);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return datas.get(rowIndex).getName();
        }else{
            return datas.get(rowIndex).getType();
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            datas.get(rowIndex).setName((String) aValue);
        }else{
            datas.get(rowIndex).setType((FieldType) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    void addAttribut(){
        Field newData = new Field();
        datas.add(newData);
        fireTableRowsInserted(datas.indexOf(newData), datas.indexOf(newData));
    }
    
    void deleteAttribut(Field data){
        int index = datas.indexOf(data);
        datas.remove(data);
        fireTableRowsDeleted(index, index);
    }

    void moveUp(Field data){
        int index = datas.indexOf(data);
        if(index > 0){
            datas.remove(index);
            fireTableRowsDeleted(index, index);
            datas.add(index-1, data);
            fireTableRowsInserted(index-1, index-1);
        }
        
    }
    
    void moveDown(Field data){
        int index = datas.indexOf(data);
        if(index >= 0 && index < datas.size()-1 ){
            datas.remove(index);
            fireTableRowsDeleted(index, index);
            datas.add(index+1, data);
            fireTableRowsInserted(index+1, index+1);
        }
    }
    
    
}


