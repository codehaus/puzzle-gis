/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.sldeditor;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.puzzle.puzzlecore.tool.ToolDescriptor;
import org.puzzle.puzzlecore.tool.ToolService;
import org.puzzle.sldeditor.gui.SLDEditorDescriptor;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

  private final Collection<ToolDescriptor> DEFAULT_TOOLS = new ArrayList<ToolDescriptor>();
  
  @Override
  public void restored() {
    
        DEFAULT_TOOLS.add(new SLDEditorDescriptor());
        
        //grab the tool service
        ToolService service = Lookup.getDefault().lookup(ToolService.class);
                  
        for (ToolDescriptor tool : DEFAULT_TOOLS) {
            service.addTool(tool);
        }
        
    }
}
