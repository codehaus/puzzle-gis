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
import javax.swing.JPanel;
import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.gui.swing.go.control.JConfigBar;
import org.geotools.gui.swing.go.control.JCoordinateBar;
import org.geotools.gui.swing.go.control.JNavigationBar;
import org.geotools.gui.swing.go.decoration.JClassicNavigationDecoration;
import org.geotools.map.MapContext;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.puzzle.core.view.MapView;

/**
 * Map view using the GO2 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2MapView extends MapView{

    private final JNavigationBar navBar = new JNavigationBar();
    private final JCoordinateBar coordBar = new JCoordinateBar();
    private final JConfigBar configBar = new JConfigBar();
    private final JClassicNavigationDecoration boussole = new JClassicNavigationDecoration();

    
    public Go2MapView(J2DMapVolatile map){
        super(map);
        navBar.setMap(map);
        coordBar.setMap(map);
        configBar.setMap(map);
        
        JPanel north = new JPanel(new BorderLayout(0,0));
        north.add(BorderLayout.CENTER,navBar);
        north.add(BorderLayout.EAST,configBar);
        navBar.setFloatable(false);
        configBar.setFloatable(false);
        add(BorderLayout.NORTH,north);
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
