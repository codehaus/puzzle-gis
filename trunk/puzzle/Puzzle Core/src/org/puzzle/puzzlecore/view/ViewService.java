/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.view;

import org.openide.util.Lookup;

/**
 *
 * @author Administrateur
 */
public interface ViewService extends Lookup.Provider{

    void addView(MapView view);

    void removeView(MapView view);
}
