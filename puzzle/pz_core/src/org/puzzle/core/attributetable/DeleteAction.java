/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.core.attributetable;

/**
 * This class is made to extend the menu item from geotoolkit, this
 * will implicitly make the class part of this module class-loader and
 * allow it to be loaded using the layer.xml.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class DeleteAction extends org.geotoolkit.gui.swing.propertyedit.DeleteSelectionAction{

    public DeleteAction(){
    }
}
