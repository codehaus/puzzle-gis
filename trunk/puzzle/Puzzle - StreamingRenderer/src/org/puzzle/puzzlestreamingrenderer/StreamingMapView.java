/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlestreamingrenderer;

import java.awt.BorderLayout;
import org.geotools.gui.swing.map.map2d.stream.StreamingMap2D;
import org.geotools.gui.swing.map.map2d.stream.control.JStreamNavigationBar;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author Johann Sorel
 */
public class StreamingMapView extends MapView{

    private final JStreamNavigationBar bar = new JStreamNavigationBar();
    
    public StreamingMapView(StreamingMap2D map){
        super(map);
        bar.setMap(map);
        bar.setFloatable(false);
        add(BorderLayout.NORTH,bar);
    }
    
    
}
