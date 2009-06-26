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

package org.puzzle.core.windows.mapdetail;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import org.geotoolkit.gui.swing.propertyedit.PropertyPane;
import org.jdesktop.swingx.JXTree;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JPropertyTree extends JXTree{

    public JPropertyTree() {
        setCellRenderer(new PaneRenderer());        
    }

    private class PaneRenderer extends DefaultTreeCellRenderer{

        @Override
        public Component getTreeCellRendererComponent(JTree arg0, Object obj, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
            super.getTreeCellRendererComponent(arg0, obj, arg2, arg3, arg4, arg5, arg6);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            Object value = node.getUserObject();

            if(node.isLeaf()){
                PaneRenderer.this.setIcon(new ImageIcon(ImageUtilities.loadImage("org/puzzle/core/windows/mapdetail/bullet.png", true)));
            }else{
                PaneRenderer.this.setIcon(null);
            }

            if(value instanceof PropertyPane){
                PropertyPane pane = (PropertyPane) value;

                ImageIcon icon = pane.getIcon();
                if(icon != null){
                    PaneRenderer.this.setIcon(icon);
                }
                PaneRenderer.this.setText(pane.getTitle());

            }else{
                PaneRenderer.this.setText("<html><b>"+getText()+"</b></html>");
            }

            return PaneRenderer.this;
        }

    }

}
