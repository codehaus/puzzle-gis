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
import gov.nasa.worldwind.geom.Sphere;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.Renderable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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


    public static Collection<? extends Renderable> toRenderable(Geometry geom, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{

        if(geom instanceof Point){

        }else if(geom instanceof MultiPoint){

        }else if(geom instanceof LineString){
            return toRenderable((LineString)geom,trs,globe);
        }else if(geom instanceof MultiLineString){
            return toRenderable((MultiLineString)geom,trs,globe);
        }else if(geom instanceof Polygon){
            return toRenderable((Polygon)geom,trs,globe);
        }else if(geom instanceof MultiPolygon){
            return toRenderable((MultiPolygon)geom,trs,globe);
        }

        return null;
    }

    public static Collection<? extends Renderable> toRenderable(Point point, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        final Geometry geoEPSGWGS84 = JTS.transform(point, trs);
        final Coordinate crd = geoEPSGWGS84.getCoordinate();
        final LatLon ll = new LatLon(Angle.fromDegreesLatitude(crd.x), Angle.fromDegreesLongitude(crd.y));

        Vec4 vec = globe.computePointFromPosition(ll,5);
        Renderable r = new Sphere(vec, 5);
        return Collections.singleton(r);
    }

    public static Collection<? extends Renderable> toRenderable(MultiPoint points, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        final List<Renderable> rds = new ArrayList<Renderable>();
        for(int i=0,n=points.getNumGeometries();i<n;i++){
            rds.addAll(toRenderable(points.getGeometryN(i), trs, globe));
        }
        return rds;
    }

    public static Collection<? extends Renderable> toRenderable(LineString line, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        return Collections.singleton(new LineRenderable(line,trs,globe));
    }

    public static Collection<? extends Renderable> toRenderable(MultiLineString line, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        final List<Renderable> rds = new ArrayList<Renderable>();
        for(int i=0,n=line.getNumGeometries();i<n;i++){
            rds.addAll(toRenderable(line.getGeometryN(i), trs, globe));
        }
        return rds;
    }

//    public static Renderable toRenderable(Polygon polygon, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
//        return new PolygonRenderable(polygon, trs, globe);
//    }
//
//    public static Renderable toRenderable(MultiPolygon polygon, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
//        return new PolygonRenderable((Polygon) polygon.getGeometryN(0), trs, globe);
//    }

    public static Collection<? extends Renderable> toRenderable(MultiPolygon polygon, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        final List<Renderable> rds = new ArrayList<Renderable>();
        for(int i=0,n=polygon.getNumGeometries();i<n;i++){
            rds.addAll(toRenderable(polygon.getGeometryN(i), trs, globe));
        }
        return rds;
    }


    public static Collection<? extends Renderable> toRenderable(Polygon polygon, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
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

        return Collections.singleton(wwjLine);
    }



}
