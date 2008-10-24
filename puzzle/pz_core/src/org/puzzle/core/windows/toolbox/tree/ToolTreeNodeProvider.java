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
package org.puzzle.core.windows.toolbox.tree;

import java.awt.Font;

import java.awt.Image;
import java.beans.BeanInfo;
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.gui.swing.icon.IconBundle;

import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;

import org.puzzle.core.tool.ToolDescriptor;

/**
 * Provider for ContextTree to render TreeColumn
 * 
 * @author Johann Sorel (Puzzle-GIS)
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
        final Object node = arg0.getValue();
        if (node instanceof ToolTreeNode) {
            final ToolTreeNode nod = (ToolTreeNode) node;
            final ToolDescriptor tool = ((WeakReference<ToolDescriptor>) nod.getUserObject()).get();

            if (tool != null) {
                rendererComponent.setFont(new Font("Arial", Font.PLAIN, 9));

                Image img = tool.getIcon(BeanInfo.ICON_COLOR_16x16);
                if (img == null) {
                    rendererComponent.setIcon(ICON_TOOL);
                } else {
                    rendererComponent.setIcon(new ImageIcon(img));
                }
                rendererComponent.setText(tool.getTitle());
            }else{
                rendererComponent.setText("no tool");
            }
        } else if (node instanceof ToolPackTreeNode) {
            final ToolPackTreeNode nod = (ToolPackTreeNode) node;
            rendererComponent.setFont(new Font("Arial", Font.PLAIN, 9));
            rendererComponent.setIcon(ICON_TOOLPACK);
            rendererComponent.setText(nod.getTitle());
        } else {
            rendererComponent.setIcon(IconBundle.EMPTY_ICON);
            rendererComponent.setText("unknown");
        }

    }
}
