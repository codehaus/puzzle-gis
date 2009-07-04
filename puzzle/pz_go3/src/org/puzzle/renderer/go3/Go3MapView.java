/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.renderer.go3;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import org.geotoolkit.display3d.canvas.A3DCanvas;
import org.geotoolkit.gui.swing.go3.control.JNavigationBar;
import org.geotoolkit.map.MapContext;
import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO3 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go3MapView extends ViewComponent{

    private final JNavigationBar guiNavBar = new JNavigationBar();
    private A3DCanvas gui3DPane;

    
    public Go3MapView(MapContext context){
        super();

        setLayout(new BorderLayout());

        guiNavBar.setFloatable(false);
        add(BorderLayout.NORTH,guiNavBar);


        context.setCoordinateReferenceSystem(context.layers().get(context.layers().size()-1).getBounds().getCoordinateReferenceSystem());


        try{
//            context.setCoordinateReferenceSystem(CRS.decode("EPSG:27592"));
            gui3DPane = new A3DCanvas(context.getCoordinateReferenceSystem(), null);
            guiNavBar.setMap(gui3DPane);
            gui3DPane.getContainer2().setContext(context,false);
            gui3DPane.getController().setCameraSpeed(100);
            add(BorderLayout.CENTER,gui3DPane.getComponent());
        }catch(Exception e){
            add(BorderLayout.CENTER,new JLabel(e.getLocalizedMessage()));
        }

    }

    @Override
    public MapContext getContext() {
        return gui3DPane.getContainer2().getContext();
    }

}
