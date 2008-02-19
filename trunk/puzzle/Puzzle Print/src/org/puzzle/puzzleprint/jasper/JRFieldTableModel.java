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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.table.AbstractTableModel;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import org.puzzle.puzzlemap2d.struct.Map2DManager;

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
//            if(parameters[rowIndex].getValueClass().equals(Image.class)){
            
            
            
            BufferedImage image = Map2DManager.getInstance().getMap2D().getRenderingStrategy().getBufferImage();
            
            BufferedImage bgImage = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics g = bgImage.getGraphics();
//            g.fillRect(0, 0, bgImage.getWidth(), bgImage.getHeight());
            g.drawImage(image, 0, 0, Color.WHITE, null);
            
                return bgImage;
//            }
            
//            return values[rowIndex];
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        values[rowIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }


}
