/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.core.windows.mapdetail;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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

            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            final Object value = node.getUserObject();

            if(node.isLeaf()){
                PaneRenderer.this.setIcon(new ImageIcon(ImageUtilities.loadImage("org/puzzle/core/resources/bullet.png", true)));
            }else{
                PaneRenderer.this.setIcon(null);
            }

            if(value instanceof PropertyPane){
                final PropertyPane pane = (PropertyPane) value;

                final ImageIcon icon = pane.getIcon();
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
