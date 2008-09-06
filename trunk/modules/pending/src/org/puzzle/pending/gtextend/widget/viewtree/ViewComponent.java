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

package org.puzzle.pending.gtextend.widget.viewtree;

import org.puzzle.puzzlecore.gtextend.widget.viewtree.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.geotools.gui.swing.contexttree.renderer.RenderAndEditComponent;
import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.map.MapLayer;



/**
 *
 * @author johann sorel
 */
final class ViewComponent extends RenderAndEditComponent implements TableCellRenderer{

       
    private final VisbleCheck check = new VisbleCheck();
    private MapLayer layer = null;
    
    ViewComponent(){
        super();
        setLayout(new GridLayout(1,1));
        check.setOpaque(false);
        
        check.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(layer != null){
                    layer.setVisible(check.isSelected());
                }
            }
        });
        
    }
    
    
    @Override
    public void parse(Object obj) {
       layer = null;
        
       
        removeAll();
        if(obj instanceof Boolean){
            check.setSelected((Boolean)obj);
            add(check);
        }
        else if(obj instanceof MapLayer){
            check.setSelected(((MapLayer)obj).isVisible());
            layer = (MapLayer) obj;
            add(check);
        }
        
    }

    @Override
    public Object getValue() {
        return check.isSelected();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        parse(value);
        return this;
    }

   
    
}
class VisbleCheck extends JCheckBox{
    
    
    private static final ImageIcon ICON_LINK = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/link.png"));
    private static final ImageIcon ICON_UNLINK = new ImageIcon(JViewTree.class.getResource("/org/puzzle/puzzlecore/gtextend/widget/iconset/unlink.png"));
     
     @Override
        public void paintComponent(Graphics g){
         
            int x = (getWidth()-16)/2;
            int y = (getHeight()-16)/2;
            g.drawImage( (isSelected())?ICON_LINK.getImage() : ICON_UNLINK.getImage(), x,y, this);
        }
}