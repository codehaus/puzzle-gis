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
package org.puzzle.core.windows.toolbox;

import java.awt.Component;
import java.util.Collection;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import org.puzzle.core.resources.MessageBundle;
import org.puzzle.core.windows.toolbox.tree.ToolTreeListener;
import org.puzzle.core.tool.ToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.Tree;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public class BindToolTree extends Tree implements LookupListener {

    private final Lookup.Result<ToolDescriptor> result;

    public BindToolTree() {
        result = Lookup.getDefault().lookupResult(ToolDescriptor.class);
        result.addLookupListener(this);
        reload(null);

        addToolTreeListener(new ToolTreeListener() {

            @Override
            public void treeToolActivated(final ToolDescriptor tool) {

                new Thread(){

                    @Override
                    public void run() {
                        final Object close = MessageBundle.getString("close");
                        final Object[] objs = new Object[]{close};

                        Component comp = tool.getComponent();
                        if(comp != null){
                            final DialogDescriptor desc = new DialogDescriptor(tool.getComponent(), tool.getTitle(), false, objs, close,DialogDescriptor.DEFAULT_ALIGN,null, null);
                            desc.setClosingOptions(objs);
                            DialogDisplayer.getDefault().notify(desc);
                        }
                    }

                }.start();
                
            }
        });
    }

    
    
    private void reload(LookupEvent lookupEvent) {
        final ToolDescriptor[] contexts = getTreeToolDescriptors();
        final Collection<? extends ToolDescriptor> c;

        if (lookupEvent == null) {
            c = result.allInstances();
        } else {
            final Lookup.Result<? extends ToolDescriptor> r = (Lookup.Result<? extends ToolDescriptor>) lookupEvent.getSource();
            c = r.allInstances();
        }

        //remove tools non in the lookup
        for (final ToolDescriptor tool : contexts) {
            if (!c.contains(tool)) {
                removeWeakTools();
            }
        }

        //add new tools
        for(final ToolDescriptor tool : c){
            addTool(tool);
        }
    }

    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        reload(lookupEvent);
    }

}
