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
package org.puzzle.core.view;

import org.geotoolkit.gui.swing.go2.Map2D;
import org.geotoolkit.map.MapContext;
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
    
    public ViewComponent(){
        super();
        associateLookup( Lookups.singleton(this));
    }

    /**
     * Returns the mapcontext displayed by the map widget.
     * 
     * @return MapContext
     */
    public abstract MapContext getContext();

    public Map2D getMapView(){
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

}
