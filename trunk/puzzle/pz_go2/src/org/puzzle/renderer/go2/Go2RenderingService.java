/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.go2;

import java.awt.Image;
import java.io.IOException;
import org.geotools.gui.swing.go.J2DMapVolatile;
import org.geotools.map.MapContext;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.RenderingService;

/**
 *
 * @author Johann Sorel
 */
public class Go2RenderingService implements RenderingService{

    private static final String IMAGE_ICON_BASE = "org/puzzle/renderer/go2/go2.png";
    private static final String TITLE = "Go2 renderer";
    
    @Override
    public MapView createView(MapContext context) {
        
        J2DMapVolatile map = new J2DMapVolatile();
        try {
            map.getRenderer().setContext(context);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        Go2MapView view = new Go2MapView(map);
        view.setDisplayName(context.getDescription().getTitle().toString() + " - Go2");
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
