/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gui;

import java.beans.PropertyChangeListener;
import org.puzzle.puzzlecore.tool.ToolDescriptor;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 *
 * @author johann sorel
 */
public interface ShellBean extends ToolDescriptor{

    
    public static final String PROP_COMMAND = "command";
    
    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
    
    public String getCommand();
    
    public void setCommand(String command);
    
    public ProcessObject createProcess();
    
}
