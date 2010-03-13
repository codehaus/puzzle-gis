/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.renderer.worldwind;

import com.sun.opengl.util.BufferUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL;
import org.geotoolkit.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Exceptions;

/**
 *
 * @author eclesia
 */
public class PolygonRenderable implements Renderable {

    private static final double EPS = 1E-3;

    private final DoubleBuffer vertexBuffer;
    private final DoubleBuffer normalBuffer;
    private final IntBuffer indexBuffer;
    private final int nbQuad;
    private final int nbTriangle;

    public PolygonRenderable(Polygon geom, MathTransform trs, Globe globe){

        try {
            geom = (Polygon) JTS.transform(geom, trs);
        } catch (MismatchedDimensionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformException ex) {
            Exceptions.printStackTrace(ex);
        }


        final ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
        builder.setSites(geom);
        builder.setConstraints(geom);
        builder.setTolerance(EPS);
        GeometryCollection pieces = (GeometryCollection) builder.getTriangles(new GeometryFactory(geom.getPrecisionModel()));

        final List<Coordinate[]> triangles = new ArrayList<Coordinate[]>();
        for(int i=0,n=pieces.getNumGeometries();i<n;i++){
            final Polygon poly = (Polygon) pieces.getGeometryN(i);

            if(geom.contains(poly)){
                triangles.add(poly.getCoordinates());
            }

            //sometimes polygon arn't contain because of calculation number rounding
            if(geom.overlaps(poly)){
                double a = poly.getArea();
                double b = poly.intersection(geom).getArea();
                if(Math.abs(a - b) < EPS * Math.max(Math.abs(a), Math.abs(b))){
                    triangles.add(poly.getCoordinates());
                }
            }
        }

        //compress triangulation
        final List<Integer> indexes = new ArrayList<Integer>();
        final List<Coordinate> vertexes = new ArrayList<Coordinate>();
        compress(triangles, vertexes, indexes);
        final int nbTriangleVertex = vertexes.size();

        //find the facades
        final List<Coordinate[]> rings = new ArrayList<Coordinate[]>();
        final Coordinate[] exteriorRing = geom.getExteriorRing().getCoordinates();
        rings.add(exteriorRing);
        int nbQuadVertex = 4*(exteriorRing.length-1) ;

        for(int i=0,n=geom.getNumInteriorRing();i<n;i++){
            final Coordinate[] hole = geom.getInteriorRingN(i).getCoordinates();
            nbQuadVertex += 4*(hole.length-1);
            rings.add(hole);
        }

        vertexBuffer  = BufferUtil.newDoubleBuffer(nbTriangleVertex*3+nbQuadVertex*3);
        normalBuffer  = BufferUtil.newDoubleBuffer(nbTriangleVertex*3+nbQuadVertex*3);
        indexBuffer   = BufferUtil.newIntBuffer(indexes.size()+nbQuadVertex);

        //calculate the minz and maxz
        final double minz = 0;
        final double maxz = 1;

        //make the facades
        int index = 0;
        for(Coordinate[] faces : rings){
            for(int i=0,n=faces.length-1;i<n;i++){
                Coordinate previous = faces[i];
                Coordinate coord = faces[i+1];

                double a = Math.PI/2;
                double x = previous.x - coord.x;
                double y = previous.y - coord.y;
                double nx =  x * Math.cos(a) - y * Math.sin(a);
                double ny =  x * Math.sin(a) + y * Math.cos(a);

                vertexBuffer.put(previous.x).put(maxz).put(previous.y);
                vertexBuffer.put(previous.x).put(minz).put(previous.y);
                vertexBuffer.put(coord.x).put(minz).put(coord.y);
                vertexBuffer.put(coord.x).put(maxz).put(coord.y);
                normalBuffer.put(nx).put(0).put(ny);
                normalBuffer.put(nx).put(0).put(ny);
                normalBuffer.put(nx).put(0).put(ny);
                normalBuffer.put(nx).put(0).put(ny);
                indexBuffer.put(index++).put(index++);
                indexBuffer.put(index++).put(index++);
            }
        }


        int start = index;

        //make the top face
        for(Coordinate c : vertexes){
            vertexBuffer.put(c.x).put(maxz).put(c.y);
            normalBuffer.put(0).put(1).put(0);
        }
        for(Integer i : indexes){
            indexBuffer.put(index+i);
        }

        //invert the triangles indexes
        for(int i=start;i<=indexBuffer.limit()-3;){
            int t1 = indexBuffer.get(i);
            int t2 = indexBuffer.get(i+1);
            int t3 = indexBuffer.get(i+2);

            indexBuffer.put(i, t3);
            indexBuffer.put(i+1, t2);
            indexBuffer.put(i+2, t1);

            i+=3;
        }

        //calculate on the globe
        for(int i=0; i<vertexBuffer.limit(); i+=3){
            double lat = vertexBuffer.get(i);
            double lon = vertexBuffer.get(i+2);
            double z = vertexBuffer.get(i+1);
            Position pos = new Position(Angle.fromDegreesLatitude(lat), Angle.fromDegreesLongitude(lon), z);

            Vec4 vec = globe.computePointFromPosition(
                    pos.latitude, pos.longitude,
                    (1 + globe.getElevation(pos.latitude, pos.longitude)));
            vertexBuffer.put(i, vec.x);
            vertexBuffer.put(i+1, vec.y);
            vertexBuffer.put(i+2, vec.z);
        }

        nbQuad = nbQuadVertex;
        nbTriangle = indexes.size();
    }

    public void render(DrawContext dc) {

        final GL gl = dc.getGL();

        /* select white for all lines */
        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL.GL_INDEX_ARRAY);
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glVertexPointer(3, GL.GL_DOUBLE, 0, vertexBuffer);
        gl.glNormalPointer(GL.GL_DOUBLE, 0, normalBuffer);
        gl.glIndexPointer(GL.GL_INT, 0, indexBuffer);
        gl.glDrawArrays(GL.GL_QUADS, 0, nbQuad);

        gl.glVertexPointer(3, GL.GL_DOUBLE, nbQuad, vertexBuffer);
        gl.glNormalPointer(GL.GL_DOUBLE, nbQuad, normalBuffer);
        gl.glIndexPointer(GL.GL_INT, nbQuad, indexBuffer);
        gl.glDrawArrays(GL.GL_TRIANGLES, nbQuad, nbTriangle);


        gl.glFlush();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL.GL_INDEX_ARRAY);

    }


    private static void compress(List<Coordinate[]> coords, List<Coordinate> vertexes, List<Integer> indexes){
        Map<Coordinate,Integer> lst = new HashMap<Coordinate,Integer>();

        int inc = -1;

        for(Coordinate[] coord : coords){
            for(int i=0;i<3;i++){
                //use only 3 first coords, the 4th one is the same as the first one
                Coordinate c = coord[i];
                Integer index = lst.get(c);
                if(index != null){
                    indexes.add(index);
                }else{
                    inc++;
                    vertexes.add(c);
                    indexes.add(inc);
                    lst.put(c, inc);
                }
            }
        }

    }

}
