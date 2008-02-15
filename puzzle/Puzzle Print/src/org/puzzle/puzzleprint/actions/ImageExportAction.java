/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.puzzleprint.actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlemap2d.struct.Map2DManager;

/**
 * 
 * @author johann sorel
 */
public final class ImageExportAction extends CallableSystemAction {

    public void performAction() {
    
        BufferedImage input = Map2DManager.getInstance().getMap2D().getRenderingStrategy().getBufferImage();
       
        if (input != null) {

            Date date = new Date();
            
            try {
                File outputFile = new File("image"+ date.getTime() +".png");
                ImageIO.write(input, "PNG", outputFile);                                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(ImageExportAction.class, "CTL_ImageExportAction");
    }

    @Override
    protected String iconResource() {
        return "org/puzzle/puzzleprint/actions/image.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
