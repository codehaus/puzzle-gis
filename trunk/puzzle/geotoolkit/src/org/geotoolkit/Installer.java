/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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

package org.geotoolkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotoolkit.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 *
 * @author Johann Sorel
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        //force netbeans platform classloader to load the derby and postgres driver
        //often use for EPSG databases
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }


        try {
            //force accessing EPSG database or create it if necessary
            CRS.decode("EPSG:3395");
        } catch (NoSuchAuthorityCodeException ex) {
            Logger.getLogger(Installer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FactoryException ex) {
            Logger.getLogger(Installer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
