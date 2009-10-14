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
package org.puzzle.renderer.worldwind;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import org.geotoolkit.map.MapContext;
import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO3 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WorldWindMapView extends ViewComponent{

    private final MapContext context;

    public WorldWindMapView(MapContext context){
        super();
        this.context = context;
        setLayout(new BorderLayout());

        Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        WorldWindowGLCanvas canvas = new WorldWindowGLCanvas();
        canvas.setModel(model);

        add(BorderLayout.CENTER,canvas);

    }

    @Override
    public MapContext getContext() {
        return context;
    }

}
