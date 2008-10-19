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

import java.awt.BorderLayout;
import org.geotools.gui.swing.map.map2d.Map2D;
import org.geotools.map.MapContext;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class MapView extends TopComponent {

    protected Map2D map;
    
    public MapView(Map2D map){
        super();
        
        associateLookup( Lookups.singleton(this));
        
        this.map = map;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,map.getComponent());
        revalidate();
    }
    
    public Map2D getMap() {
        return map;
    }

    public abstract MapContext getContext();

    @Override
    protected void componentOpened() {
        
        ViewService v = Lookup.getDefault().lookup(ViewService.class);
        if (v != null) {
            v.add(this);
        }
        
        super.componentOpened();
    }

    @Override
    protected void componentClosed() {
        ViewService v = Lookup.getDefault().lookup(ViewService.class);
        if (v != null) {
            v.remove(this);
        }
        
        map.dispose();
        map = null;
        super.componentClosed();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

}
