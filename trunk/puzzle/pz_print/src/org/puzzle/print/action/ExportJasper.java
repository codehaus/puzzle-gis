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
package org.puzzle.print.action;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.view.ViewComponent;

/**
 * Save view action.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ExportJasper extends CookieAction{

    public ExportJasper() {}

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{GISView.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        if(activatedNodes.length == 0 ) return ;

        final GISView view = activatedNodes[0].getLookup().lookup(GISView.class);

        if(view == null) return;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ViewComponent viewcomp = view.getComponent(false);

                if(viewcomp != null){
                    Image img = viewcomp.getMap().getCanvas().getSnapShot();
                    final BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                    final Graphics g = bi.getGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();

                    JFileChooser chooser = new JFileChooser();
                    int choice = chooser.showSaveDialog(null);

                    if(choice == JFileChooser.APPROVE_OPTION){
                        try {
                            ImageIO.write(bi, "png", chooser.getSelectedFile());
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
         }
        });

    }

    @Override
    public String getName() {
        return Utilities.getString("exportJasper");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if(activatedNodes.length == 0 ) return false;
        final GISView view = activatedNodes[0].getLookup().lookup(GISView.class);
        if(view == null) return false;
        final ViewComponent viewcomp = view.getComponent(false);
        return viewcomp != null;
    }

}
