/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.tool;

import org.openide.util.Lookup;

/**
 *
 * @author Administrateur
 */
public interface ToolService extends Lookup.Provider{

    void addTool(ToolDescriptor tool);

    void removeTool(ToolDescriptor tool);
}
