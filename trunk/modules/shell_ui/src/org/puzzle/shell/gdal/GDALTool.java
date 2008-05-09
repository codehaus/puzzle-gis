/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gdal;

import javax.swing.JComponent;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 *
 * @author johann sorel
 */
public interface GDALTool {

    public static final ProcessObject[] EMPTY_PROCESS_OBJECT_ARRAY = {};
    
    
    JComponent getComponent();
    
    ProcessObject[] createProcesses();
    
    
}
