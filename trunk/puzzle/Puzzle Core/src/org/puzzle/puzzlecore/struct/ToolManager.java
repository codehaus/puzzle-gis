/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

package org.puzzle.puzzlecore.struct;


import java.util.HashMap;
import javax.swing.JDialog;
import org.geotools.gui.swing.toolbox.tooltree.JToolTree;
import org.geotools.gui.swing.toolbox.tooltree.ToolTreeListener;
import org.geotools.gui.swing.toolbox.widgettool.WidgetToolDescriptor;
import org.geotools.gui.swing.toolbox.widgettool.clipping.ClippingTTDescriptor;
import org.geotools.gui.swing.toolbox.widgettool.shapecreation.ShapeCreationTTDescriptor;
import org.geotools.gui.swing.toolbox.widgettool.svg2mif.SVG2MIFTTDescriptor;
import org.geotools.gui.swing.toolbox.widgettool.vdem2csv.VDem2CSVTTDescriptor;
import org.puzzle.puzzlecore.gtextend.widget.sldeditor.SLDEditorDescriptor;

/**
 *
 * @author Johann Sorel
 */
public final class ToolManager {

    private static ToolManager instance;
    private JToolTree tree = null;
    private ToolTreeListener toolTreeListener = null;

    private ToolManager() {
    }

    public JToolTree getToolTree() {

        if (tree == null) {
            tree = new JToolTree();

            tree.addTool(new SVG2MIFTTDescriptor());
            tree.addTool(new ShapeCreationTTDescriptor());
            tree.addTool(new VDem2CSVTTDescriptor());
            tree.addTool(new ClippingTTDescriptor());
            tree.addTool(new SLDEditorDescriptor());
            
            toolTreeListener = new ToolTreeListener() {

                public void treeToolActivated(WidgetToolDescriptor tool) {
                    JDialog dialog = new JDialog();
                    dialog.setTitle(tool.getTitle());
                    dialog.setContentPane(tool.createTool(new HashMap()).getComponent());
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setModal(false);
                    dialog.setVisible(true);
                }
            };

            tree.addToolTreeListener(toolTreeListener);
        }

        return tree;
    }

    static ToolManager getInstance() {
        if (instance == null) {
            instance = new ToolManager();
        }
        return instance;
    }
}
