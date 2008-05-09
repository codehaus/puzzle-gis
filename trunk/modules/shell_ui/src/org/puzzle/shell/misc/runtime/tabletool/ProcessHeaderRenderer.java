/*
 * ProcessHeaderRenderer.java
 *
 * Created on 11 f�vrier 2007, 13:36
 *
 * Copyright 2004,2007 Johann Sorel
 *
 * JSL-ToolBox is free library; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JSL-ToolBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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
