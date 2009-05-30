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
package org.puzzle.renderer.go3;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import org.geotoolkit.display3d.canvas.A3DCanvas;
import org.geotoolkit.gui.swing.go3.control.JNavigationBar;
import org.geotoolkit.gui.swing.go.J2DMapVolatile;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO2 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go3MapView extends ViewComponent{

    private final JNavigationBar guiNavBar = new JNavigationBar();
    private A3DCanvas gui3DPane;

    
    public Go3MapView(J2DMapVolatile map, MapContext context){
        super(map);

        setLayout(new BorderLayout());

        guiNavBar.setFloatable(false);
        add(BorderLayout.NORTH,guiNavBar);

        
        try{
            gui3DPane = new A3DCanvas(DefaultGeographicCRS.WGS84, null);
            guiNavBar.setMap(gui3DPane);
            gui3DPane.getController().setObjectiveCRS(context.getCoordinateReferenceSystem());
            gui3DPane.getController().setCameraSpeed(100);
            gui3DPane.getContainer2().setContext(context,false);
            add(BorderLayout.CENTER,gui3DPane.getComponent());
        }catch(Exception e){
            add(BorderLayout.CENTER,new JLabel(e.getLocalizedMessage()));
        }

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
