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
public class PolygonRenderable_1 implements Renderable {

    private static final double EPS = 1E-3;

    private final DoubleBuffer vertexBuffer;
    private final DoubleBuffer normalBuffer;
    private final IntBuffer indexBuffer;
    private final int nbQuad;
    private final int nbTriangle;

    public PolygonRenderable_1(Polygon geom, MathTransform trs, Globe globe){

        vertexBuffer  = BufferUtil.newDoubleBuffer(4*3);
        normalBuffer  = BufferUtil.newDoubleBuffer(4*3);
        indexBuffer   = BufferUtil.newIntBuffer(1);

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

        nbQuad = 1;
        nbTriangle = 0;
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
