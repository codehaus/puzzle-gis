/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.context;

import org.geotools.map.MapContext;
import org.openide.util.Lookup;

/**
 *
 * @author Administrateur
 */
public interface ContextService extends Lookup.Provider{

    void addContext(MapContext context);

    void removeContext(MapContext context);
    
    MapContext getActiveContext();
    
    void setActiveContext(MapContext context);
    
    /**
     * add ContextListener to Model
     * @param ker the new listener
     */
    void addContextListener(ContextListener ker);

    /**
     * remove ContextListener from Model
     * @param ker the listner to remove
     */
    void removeContextListener(ContextListener ker);

    /**
     * get ContextListeners list
     * @return the listener's table
     */
    public ContextListener[] getContextListeners();
    
}
