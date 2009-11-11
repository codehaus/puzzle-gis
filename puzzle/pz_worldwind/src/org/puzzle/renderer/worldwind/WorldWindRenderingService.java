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

import java.awt.Image;
import javax.swing.SwingUtilities;

import org.geotoolkit.map.MapContext;

import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

import org.puzzle.core.project.view.GISView;
import org.puzzle.core.view.ViewComponent;
import org.puzzle.core.view.RenderingService;

/**
 * Rendering service for the Go2 engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WorldWindRenderingService implements RenderingService{

    private static final String ID = "WorldWind";
    private static final String MATRIX_PARAMETER = "Matrix";
    private static final String CRS_PARAMETER = "Crs";

    public WorldWindRenderingService(){
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/puzzle/renderer/worldwind/resources/worldwind.png");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(WorldWindRenderingService.class, "title");
    }

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public ViewComponent restoreView(MapContext context, final GISView view) {
        final WorldWindMapView viewComponent = new WorldWindMapView(context);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                viewComponent.setDisplayName(view.getTitle());
            }
        });
        return viewComponent;
    }

}
