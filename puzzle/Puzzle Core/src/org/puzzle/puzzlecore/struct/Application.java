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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.geotools.map.MapContext;

/**
 * @author johann sorel
 */
public final class Application {
    
    private final MapContext[] EMPTY_CONTEXT_ARRAY = new MapContext[0];
    private final EventListenerList listeners = new EventListenerList();
    private final List<MapContext> contexts = new ArrayList<MapContext>();
    private MapContext activeContext = null;
    private static Application instance = null;

    private Application() {
        
    }

    public static Application getInstance() {

        if (instance == null) {
            instance = new Application();
        }

        return instance;
    }

////////////////////////////////////////////////////////////////////////////////
// MAPCONTEXT MANAGEMENT ///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    /**
     * get the active context
     * @return return the active MapContext, if none return null
     */
    public MapContext getActiveContext() {
        return activeContext;
    }

    /**
     * active the context if mapcontext is in the mapcontext list
     * @param context the mapcontext to active
     */
    public void setActiveContext(MapContext context) {
        if (contexts.contains(context)  || context == null) {                  
            
            if(context != activeContext){
                activeContext = context;
                fireContextActivated(context, contexts.indexOf(context));
                }
        } else {
            throw new IllegalArgumentException();
        }
        
        
    }

    /**
     * add context to application if not allready in it
     * @param context the context to add
     */
    public void addContext(MapContext context) {
        
        if(!contexts.contains(context)){
            contexts.add(context);
            fireContextAdded(context, contexts.indexOf(context));
        }
    }

    /**
     * remove context 
     * @param context target mapcontext to remove
     */
    public void removeContext(MapContext context) {
                
        if(contexts.contains(context)){
            int position = contexts.indexOf(context);
            contexts.remove(context);
            fireContextRemoved(context, position);
        }
        
    }

    /**
     * count MapContext 
     * @return number of mapcontext in the tree
     */
    public int getContextCount() {
        return contexts.size();
    }

    /**
     * return context at index i
     * @param i position of the mapcontext
     * @return the mapcontext a position i
     */
    public MapContext getContext(int i) {
        return contexts.get(i);
    }

    /**
     * get the index of a mapcontext
     * @param context the mapcontext to find
     * @return index of context
     */
    public int getContextIndex(MapContext context) {
        return contexts.indexOf(context);
    }

    /**
     * MapContext Array
     * @return empty Array if no mapcontexts
     */
    public MapContext[] getContexts() {
        return contexts.toArray(EMPTY_CONTEXT_ARRAY);
    }

    /**
     * move a mapcontext in the list
     * @param context the context to move, must be in the list
     * @param new index of mapcontext
     */
    public void moveContext(MapContext context, int newplace) {
        if (contexts.contains(context) && newplace < contexts.size()) {

            int origine = contexts.indexOf(context);

            if (newplace != origine) {
                contexts.remove(context);
                contexts.add(newplace, context);
                fireContextMoved(context, origine, newplace);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

////////////////////////////////////////////////////////////////////////////////
// FIREEVENT AND LISTENERS /////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    /**
     * generate a Applicationevent for an added node
     * @param mapcontext the added mapcontext
     * @param position the position of the mapcontext in the tree
     */
    private void fireContextAdded(MapContext mapcontext, int position) {
        ApplicationContextEvent kevent = new ApplicationContextEvent(this, mapcontext, position);

        ApplicationContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextAdded(kevent);
        }
    }

    /**
     * generate a Applicationevent for a mapcontext removed
     * @param mapcontext the removed mapcontext
     * @param position the last position of the mapcontext
     */
    private void fireContextRemoved(MapContext mapcontext, int position) {
        ApplicationContextEvent event = new ApplicationContextEvent(this, mapcontext, position);

        ApplicationContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextRemoved(event);
        }
    }

    /**
     * generate a Applicationevent for an activated mapcontext
     * @param mapcontext the activated mapcontext (null if none activated)
     * @param index the position of the activated context
     */
    private void fireContextActivated(MapContext mapcontext, int index) {
        ApplicationContextEvent event = new ApplicationContextEvent(this, mapcontext, index);

        ApplicationContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextActivated(event);
        }
    }

    /**
     * generate a Applicationevent for a moving context
     * @param mapcontext the moving mapcontext
     * @param begin the start position of the mapcontext
     * @param end the end position of the mapcontext
     */
    private void fireContextMoved(MapContext mapcontext, int begin, int end) {
        ApplicationContextEvent event = new ApplicationContextEvent(this, mapcontext, begin, end);

        ApplicationContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextMoved(event);
        }
    }

    /**
     * add ApplicationListener to Model
     * @param ker the new listener
     */
    public void addApplicationContextListener(ApplicationContextListener ker) {
        listeners.add(ApplicationContextListener.class, ker);
    }

    /**
     * remove ApplicationListener from Model
     * @param ker the listner to remove
     */
    public void removeApplicationContextListener(ApplicationContextListener ker) {
        listeners.remove(ApplicationContextListener.class, ker);
    }

    /**
     * get ApplicationListeners list
     * @return the listener's table
     */
    public ApplicationContextListener[] getApplicationContextListeners() {
        return listeners.getListeners(ApplicationContextListener.class);
    }
}
