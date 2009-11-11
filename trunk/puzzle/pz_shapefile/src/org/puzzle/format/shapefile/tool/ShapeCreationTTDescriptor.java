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
package org.puzzle.format.shapefile.tool;

import java.awt.Component;

import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.format.shapefile.resources.ShapefileResource;


/**
 * Widget Tool descriptor for shapefile creation tool, this is used in the ToolBox tree.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class ShapeCreationTTDescriptor extends AbstractToolDescriptor{
    
    private final String[] path = ToolTreeConstants.getInstance().FILE_CREATE.getPath();
    
    private String title = ShapefileResource.getString("shapefile_creation");
       
    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getPath() {
        return path;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getComponent() {
        return new ShapeCreationTool();
    }

}
