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
package org.puzzle.core.view;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


/**
 *
 * @author Johann Sorel
 */
public class DefaultViewService implements ViewService{

    private final InstanceContent CONTENT = new InstanceContent();
    private final Lookup LOOKUP = new AbstractLookup(CONTENT);
    
    public DefaultViewService() {
    }

    public Lookup getLookup() {
        return LOOKUP;
    }
    
    /**
     * add a new view
     * @param view the mapview to add
     */
    public void add(MapView view) {        
        CONTENT.add(view);        
        if(!view.isOpened()) view.open();
    }

    /**
     * remove view
     * @param view target mapview to remove
     */
    public void remove(MapView view) {
        CONTENT.remove(view);
    }

    public void add(MapGroup group) {
        CONTENT.add(group);
    }

    public void remove(MapGroup group) {
        CONTENT.remove(group);
    }
}
