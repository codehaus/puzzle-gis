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

package org.puzzle.puzzlecore.swing.toolbox.widgettool;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * @author johann sorel
 */
public abstract class AbstractWidgetTool extends JPanel implements WidgetTool{

    protected final EventListenerList LISTENERS = new EventListenerList();

    public JComponent getComponent() {
        return this;
    }
    
    protected void fireObjectCreation(Object[] obj){
        WidgetToolListener[] listeners = getWidgetToolListeners();
        
        for(WidgetToolListener listener : listeners){
            listener.objectCreated(obj);
        }
    }
    
    /**
     * add TreeToolListener
     * @param listener
     */
    public void addWidgetToolListener(WidgetToolListener listener) {
        LISTENERS.add(WidgetToolListener.class,listener);
    }

    /**
     * remove TreeToolListener
     * @param listener to remove
     */
    public void removeWidgetToolListener(WidgetToolListener listener) {
        LISTENERS.remove(WidgetToolListener.class,listener);
    }

    /**
     * get TreeToolListener list
     * @return the listener's table
     */
    public WidgetToolListener[] getWidgetToolListeners() {
        return LISTENERS.getListeners(WidgetToolListener.class);
    }
    
    
    
}
