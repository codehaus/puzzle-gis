/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2007, GeoTools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.puzzle.puzzlecore.struct.favorites;

import java.io.File;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

/**
 *
 * @author johann sorel
 */
public class MapComboBoxModel extends ListComboBoxModel{

    
    public MapComboBoxModel(List<File> paths){
        super(paths);
    }
    
    
    public File getFileAt(int index){
        return ((File)data.get(index));
    }
    
    public Object getElementAt(int index) {        
       return ((File)data.get(index)).getPath();
    }


}
