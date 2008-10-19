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
package org.puzzle.core.tool.gui.toolbox;

import java.util.Collection;
import java.util.Iterator;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.puzzle.core.windows.toolbox.tree.JToolTree;
import org.puzzle.core.windows.toolbox.tree.ToolTreeListener;
import org.puzzle.core.tool.ToolDescriptor;
import org.puzzle.core.tool.ToolService;

/**
 * @author johann sorel
 */
public class BindToolTree extends JToolTree implements LookupListener {

    private ToolService service = Lookup.getDefault().lookup(ToolService.class);
    private Lookup.Result<ToolDescriptor> result = null;

    public BindToolTree() {

        if (service != null) {
            result = service.getLookup().lookupResult(ToolDescriptor.class);
            result.addLookupListener(this);
            reload(null);
        }

        addToolTreeListener(new ToolTreeListener() {

            @Override
            public void treeToolActivated(ToolDescriptor tool) {
                
                Object close = "Close";
                Object[] objs = new Object[]{close};
                
                DialogDescriptor desc = new DialogDescriptor(tool.getComponent(), tool.getTitle(), false, objs, close,DialogDescriptor.DEFAULT_ALIGN,null, null);
                desc.setClosingOptions(objs);
                DialogDisplayer.getDefault().notify(desc);
                
            }
        });
    }

    
    
    private void reload(LookupEvent lookupEvent) {
        ToolDescriptor[] contexts = getTreeToolDescriptors();

        Collection<? extends ToolDescriptor> c = null;

        if (lookupEvent == null) {
            c = result.allInstances();
        } else {
            Lookup.Result<? extends ToolDescriptor> r = (Lookup.Result<? extends ToolDescriptor>) lookupEvent.getSource();
            c = r.allInstances();
        }

        //remove tools non in the lookup
        for (ToolDescriptor tool : contexts) {
            if (!c.contains(tool)) {
                treetable.removeWeakTools();
            }
        }

        //add new tools
        Iterator<? extends ToolDescriptor> ite = c.iterator();
        while (ite.hasNext()) {
            ToolDescriptor tool = ite.next();
            addTool(tool);
        }
    }

    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        reload(lookupEvent);
    }

}
