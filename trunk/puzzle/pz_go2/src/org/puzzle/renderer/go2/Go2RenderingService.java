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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.map.MapContext;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import org.openide.util.Utilities;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.RenderingService;

/**
 * Rendering service for the Go2 engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2RenderingService implements RenderingService{

    public Go2RenderingService(){
        File f = new File("/resources/boussole.svg");
        try {
//        URL url =Lookup.getDefault().lookup(ClassLoader.class).getResource("/org/puzzle/renderer/resources/boussole.svg");
//        URL url = Go2MapView.class.getResource("/org/puzzle/renderer/resources/boussole.svg");
            System.out.println("+++++++++++++++++++++++" + f.toURL());
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
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
