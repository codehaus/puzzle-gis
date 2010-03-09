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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.render.Renderable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.geotoolkit.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class WorldWindUtils {

    private WorldWindUtils(){}


    public static Renderable toRenderable(Geometry geom, MathTransform trs) throws MismatchedDimensionException, TransformException{

        if(geom instanceof Point){

        }else if(geom instanceof MultiPoint){

        }else if(geom instanceof LineString){

        }else if(geom instanceof MultiLineString){
            return toRenderable((MultiLineString)geom,trs);
        }else if(geom instanceof Polygon){

        }else if(geom instanceof MultiPolygon){
            return toRenderable((MultiPolygon)geom,trs);

        }

        return null;
    }

    public static Renderable toRenderable(MultiLineString line, MathTransform trs) throws MismatchedDimensionException, TransformException{
        final Geometry geoEPSGWGS84 = JTS.transform(line, trs);
        final Coordinate[] coords = geoEPSGWGS84.getCoordinates();

        List<Position> positions = new ArrayList<Position>();
        for (int i = 0; i < coords.length; i++) {
            positions.add(new Position(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y), coords[i].z));
        }

        Polyline wwjLine = new Polyline();
        wwjLine.setPositions(positions);
        wwjLine.setFollowTerrain(true);
        wwjLine.setLineWidth(1);
        wwjLine.setColor(Color.RED);

        return wwjLine;
    }

    public static Renderable toRenderable(MultiPolygon polygon, MathTransform trs) throws MismatchedDimensionException, TransformException{
        final Geometry geoEPSGWGS84 = JTS.transform(polygon, trs);
        final Coordinate[] coords = geoEPSGWGS84.getCoordinates();

        List<LatLon> latlons = new ArrayList<LatLon>();
        for (int i = 0; i < coords.length; i++) {
            latlons.add(new LatLon(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y)));
        }

        gov.nasa.worldwind.render.airspaces.Polygon wwjLine = new gov.nasa.worldwind.render.airspaces.Polygon();
        wwjLine.setLocations(latlons);
        wwjLine.setTerrainConforming(true);
        wwjLine.setAltitudes(5, 30);
        wwjLine.setEnableCaps(false);
        //wwjLine.setEnableCaps(false);
        wwjLine.setEnableLevelOfDetail(false);
//        wwjLine.setPositions(positions);
//        wwjLine.setFollowTerrain(true);
//        wwjLine.setLineWidth(1);
//        wwjLine.setColor(Color.RED);

        return wwjLine;
    }



}
