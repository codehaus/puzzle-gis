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

package org.puzzle.shell.gdal.bean;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

/**
 *
 * @author johann sorel
 */
public class Ogr2ogrOutputFormatEditor extends PropertyEditorSupport{

    
    private String[] tags ={
        "DGN",
        "GML",
        "Google KML",
        "MapInfo TAB",
        "Esri SHAPE"};

    @Override
    public String getAsText() {
        int index = ((Ogr2ogr.FORMAT)getValue()).ordinal();
        return tags[index];
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int index = Arrays.asList(tags).indexOf(text);
        if(index >=0 )setValue(Ogr2ogr.FORMAT.values()[index]);
    }
    
    @Override
    public String[] getTags(){
        return tags;
    }
    
    @Override
    public String getJavaInitializationString(){
        return Ogr2ogr.FORMAT.class.getName().replace('$', '.')+"."+getValue();
    }
    
}
