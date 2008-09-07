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

package org.puzzle.core.swing.toolbox.widgettool;

import javax.swing.JComponent;

/**
 * A widget Tool is a tool wich has a JComponent to configure it
 * widget tool are top level tools because they offer an interface for the user
 * @author johann sorel
 */
public interface WidgetTool {

    public final WidgetToolListener[] EMPTY_LISTENER_ARRAY = {};
    
    
    /**
     * get the configuration component/panel of the tool
     * @return JComponent
     */
    public JComponent getComponent();
    
    
    /**
     * add WidgetToolListener
     * @param listener
     */
    public void addWidgetToolListener(WidgetToolListener listener);

    /**
     * remove WidgetToolListener
     * @param listener to remove
     */
    public void removeWidgetToolListener(WidgetToolListener listener);

    /**
     * get WidgetToolListener list
     * @return the listener's table
     */
    public WidgetToolListener[] getWidgetToolListeners();
    
}
