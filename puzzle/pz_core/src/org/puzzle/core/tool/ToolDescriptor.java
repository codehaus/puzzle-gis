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

import java.awt.Component;
import java.awt.Image;

/**
 * A Tool Descriptor is a set of informations to classify the tool.
 * We can have his categories and a treepath that we be used in the Tooltree.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public interface ToolDescriptor {

    /**
     * Empty String array, can be used for keywords, path or categories.
     */
    public static final String[] EMPTY_STRING_ARRAY = {};
    
    /**
     * @return name of the tool , never null.
     */
    public String getTitle();
    
    /**
     * short description of the tool
     * @return String , never null.
     */
    public String getDescription();
    
    /**
     * used to categorize a widget tool
     * @return String Path ex: {utilities,convert} , never null but can be empty.
     */
    public String[] getPath();
    
    /**
     * Keywords for this tool
     * @return String[] , never null but can be empty.
     */
    public String[] getKeyWords();
    
    /**
     * Array of string for categories 
     * @return String[] , never null but can be empty.
     */
    public String[] getCategories();
    
    /**
     * @param parameters 
     * @return the panel of the tool
     */
    public Component getComponent();
           
    /**
     * Used if a tool has a special icon.
     * 
     * @param iconType : same as bean Icon,
     * can be BeanInfo :
     * ICON_COLOR_16x16
     * ICON_COLOR_32x32
     * ICON_MONO_16x16
     * ICON_MONO_32x32
     * @return Image, can be null if none.
     */
    public Image getIcon(int iconType);
    
}
