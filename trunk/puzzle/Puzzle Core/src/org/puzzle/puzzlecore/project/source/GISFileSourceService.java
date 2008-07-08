/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.project.source;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Administrateur
 */
public interface GISFileSourceService extends GISSourceService{

    FileFilter createFilter();
    
    GISSource createSource(File file) throws IllegalArgumentException;
    
}
