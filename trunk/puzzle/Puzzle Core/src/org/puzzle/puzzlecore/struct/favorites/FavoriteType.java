/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

package org.puzzle.puzzlecore.struct.favorites;

import java.awt.Component;
import java.io.Serializable;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * @author johann sorel
 */
public interface FavoriteType<T> extends Serializable{

    public String getTitle();
    
    public ImageIcon getIcon();
    
    public List<T> createEmptyList();
    
    public Component getManagingPanel(List<T> favoriteValues);
    
    public String toXML(List<T> favoriteValues);
    
    public List<T> toFavorites(String xml);
    
}
