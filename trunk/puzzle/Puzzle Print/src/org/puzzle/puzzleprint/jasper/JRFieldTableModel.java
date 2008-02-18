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

package org.puzzle.puzzleprint.jasper;

import javax.swing.table.AbstractTableModel;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author johann sorel
 */
public class JRFieldTableModel extends AbstractTableModel{

    JRField[] parameters = null;
    Object[] values = null;
    
    
    public JRFieldTableModel(JasperReport report){
        parameters = report.getFields();
        values = new Object[parameters.length];
    }
    
    public int getRowCount() {
        return parameters.length;
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex == 0){
            return "Name";
        }else{
            return "Value";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0){
            return String.class;
        }else{
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return false;
        }else{
            return true;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return parameters[rowIndex].getName();
        }else{
            return values[rowIndex];
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        values[rowIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }


}
