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

import java.awt.Color;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.openide.util.Utilities;
import org.puzzle.shell.misc.runtime.JRuntimePanel;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * 
 * @author johann sorel
 */
public class ProcessCellEditor extends AbstractCellEditor implements TableCellEditor{
    
    private final static ImageIcon ICON_WARNING = new ImageIcon(Utilities.loadImage("org/puzzle/shell/misc/runtime/warning.png"));
    private final static ImageIcon ICON_ERROR = new ImageIcon(Utilities.loadImage("org/puzzle/shell/misc/runtime/error.png"));
    private final static String WARNING = ResourceBundle.getBundle("org/puzzle/shell/misc/runtime/Bundle").getString("warning");
    private final static String ERROR = ResourceBundle.getBundle("org/puzzle/shell/misc/runtime/Bundle").getString("error");
    
    
    
    /**
     * 
     */
    public static int INBUTTON = 1;
    /**
     * 
     */
    public static int ERRORBUTTON = 2;
    
    private int type = 1;
    private JLabel state = null;
    
    private ProcessObject process = null;
    
    
    /** Creates a new instance of ProcessCellEditor 
     * @param type 
     */
    public ProcessCellEditor(int type) {
        this.type = type;
        
         if(type == 1){
            state = new JLabel( ICON_WARNING );
            state.setOpaque(true);
            state.setBackground(new Color(255,247,153));
        }
        else{
            state = new JLabel( ICON_ERROR );
            state.setOpaque(true);
            state.setBackground(new Color(255,175,175));
        }
    }
    
    public Object getCellEditorValue() {
        return new JLabel();
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         process = ((ProcessCell)value).getProcess();
        
        if(true){
            JDialog affichage = new JDialog();
            affichage.setAlwaysOnTop(true);
            affichage.setModal(false);
            affichage.setSize(450,250);
            affichage.setResizable(true);
            affichage.setLocationRelativeTo(null);
            
            if(type==1){
                affichage.setTitle(ERROR);
                JRuntimePanel jrp = new JRuntimePanel(process, JRuntimePanel.TYPE.LISTEN_IN);
                affichage.getContentPane().add(jrp);
            } else{
                affichage.setTitle(ERROR);
                JRuntimePanel jrp = new JRuntimePanel(process, JRuntimePanel.TYPE.LISTEN_ERROR);
                affichage.getContentPane().add(jrp);
            }
            
             affichage.setVisible(true);
            
        }
        /*if( true ){
            affichage.setVisible(true);
        }*/
        
        
        if(type == 1){
            state.setText(String.valueOf(process.getInList().size()));
            return state;
        }
        else{
            state.setText(String.valueOf(process.getErrorList().size()));
             return state;
        }
    }
    
    
    
}
