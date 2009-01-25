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

import java.awt.Image;

import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.map.MapContext;

import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

import org.puzzle.core.view.MapView;
import org.puzzle.core.view.RenderingService;

/**
 * Rendering service for the Go2 engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2RenderingService implements RenderingService{

    public Go2RenderingService(){
    }

    @Override
    public MapView createView(MapContext context) {
        
        J2DMapVolatile map = new J2DMapVolatile();
        map.getRenderer().setContext(context);
        
        Go2MapView view = new Go2MapView(map);
        view.setDisplayName(context.getDescription().getTitle().toString() + " - Go2");
        return view;
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/puzzle/renderer/go2/go2.png");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(Go2RenderingService.class, "title");
    }

}
