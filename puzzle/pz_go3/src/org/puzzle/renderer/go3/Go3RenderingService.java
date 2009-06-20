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

import java.awt.Image;

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
public class Go3RenderingService implements RenderingService{

    private static final String ID = "GO3";
    private static final String MATRIX_PARAMETER = "Matrix";
    private static final String CRS_PARAMETER = "Crs";

    public Go3RenderingService(){
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/puzzle/renderer/go3/go3.png");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(Go3RenderingService.class, "title");
    }

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public ViewComponent restoreView(MapContext context, final GISView view) {
        Go3MapView viewComponent = new Go3MapView(context);
        viewComponent.setDisplayName(view.getTitle());
        return viewComponent;
    }

}
