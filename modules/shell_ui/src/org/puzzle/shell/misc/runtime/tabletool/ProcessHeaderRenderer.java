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

package org.puzzle.shell.misc.runtime.tabletool;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * �l�ment pour ProcessObjectPanel
 * @author johann sorel
 */
public class ProcessHeaderRenderer implements TableCellRenderer{
    
    /** Creates a new instance of ProcessHeaderRenderer */
    public ProcessHeaderRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if(value.getClass().equals(JLabel.class)){
            JLabel lbl = (JLabel)value;
            lbl.setFont(new Font("Arial",Font.BOLD,10));
            lbl.setBorder(BorderFactory.createEtchedBorder());
            return lbl;
        }
        else{
            JLabel lbl = new JLabel(value.toString(),JLabel.CENTER);
            lbl.setFont(new Font("Arial",Font.BOLD,10));
            lbl.setBorder(BorderFactory.createEtchedBorder());
            return lbl;
        }
        
    }
    
}
