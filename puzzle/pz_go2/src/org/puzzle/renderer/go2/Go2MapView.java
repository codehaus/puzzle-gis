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
package org.puzzle.renderer.go2;

import java.awt.BorderLayout;
import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.gui.swing.go.control.JCoordinateBar;
import org.geotools.gui.swing.go.control.JNavigationBar;
import org.geotools.gui.swing.go.decoration.JNavigationDecoration;
import org.geotools.map.MapContext;
import org.puzzle.core.view.MapView;

/**
 * Map view using the GO2 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
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
