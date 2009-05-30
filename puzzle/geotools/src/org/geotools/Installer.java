/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotoolkit.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author eclesia
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

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
