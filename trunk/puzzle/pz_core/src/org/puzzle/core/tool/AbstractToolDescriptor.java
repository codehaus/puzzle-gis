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
package org.puzzle.core.tool;

import java.awt.Image;

/**
 * Abstract class for tools, this implementation provide default paths, description
 * and no icon for the tool.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class AbstractToolDescriptor implements ToolDescriptor{

    /**
     * {@inheritDoc }
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getPath() {
        return EMPTY_STRING_ARRAY;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getKeyWords() {
        return EMPTY_STRING_ARRAY;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getCategories() {
        return EMPTY_STRING_ARRAY;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int iconType){
        return null;
    }
}
