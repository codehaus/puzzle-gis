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
package org.puzzle.format.wfs.resources;

import java.util.ResourceBundle;

/**
 * I18N for this package
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class WFSResource {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org/puzzle/format/wfs/resources/Bundle");

    private WFSResource(){}

    public static final String getString(final String key){
        return BUNDLE.getString(key);
    }

}
