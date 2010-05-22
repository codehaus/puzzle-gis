/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.geotoolkit.gui.swing.About;

/**
 * Display the geotoolkit about.
 *
 * @author Johann Sorel
 */
public final class AboutGeotoolKit implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        final About about = new About();
        about.showDialog(null);
    }
}
