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

package org.puzzle.shell.misc.runtime;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;
import org.puzzle.shell.misc.runtime.tabletool.ProcessCell;

/** 
 * Modele de tableau pour ProcessObjectPanel
 * @author johann Sorel
 */
public class ProcessTableModel extends AbstractTableModel{
    
    private static final long serialVersionUID = 1;
    private String[] columnNames = {"","",""};
    private Class[] columnClass = {JProgressBar.class,JButton.class,JButton.class};
    private ArrayList<ArrayList<ProcessCell>> data = new ArrayList<ArrayList<ProcessCell>>();
    
    
    /** Creates a new instance of ProcessTableModel */
    public ProcessTableModel() {
        super();
    }
    
    
    /**
     * 
     * @param po 
     */
    public void addLine(ProcessObject po){
        
        ProcessCell pc1 = new ProcessCell(po,ProcessCell.TYPE.PROGRESSBAR);
        ProcessCell pc2 = new ProcessCell(po,ProcessCell.TYPE.INBUTTON);
        ProcessCell pc3 = new ProcessCell(po,ProcessCell.TYPE.ERRORBUTTON);
        
        ArrayList<ProcessCell> ar = new ArrayList<ProcessCell>();
        ar.add(pc1);
        ar.add(pc2);
        ar.add(pc3);
        data.add(ar);
        
        fireTableRowsInserted(data.size()-1,data.size());
    }
    
    /**
     * 
     * @param row 
     * @param col 
     */
    public void fireCellUpdated(int row,int col){
        //System.out.println(row +"  " +col);
        fireTableCellUpdated(row,col);
    }
    
    /**
     * 
     * @param row 
     */
    public void fireRowUpdated(int row){
        fireTableRowsUpdated(row,row+1);
    }
    
    
    /**
     * 
     */
    public void empty(){
        data = new ArrayList<ArrayList<ProcessCell>>();
        fireTableDataChanged();
    }
    
       
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public int getRowCount() {
        return data.size();
    }
        
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }
    
    public Class<?> getColumnClass(int c) {
        return columnClass[c];
    }
    
    public boolean isCellEditable(int row, int col) {
        return true;
    }
    
     
}
