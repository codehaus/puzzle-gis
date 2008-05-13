/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.view.action;

import org.geotools.gui.swing.map.map2d.Map2D;

/**
 *
 * @author johann sorel
 */
public interface MapAction {

    Map2D getMap();

    void setMap(Map2D map);
}
