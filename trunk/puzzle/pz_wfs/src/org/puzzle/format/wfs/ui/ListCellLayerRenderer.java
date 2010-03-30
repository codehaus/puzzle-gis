/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.puzzle.format.wfs.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import org.geotoolkit.wms.xml.v111.Layer;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ListCellLayerRenderer extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList list, Object obj, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, obj, index, isSelected, cellHasFocus);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(this.isOpaque());
        panel.setBackground(this.getBackground());
        panel.setForeground(this.getForeground());
        panel.setBorder(this.getBorder());

        final JLabel lbl = new JLabel();
        lbl.setOpaque(false);
        final JTextPane area = new JTextPane();
        area.setOpaque(false);
        area.setEditorKit(new HTMLEditorKit());

        final String name;
        final String title;
        final String desc;

        if(obj instanceof Layer){
            Layer layer = (Layer) obj;
            name = layer.getName();
            title = layer.getTitle();
            desc = layer.getAbstract();
        }else if(obj instanceof org.geotoolkit.wms.xml.v130.Layer){
            org.geotoolkit.wms.xml.v130.Layer layer = (org.geotoolkit.wms.xml.v130.Layer)obj;
            name = layer.getName();
            title = layer.getTitle();
            desc = layer.getAbstract();
        }else{
            name = "";
            title = "";
            desc = "";
        }

        lbl.setText("<html><b>"+title+"</b> ("+name+")</html>");
        area.setText("<html><i>"+ ((desc!= null)?desc:"  - no description - ") +"</i></html>");

        panel.add(BorderLayout.NORTH,lbl);
        panel.add(BorderLayout.CENTER,area);
        return panel;
    }

}
