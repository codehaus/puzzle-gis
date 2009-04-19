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
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 * A map view is a windows component displaying a map widget.
 * This component is automaticly registered in the ViewService when opened
 * and removed when closed.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class ViewComponent extends TopComponent {

    /**
     * Map2D widget, is display at the center of the window.
     */
    protected final Map2D map;

    
    public ViewComponent(final Map2D map){
        super();
        
        associateLookup( Lookups.singleton(this));
        
        this.map = map;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,map.getComponent());
        revalidate();
    }

    /**
     * Get the map2D widget of this view.
     *
     * @return Map2D
     */
    public Map2D getMap() {
        return map;
    }

    /**
     * Returns the mapcontext displayed by the map widget.
     * 
     * @return MapContext
     */
    public abstract MapContext getContext();

    /**
     * {@inheritDoc }
     */
    @Override
    protected void componentOpened() {
        super.componentOpened();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void componentClosed() {
        super.componentClosed();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void componentActivated() {
        super.componentActivated();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

}
