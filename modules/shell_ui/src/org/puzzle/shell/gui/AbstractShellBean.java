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
