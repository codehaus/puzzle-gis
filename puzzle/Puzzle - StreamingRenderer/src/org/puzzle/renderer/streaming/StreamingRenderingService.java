/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.streaming;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gui.swing.map.map2d.stream.JStreamEditMap;
import org.geotools.map.MapContext;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.view.MapView;
import org.puzzle.puzzlecore.view.RenderingService;

/**
 *
 * @author Johann Sorel
 */
public class StreamingRenderingService implements RenderingService{

    private static final String IMAGE_ICON_BASE = "org/puzzle/renderer/streaming/streaming.png";
    private static final String TITLE = "Streaming renderer";
    
    public MapView createView(MapContext context) {
        
        JStreamEditMap streamMap = new JStreamEditMap();
        streamMap.getRenderingStrategy().setContext(context);
        try {
            streamMap.getRenderingStrategy().setMapArea(context.getLayerBounds());
        } catch (IOException ex) {
            Logger.getLogger(StreamingRenderingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        StreamingMapView view = new StreamingMapView(streamMap);
        view.setDisplayName(context.getTitle() + " - Streaming");
        return view;
    }

    public Image getIcon() {
        return Utilities.loadImage(IMAGE_ICON_BASE);
    }

    public String getTitle() {
        return TITLE;
    }

}
