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
package org.puzzle.puzzlecore.context.action.addlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JDataChooser;
import org.geotools.gui.swing.datachooser.JFileDataPanel;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.puzzle.puzzlecore.context.ContextService;

/**
 * @author johann sorel
 */
public final class AddFileLayerAction extends CallableSystemAction {

  public void performAction() {

    JDialog dialog = new JDialog();


    JComponent glass = new JComponent() {

      @Override
      protected void paintComponent(Graphics g) {
        Rectangle clip = g.getClipBounds();
        Color alphaWhite = new Color(1.0f, 1.0f, 1.0f, 0.65f);
        g.setColor(alphaWhite);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        
//        g.setColor(Color.RED);
//        g.drawString("hahaha", 50, 50);

      }
    };


//    dialog.setGlassPane(glass);
//    dialog.getGlassPane().setVisible(true);
//
//    dialog.setSize(640, 480);
//    dialog.setLocationRelativeTo(null);
//    dialog.setVisible(true);

        ContextService service = Lookup.getDefault().lookup(ContextService.class);
        MapContext context = service.getActiveContext();
        
        if (context != null) {
            List<DataPanel> lst = new ArrayList<DataPanel>();
            lst.add(new JFileDataPanel());

            JDataChooser jdc = new JDataChooser(null, lst);

            JDataChooser.ACTION ret = jdc.showDialog();

            if (ret == JDataChooser.ACTION.APPROVE) {
                        
                final MapLayer[] layers = jdc.getLayers();

                for (MapLayer layer : layers) {
                    context.addLayer(layer);
                }
            } 

        }else {
                JOptionPane.showMessageDialog(null, "No active Context");
            }
  }

  public String getName() {
    return NbBundle.getMessage(AddFileLayerAction.class, "CTL_AddLayerAction");
  }

  @Override
  protected String iconResource() {
    return "org/puzzle/puzzlecore/context/action/addlayer/file_add.png";
  }

  public HelpCtx getHelpCtx() {
    return HelpCtx.DEFAULT_HELP;
  }

  @Override
  protected boolean asynchronous() {
    return false;
  }
}
