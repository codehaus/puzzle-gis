/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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

package org.puzzle.renderer.worldwind;

import org.geotoolkit.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.openide.util.Exceptions;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class WorldWindConstants {

    public static CoordinateReferenceSystem EPSG_4326 = null;

    static{
        try {
            EPSG_4326 = CRS.decode("EPSG:4326");
        } catch (NoSuchAuthorityCodeException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FactoryException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private WorldWindConstants(){}
    
}
