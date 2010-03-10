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

import com.sun.opengl.util.BufferUtil;
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
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import org.geotoolkit.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Exceptions;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class WorldWindUtils {

    private WorldWindUtils(){}


    public static Renderable toRenderable(Geometry geom, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{

        if(geom instanceof Point){

        }else if(geom instanceof MultiPoint){

        }else if(geom instanceof LineString){
            return toRenderableLine((LineString)geom,trs,globe);
        }else if(geom instanceof MultiLineString){
            return toRenderableLine((MultiLineString)geom,trs,globe);
        }else if(geom instanceof Polygon){

        }else if(geom instanceof MultiPolygon){
            return toRenderable((MultiPolygon)geom,trs);

        }

        return null;
    }

    public static Renderable toRenderableLine(Geometry line, MathTransform trs, Globe globe) throws MismatchedDimensionException, TransformException{
        return new LineRenderable(line,trs,globe);
    }

    private static class LineRenderable implements Renderable{

        private final int size;
        private final DoubleBuffer buffer;
        //private final Position[] positions;
        //private final Vec4[] vertexes;

        LineRenderable(Geometry line, MathTransform trs, Globe globe) {

            Position[] positions = null;
            try {
                final Geometry geoEPSGWGS84 = JTS.transform(line, trs);
                final Coordinate[] coords = geoEPSGWGS84.getCoordinates();

                List<Position> pos = new ArrayList<Position>();
                for (int i = 0; i < coords.length; i++) {
                    pos.add(new Position(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y), coords[i].z));
                }

                positions = pos.toArray(new Position[pos.size()]);

            } catch (MismatchedDimensionException ex) {
                Exceptions.printStackTrace(ex);
            } catch (TransformException ex) {
                Exceptions.printStackTrace(ex);
            }


            buffer = BufferUtil.newDoubleBuffer(positions.length*3);
            size = positions.length;

            //vertexes = new Vec4[positions.length];
            for(int i=0; i<positions.length; i++){
                //vertexes[i] = dc.getSurfaceGeometry().getSurfacePoint(positions[i].latitude,positions[i].longitude,2+dc.getVerticalExaggeration());

//                if (vertexes[i] == null) {
                    Vec4 vec = globe.computePointFromPosition(positions[i].latitude,positions[i].longitude,
                        (1 + globe.getElevation(positions[i].latitude,positions[i].longitude)));
                    buffer.put(vec.x);
                    buffer.put(vec.y);
                    buffer.put(vec.z);
//                }
            }

            buffer.rewind();
        }

        public void render(DrawContext dc) {
//            final Globe globe = dc.getGlobe();
//
//            final Vec4[] vertexes = new Vec4[positions.length];
//            for(int i=0; i<vertexes.length; i++){
//                vertexes[i] = dc.getSurfaceGeometry().getSurfacePoint(positions[i].latitude,positions[i].longitude,2+dc.getVerticalExaggeration());
//
//                if (vertexes[i] == null) {
//                    vertexes[i] = globe.computePointFromPosition(positions[i].latitude,positions[i].longitude,
//                        (2 + globe.getElevation(positions[i].latitude,positions[i].longitude)) * dc.getVerticalExaggeration());
//                }
//            }

            final GL gl = dc.getGL();

            /* select white for all lines */
            gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glLineWidth(2);

            gl.glVertexPointer(3, GL.GL_DOUBLE, 0, buffer);
            gl.glDrawArrays(GL.GL_LINE_STRIP, 0, size);

            gl.glFlush();
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glDisableClientState(GL.GL_VERTEX_ARRAY);

        }

    }

//    public static Renderable toRenderable(MultiLineString line, MathTransform trs) throws MismatchedDimensionException, TransformException{
//        final Geometry geoEPSGWGS84 = JTS.transform(line, trs);
//        final Coordinate[] coords = geoEPSGWGS84.getCoordinates();
//
//        List<Position> positions = new ArrayList<Position>();
//        for (int i = 0; i < coords.length; i++) {
//            positions.add(new Position(Angle.fromDegreesLatitude(coords[i].x), Angle.fromDegreesLongitude(coords[i].y), coords[i].z));
//        }
//
//        Polyline wwjLine = new Polyline();
//        wwjLine.setPositions(positions);
//        wwjLine.setFollowTerrain(true);
//        wwjLine.setLineWidth(1);
//        wwjLine.setColor(Color.RED);
//
//        return wwjLine;
//    }

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
