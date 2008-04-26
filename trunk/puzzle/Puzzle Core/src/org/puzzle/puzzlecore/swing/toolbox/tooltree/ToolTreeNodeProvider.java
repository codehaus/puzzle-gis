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

package org.puzzle.puzzlecore.swing.toolbox.tooltree;


import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.gui.swing.toolbox.widgettool.WidgetToolDescriptor;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;


/**
 * Provider for ContextTree to render TreeColumn
 * 
 * @author johann sorel
 */
public final class ToolTreeNodeProvider extends ComponentProvider<JLabel> {
        
    private static final ImageIcon ICON_TOOL = IconBundle.getResource().getIcon("16_tool");
    private static final ImageIcon ICON_TOOLPACK = IconBundle.getResource().getIcon("16_toolpack");

    private final JToolTree tree;
    
    

    public ToolTreeNodeProvider(JToolTree tree) {
        this.tree = tree;
        rendererComponent = new JLabel();
    }
    

    @Override
    protected void configureState(CellContext arg0) {
    }
    

    @Override
    protected JLabel createRendererComponent() {        
        return new JLabel();
    }
    

    @Override
    protected void format(CellContext arg0) {
        Object node = arg0.getValue();
        if(node instanceof ToolTreeNode){
            ToolTreeNode nod  = (ToolTreeNode) node;
            WidgetToolDescriptor tool = (WidgetToolDescriptor)nod.getUserObject();            
            rendererComponent.setFont(new Font("Arial",Font.PLAIN,9));            
            rendererComponent.setIcon(ICON_TOOL);                        
            rendererComponent.setText( tool.getTitle() );
        }else if(node instanceof ToolPackTreeNode){
            ToolPackTreeNode nod  = (ToolPackTreeNode) node;
            rendererComponent.setFont(new Font("Arial",Font.PLAIN,9));            
            rendererComponent.setIcon(ICON_TOOLPACK);                        
            rendererComponent.setText( nod.getTitle() );
        }else {
            rendererComponent.setIcon(IconBundle.EMPTY_ICON); 
            rendererComponent.setText("unknown");
        }
        
    }
    
}
