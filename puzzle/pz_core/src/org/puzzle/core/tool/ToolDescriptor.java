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

package org.puzzle.core.tool;

import java.awt.Component;
import java.awt.Image;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public interface ToolDescriptor {

    /**
     * empty String array
     */
    public static final String[] EMPTY_STRING_ARRAY = {};
    
    /**
     * @return name of the tool
     */
    public String getTitle();
    
    /**
     * short description of the tool
     * @return String
     */
    public String getDescription();
    
    /**
     * used to categorize a widget tool
     * @return String Path ex: {utilities,convert}
     */
    public String[] getPath();
    
    /**
     * Keywords for this tool
     * @return String[]
     */
    public String[] getKeyWords();
    
    /**
     * Array of string for categories 
     * @return String[]
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
     * @return
     */
    public Image getIcon(int iconType);
    
}
