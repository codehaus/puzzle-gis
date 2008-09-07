/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.core.view;

import org.openide.util.Lookup;

/**
 *
 * @author Administrateur
 */
public interface ViewService extends Lookup.Provider{

    void add(MapView view);

    void remove(MapView view);
    
    void add(MapGroup group);

    void remove(MapGroup group);
    
    
}
