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
package org.puzzle.puzzlecore.tool.gui.toolbox;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.JToolTree;
import org.puzzle.puzzlecore.swing.toolbox.tooltree.ToolTreeListener;
import org.puzzle.puzzlecore.tool.ToolDescriptor;
import org.puzzle.puzzlecore.tool.ToolService;

/**
 * @author johann sorel
 */
public class BindToolTree extends JToolTree implements LookupListener {

    private ToolService service = Lookup.getDefault().lookup(ToolService.class);
    private Lookup.Result result = null;

    public BindToolTree() {

        if (service != null) {
            result = service.getLookup().lookupResult(ToolDescriptor.class);
            result.addLookupListener(this);
            reload(null);
        }

        addToolTreeListener(new ToolTreeListener() {

            public void treeToolActivated(ToolDescriptor tool) {
                JFrame dialog = new JFrame();
                dialog.setTitle(tool.getTitle());
                JPanel pane = new JPanel(new GridLayout(1, 1));
                pane.add(tool.getComponent());
                dialog.setContentPane(pane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
    }

    
    
    private void reload(LookupEvent lookupEvent) {
        ToolDescriptor[] contexts = getTreeToolDescriptors();

        Collection<ToolDescriptor> c = null;

        if (lookupEvent == null) {
            c = result.allInstances();
        } else {
            Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
            c = r.allInstances();
        }

        //remove tools non in the lookup
        for (ToolDescriptor tool : contexts) {
            if (!c.contains(tool)) {
                treetable.removeWeakTools();
            }
        }

        //add new tools
        Iterator<ToolDescriptor> ite = c.iterator();
        while (ite.hasNext()) {
            ToolDescriptor tool = ite.next();
            addTool(tool);
        }
    }

    public void resultChanged(LookupEvent lookupEvent) {
        reload(lookupEvent);
    }

}
