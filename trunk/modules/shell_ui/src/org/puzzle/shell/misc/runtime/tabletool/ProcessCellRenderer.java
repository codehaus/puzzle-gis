/*
 * ProcessCellRenderer.java
 *
 * Created on 10 f�vrier 2007, 15:55
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
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * �l�ment pour ProcessObjectPanel
 * @author johann sorel
 */
public class ProcessCellRenderer  implements TableCellRenderer{
    
    
  
    /**
     * 
     */
    public ProcessCellRenderer() {
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return ((ProcessCell)value).getComponent();
    }
    
    
    
}
