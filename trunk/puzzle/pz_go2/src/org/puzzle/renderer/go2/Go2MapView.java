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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.gui.swing.go.control.JConfigBar;
import org.geotools.gui.swing.go.control.JCoordinateBar;
import org.geotools.gui.swing.go.control.JInformationBar;
import org.geotools.gui.swing.go.control.JNavigationBar;
import org.geotools.gui.swing.go.decoration.JClassicNavigationDecoration;
import org.geotools.map.MapContext;
import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO2 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2MapView extends ViewComponent{

    private final JNavigationBar navBar = new JNavigationBar();
    private final JInformationBar infoBar = new JInformationBar();
    private final JCoordinateBar coordBar = new JCoordinateBar();
    private final JConfigBar configBar = new JConfigBar();
    private final JClassicNavigationDecoration boussole = new JClassicNavigationDecoration();

    
    public Go2MapView(J2DMapVolatile map){
        super(map);
        navBar.setMap(map);
        infoBar.setMap(map);
        coordBar.setMap(map);
        configBar.setMap(map);


        JPanel north = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.0;
        north.add(navBar,constraints);
        
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        north.add(infoBar,constraints);
        
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        north.add(configBar,constraints);
        
        navBar.setFloatable(false);
        infoBar.setFloatable(false);
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
        return getMap().getContainer().getContext();
    }
    
    
}
