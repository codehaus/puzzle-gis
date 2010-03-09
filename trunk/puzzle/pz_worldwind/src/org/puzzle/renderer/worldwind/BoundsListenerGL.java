/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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

package org.puzzle.renderer.worldwind;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import org.geotoolkit.geometry.GeneralEnvelope;
import org.opengis.geometry.Envelope;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public abstract class BoundsListenerGL implements Renderable{

    private final GeneralEnvelope env = new GeneralEnvelope(WorldWindConstants.EPSG_4326);

    public void render(DrawContext dc) {
        final Sector sector = dc.getVisibleSector();
        if(    env.getMinimum(0) != sector.getMinLatitude().degrees
            || env.getMaximum(0) != sector.getMaxLatitude().degrees
            || env.getMinimum(1) != sector.getMinLongitude().degrees
            || env.getMaximum(1) != sector.getMaxLongitude().degrees ){
            //envelope changed
            env.setRange(0, sector.getMinLatitude().degrees, sector.getMaxLatitude().degrees);
            env.setRange(1, sector.getMinLongitude().degrees, sector.getMaxLongitude().degrees);
            boundsChange(env);
        }
    }

    public abstract void boundsChange(Envelope env);

}
