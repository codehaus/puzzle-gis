/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.format.shapefile;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.puzzle.format.shapefile.tool.ShapeCreationTTDescriptor;
import org.puzzle.puzzlecore.tool.ToolService;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // By default, do nothing.
        // Put your startup code here.
        
        ToolService service = Lookup.getDefault().lookup(ToolService.class);
        if(service != null){
            service.addTool(new ShapeCreationTTDescriptor());
        }
    }
}
