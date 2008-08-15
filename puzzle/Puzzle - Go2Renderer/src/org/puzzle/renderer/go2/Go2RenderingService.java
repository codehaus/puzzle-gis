/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.go2;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.geotools.gui.swing.map.map2d.stream.JStreamEditMap;
import org.geotools.gui.swing.go.J2DMap;
import org.geotools.map.MapContext;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.view.MapView;
import org.puzzle.puzzlecore.view.RenderingService;

/**
 *
 * @author Johann Sorel
 */
public class Go2RenderingService implements RenderingService{

    private static final String IMAGE_ICON_BASE = "org/puzzle/renderer/go2/go2.png";
    private static final String TITLE = "Go2 renderer";
    
    @Override
    public MapView createView(MapContext context) {
        
        J2DMap map = new J2DMap();
        map.getRenderer().setContext(context);
        
        Go2MapView view = new Go2MapView(map);
        view.setDisplayName(context.getTitle() + " - Go2");
        return view;
    }

    @Override
    public Image getIcon() {
        return Utilities.loadImage(IMAGE_ICON_BASE);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

}
