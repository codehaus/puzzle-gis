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
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * 
 * @author johann Sorel
 */
public class ProcessCell {
    
    
    /**
     * 
     */
    public static enum TYPE{
        /**
         *
         */
        PROGRESSBAR,
        /**
         *
         */
        INBUTTON,
        /**
         *
         */
        ERRORBUTTON
    };
    
    private TYPE  type = null;
    private JProgressBar jpb = null;
    private JLabel state = null;
    private ProcessObject process = null;
    private int nombre = 0;
    private Thread chronos ;
    
    /** Creates a new instance of ProcessCell 
     * @param po 
     * @param type 
     */
    public ProcessCell(ProcessObject po, TYPE type) {
        process = po;
        this.type = type;
        
        
        switch (type){
            case PROGRESSBAR:
                jpb = new JProgressBar(0,100);
                jpb.setStringPainted(true);
                jpb.setBorderPainted(false);
                break;
            case INBUTTON :
                state = new JLabel();
                state.setHorizontalAlignment(JLabel.CENTER);
                state.setOpaque(true);
                state.setBackground(new Color(255,247,153));
                break;
            case ERRORBUTTON:
                state = new JLabel( );
                state.setHorizontalAlignment(JLabel.CENTER);
                state.setOpaque(true);
                state.setBackground(new Color(255,175,175));
                break;
        };
        
    }
    
    /**
     * 
     * @return 
     */
    public ProcessObject getProcess(){
        return process;
    }
    
    /**
     * 
     * @return 
     */
    public Component getComponent(){
        
        switch (type){
            
            case PROGRESSBAR:
                jpb.setString(process.getString());
                
                switch(process.getStatus()){
                    case WAITING :
                        jpb.setValue(0);
                        break;
                    case WORKING :
                        nombre = (nombre<100) ? nombre+2 : 0 ;
                       jpb.setValue(nombre);
                        break;
                    case FINISHED :
                        jpb.setValue(100);
                        break;
                    case ALERTS :
                        jpb.setValue(100);
                        break;
                    case ERRORS :
                        jpb.setValue(100);
                        break;
                };
                
                return jpb;
                
            case INBUTTON :
                state.setText(String.valueOf(process.getInList().size()));
                break;
                
            case ERRORBUTTON :
                state.setText(String.valueOf(process.getErrorList().size()));
                break;
        };
        
        return state;
        
    }
}
