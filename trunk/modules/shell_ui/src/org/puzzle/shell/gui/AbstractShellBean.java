/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gui;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.puzzle.puzzlecore.tool.AbstractToolDescriptor;


/**
 *
 * @author johann sorel
 */
public abstract class AbstractShellBean extends AbstractToolDescriptor implements ShellBean{

    
    protected PropertyChangeSupport propertySupport;
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }
    
    
    protected abstract void parseCommand();
    
    public void setCommand(String command) {}
    
    public Component getComponent() {
        return null;
    }

}
