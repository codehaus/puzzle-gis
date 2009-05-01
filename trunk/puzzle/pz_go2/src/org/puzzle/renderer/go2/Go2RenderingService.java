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
package org.puzzle.renderer.go2;

import java.awt.Image;

import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.geotoolkit.gui.swing.go.J2DMapVolatile;
import org.geotoolkit.map.MapContext;
import org.geotoolkit.referencing.CRS;

import org.geotoolkit.referencing.operation.matrix.AffineMatrix3;
import org.geotoolkit.referencing.operation.transform.AffineTransform2D;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

import org.puzzle.core.project.view.GISView;
import org.puzzle.core.view.ViewComponent;
import org.puzzle.core.view.RenderingService;

/**
 * Rendering service for the Go2 engine.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class Go2RenderingService implements RenderingService{

    private static final String ID = "GO2";
    private static final String MATRIX_PARAMETER = "Matrix";
    private static final String CRS_PARAMETER = "Crs";

    public Go2RenderingService(){
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/puzzle/renderer/go2/go2.png");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(Go2RenderingService.class, "title");
    }

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public ViewComponent restoreView(MapContext context, final GISView view) {
        final J2DMapVolatile map = new J2DMapVolatile();
        map.getContainer().setContext(context);
        
//        try {
//            map.getCanvas().setObjectiveCRS(CRS.decode("EPSG:3395"));
//        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
//        }


        final String strCrs = view.getInfo().parameters().get(CRS_PARAMETER);
        final String matrix = view.getInfo().parameters().get(MATRIX_PARAMETER);

        //restore the matrix parameters if available
        if(matrix != null && !matrix.trim().isEmpty()){

            String[] splits = matrix.split(";");

            final AffineMatrix3 trs = new AffineMatrix3();
            trs.setTransform(
                    Double.valueOf(splits[0]),
                    Double.valueOf(splits[1]),
                    Double.valueOf(splits[2]),
                    Double.valueOf(splits[3]),
                    Double.valueOf(splits[4]),
                    Double.valueOf(splits[5]));
//            try {
                map.getCanvas().getController().transform(trs);
//            } catch (TransformException ex) {
//                Exceptions.printStackTrace(ex);
//            }
        }

        //restore the crs parameters if available
        if(strCrs != null && !strCrs.trim().isEmpty()){

            try {
                CoordinateReferenceSystem crs = CRS.decode(strCrs);
                map.getCanvas().setObjectiveCRS(crs);
            } catch (NoSuchAuthorityCodeException ex) {
                Exceptions.printStackTrace(ex);
            } catch (FactoryException ex) {
                Exceptions.printStackTrace(ex);
            } catch (TransformException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        


        map.getCanvas().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final AffineMatrix3 trs = map.getCanvas().getController().getTransform();
                final double[] matrix = new double[6];
                trs.getMatrix(matrix);

                final StringBuilder sb = new StringBuilder();
                int i=0;
                for(i=0;i<5;i++){
                    sb.append(matrix[i]).append(';');
                }
                sb.append(matrix[i]);

                view.getInfo().parameters().put(MATRIX_PARAMETER, sb.toString());
                try {
                    view.getInfo().parameters().put(CRS_PARAMETER, CRS.lookupIdentifier(map.getCanvas().getObjectiveCRS(), true));
                } catch (FactoryException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });


        Go2MapView viewComponent = new Go2MapView(map);
        viewComponent.setDisplayName(view.getTitle());
        return viewComponent;
    }

}
