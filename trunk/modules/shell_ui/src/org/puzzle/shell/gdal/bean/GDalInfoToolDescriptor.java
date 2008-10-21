/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gdal.bean;

import org.puzzle.shell.gui.ShellToolPane;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class GDalInfoToolDescriptor extends ShellToolPane{

    public GDalInfoToolDescriptor(){
        setBean(new GdalInfo());
    }
}
