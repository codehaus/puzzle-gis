/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source;

import java.util.Map;
import org.geotools.gui.swing.datachooser.DataPanel;

/**
 *
 * @author Administrateur
 */
public interface GISDistantSourceService extends GISSourceService{

    DataPanel createDataPanel();
    
    GISSource createSource(Map parameters) throws IllegalArgumentException;
    
}
