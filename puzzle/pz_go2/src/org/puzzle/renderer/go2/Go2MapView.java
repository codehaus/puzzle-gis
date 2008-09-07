/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.go2;

import java.awt.BorderLayout;
import org.geotools.gui.swing.go.GoMap2D;
import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.gui.swing.go.control.JCoordinateBar;
import org.geotools.gui.swing.go.control.JNavigationBar;
import org.geotools.gui.swing.go.decoration.JNavigationDecoration;
import org.geotools.gui.swing.map.map2d.Map2D;
import org.geotools.map.MapContext;
import org.puzzle.core.view.MapView;

/**
 *
 * @author Johann Sorel
 */
public class Go2MapView extends MapView{

    private final JNavigationBar navBar = new JNavigationBar();
    private final JCoordinateBar coordBar = new JCoordinateBar();
    private final JNavigationDecoration boussole = new JNavigationDecoration();
    
    public Go2MapView(J2DMapVolatile map){
        super(map);
        navBar.setMap(map);
        coordBar.setMap(map);
        navBar.setFloatable(false);
        add(BorderLayout.NORTH,navBar);
        add(BorderLayout.SOUTH,coordBar);

        map.addDecoration(boussole);

    }

    @Override
    public J2DMapVolatile getMap() {
        return (J2DMapVolatile)super.getMap();
    }

    
    
    @Override
    public MapContext getContext() {
        return getMap().getRenderer().getContext();
    }
    
    
}
