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

import java.util.EventObject;

import org.geotools.map.MapContext;

/**
 * Event generated by Application
 * @author johann sorel
 * 
 */
public class ContextEvent extends EventObject{
    
    private final MapContext mapcontext;
    private final int begin;
    private final int end;
    
    
    /**
     * constructor
     * @param appli the AlterSIG application
     * @param mapcontext the mapcontext concerne
     * @param position the position of the mapcontext
     */
    ContextEvent(ContextManager appli, MapContext mapcontext, int position) {
        this(appli,mapcontext,position,position);
    }
    
    /**
     * constructor
     * @param appli the AlterSIG application
     * @param mapcontext the mapcontext concerne
     * @param begin start position of the mapcontext
     * @param end end position of the mapcontext
     */
    ContextEvent(ContextManager appli, MapContext mapcontext, int begin, int end) {
        super(appli);
        this.mapcontext = mapcontext;
        this.begin = begin;
        this.end = end;
    }
    
    /**
     * return the Mapcontext affected by the event
     * @return concern Mapcontext
     */
    public MapContext getMapContext() {
        return this.mapcontext;
    }
    
    /**
     * return the start index for contextmoved event
     * or the index for other events
     * @return start index for contextmoved event
     * or the index for other events
     */
    public int getFromIndex() {
        return begin;
    }
    
    /**
     * return the target index for contextmoved event
     * or the index for other events
     * @return index for contextmoved event
     * or the index for other events
     */
    public int getToIndex() {
        return end;
    }
    
    
    
}
