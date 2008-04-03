/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

package org.puzzle.puzzlecore.struct;

import java.util.EventListener;

/**
 * Listener for Application
 * 
 * @author johann sorel
 */
public interface ContextListener extends EventListener{
        
    /**
     * When a Context is added
     * 
     * @param event the event
     */
    public void contextAdded(ContextEvent event) ;
      
    /**
     * When a Context is removed
     * 
     * @param event the event
     */
    public void contextRemoved(ContextEvent event);
      
    /**
     * When a Context is activated
     * 
     * @param event the event
     */
    public void contextActivated(ContextEvent event);
      
    /**
     * When a Context moved
     * 
     * @param event the event
     */
    public void contextMoved(ContextEvent event);
        
}
