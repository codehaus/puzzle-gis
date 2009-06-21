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
package org.puzzle.renderer.go2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.geotoolkit.gui.swing.go2.JMap2D;
import org.geotoolkit.gui.swing.go2.control.JConfigBar;
import org.geotoolkit.gui.swing.go2.control.JCoordinateBar;
import org.geotoolkit.gui.swing.go2.control.JInformationBar;
import org.geotoolkit.gui.swing.go2.control.JNavigationBar;
import org.geotoolkit.gui.swing.go2.control.MapControlBar;
import org.geotoolkit.gui.swing.go2.decoration.MapDecoration;
import org.geotoolkit.map.MapContext;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.puzzle.core.view.ViewComponent;

/**
 * Map view using the GO2 Rendering engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2MapView extends ViewComponent{

    private final JNavigationBar navBar = new JNavigationBar();
    private final JInformationBar infoBar = new JInformationBar();
    private final JCoordinateBar coordBar = new JCoordinateBar();
    private final JConfigBar configBar = new JConfigBar();
    private final JMap2D map;
    private boolean firstPaint = true;

    
    public Go2MapView(JMap2D map){
        super();
        setLayout(new BorderLayout());
        this.map = map;

        final JPanel north = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //search available toolbars
        Lookup lk = Lookups.forPath("/Puzzle/Engine/Go2/ToolBar");
        for(MapControlBar item : lk.lookupAll(MapControlBar.class)){
            try {
                //TODO find a better solution, store only the class reference in layer.xml
                //we must copy this item since we may have several maps at the same time
                item = item.getClass().newInstance();
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.weightx = 0.0;
            item.setMap(map);
            Component comp = item.getComponent();
            if(comp instanceof JToolBar){
                ((JToolBar)comp).setFloatable(false);
            }
            north.add(comp,constraints);
        }

        //search available decorations
        lk = Lookups.forPath("/Puzzle/Engine/Go2/Decoration");
        for(MapDecoration item : lk.lookupAll(MapDecoration.class)){
            try {
                //TODO find a better solution, store only the class reference in layer.xml
                //we must copy this item since we may have several maps at the same time
                item = item.getClass().newInstance();
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
            map.addDecoration(item);
        }

        //a an empty component to fill space, like glue
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        JToolBar glue = new JToolBar();
        glue.setFloatable(false);
        north.add(glue,constraints);

        coordBar.setMap(map);
        configBar.setMap(map);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        north.add(configBar,constraints);
        
        navBar.setFloatable(false);
        infoBar.setFloatable(false);
        configBar.setFloatable(false);
        coordBar.setFloatable(false);
        add(BorderLayout.NORTH,north);
        add(BorderLayout.CENTER,map);
        add(BorderLayout.SOUTH,coordBar);

    }

    public JMap2D getMap() {
        return map;
    }

    @Override
    public MapContext getContext() {
        return getMap().getContainer().getContext();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(firstPaint){
            firstPaint = false;

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    map.getCanvas().getController().repaint();
                }
            });
        }

    }

}
