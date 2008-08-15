/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.go2;

import java.awt.BorderLayout;
import org.geotools.gui.swing.go.J2DMap;
import org.geotools.gui.swing.go.control.JCoordinateBar;
import org.geotools.gui.swing.go.control.JNavigationBar;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author Johann Sorel
 */
public class Go2MapView extends MapView{

    private final JNavigationBar navBar = new JNavigationBar();
    private final JCoordinateBar coordBar = new JCoordinateBar();
    
    public Go2MapView(J2DMap map){
        super(map);
        navBar.setMap(map);
        coordBar.setMap(map);
        navBar.setFloatable(false);
//        coordBar.setFloatable(false);
        add(BorderLayout.NORTH,navBar);
        add(BorderLayout.SOUTH,coordBar);
    }
    
    
}
