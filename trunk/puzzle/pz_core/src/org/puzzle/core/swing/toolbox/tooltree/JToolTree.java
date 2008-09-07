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

package org.puzzle.core.swing.toolbox.tooltree;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.puzzle.core.tool.ToolDescriptor;


/**
 * 
 * @author Johann Sorel
 */
public class JToolTree extends JComponent{

    
    protected final TreeTable treetable;
    
    public JToolTree(){
        
        treetable = new TreeTable(this);
        
        //the tree view
        JScrollPane pane = new JScrollPane(treetable);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,pane);    
        
    }
    
    public void addTool(ToolDescriptor tool){
        treetable.addTool(tool);
    }
    
    public void removeTool(ToolDescriptor tool){
        treetable.removeTool(tool);
    }
    
    public ToolDescriptor[] getTreeToolDescriptors(){
        return treetable.getTreeToolDescriptors();
    }
    
    /**
     * add ToolTreeListener
     * @param listener
     */
    public void addToolTreeListener(ToolTreeListener listener) {
        treetable.addToolTreeListener(listener);
    }

    /**
     * remove ToolTreeListener
     * @param listener to remove
     */
    public void removeToolTreeListener(ToolTreeListener listener) {
        treetable.removeToolTreeListener(listener);
    }

    /**
     * get ToolTreeListener list
     * @return the listener's table
     */
    public ToolTreeListener[] getToolTreeListeners() {
        return treetable.getToolTreeListeners();
    }
    
    
}
