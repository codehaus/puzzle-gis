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
package org.puzzle.core.context;

import javax.swing.event.EventListenerList;
import org.geotools.map.MapContext;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


/**
 *
 * @author Johann Sorel
 */
public class DefaultContextService implements ContextService{

    private final EventListenerList listeners = new EventListenerList();
    private final InstanceContent CONTENT = new InstanceContent();
    private final Lookup LOOKUP = new AbstractLookup(CONTENT);
    private MapContext active = null;
    
    public DefaultContextService() {        
    }
    
    public Lookup getLookup() {
        return LOOKUP;
    }    
    
    /**
     * add a new mapcontext
     * @param context
     */
    public void addContext(MapContext context) {        
        CONTENT.add(context);        
    }

    /**
     * remove a mapcontext
     * @param context
     */
    public void removeContext(MapContext context) {
        CONTENT.remove(context);
    }

    public MapContext getActiveContext() {
        return active;
    }
    
    public void setActiveContext(MapContext context){
        
        if(active != context){
            active = context;
            fireContextActivated(context);
        }
    }
    
    //----Listeners-------------------------------------------------------------
    /**
     * generate a Applicationevent for an activated mapcontext
     * @param mapcontext the activated mapcontext (null if none activated)
     * @param index the position of the activated context
     */
    private void fireContextActivated(MapContext mapcontext) {
        ContextEvent event = new ContextEvent(this, mapcontext);

        ContextListener[] list = getContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextActivated(event);
        }
    }
    

    public void addContextListener(ContextListener ker) {
        listeners.add(ContextListener.class, ker);
    }

    public void removeContextListener(ContextListener ker) {
        listeners.remove(ContextListener.class, ker);
    }

    public ContextListener[] getContextListeners() {
        return listeners.getListeners(ContextListener.class);
    }
    
}
