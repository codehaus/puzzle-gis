/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlestreamingrenderer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gui.swing.map.map2d.stream.JStreamEditMap;
import org.geotools.map.MapContext;
import org.puzzle.puzzlecore.view.MapView;
import org.puzzle.puzzlecore.view.RenderingService;

/**
 *
 * @author Johann Sorel
 */
public class StreamingRenderingService implements RenderingService{

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

}
