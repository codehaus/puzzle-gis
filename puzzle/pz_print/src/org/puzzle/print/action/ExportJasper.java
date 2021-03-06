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
package org.puzzle.print.action;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JRException;
import org.geotoolkit.geometry.GeneralEnvelope;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.opengis.geometry.Envelope;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.view.ViewComponent;
import org.puzzle.renderer.go2.Go2MapView;

/**
 * Save view action.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class ExportJasper extends CookieAction{

    public ExportJasper() {}

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{GISView.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        if(activatedNodes.length == 0 ) return ;

        final GISView view = activatedNodes[0].getLookup().lookup(GISView.class);
        if(view == null ) return;
        final Project project = FileOwnerQuery.getOwner(view.getContext().getPrimaryFile());               
        if(project == null || !(project instanceof GISProject) ) return;

        final Collection<FileObject> jasperFiles = ((GISProject)project).getFiles("jrxml");
//        final Collection<File> files = new ArrayList<File>();
//
//        for(FileObject obj : jasperFiles){
//            files.add(FileUtil.toFile(obj));
//            System.out.println(FileUtil.toFile(obj));
//        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ViewComponent viewcomp = view.getComponent(false);

                if(viewcomp != null && viewcomp instanceof Go2MapView){
//                    Image img = viewcomp.getMap().getCanvas().getSnapShot();

                    try {
                        Shape shap = ((Go2MapView)view.getComponent(true)).getMapView().getCanvas().getObjectiveBounds();
                        GeneralEnvelope env = new GeneralEnvelope(shap.getBounds2D());
                        env.setCoordinateReferenceSystem(((Go2MapView)view.getComponent(true)).getMapView().getCanvas().getObjectiveCRS());
                        JJasperDialog dia = new JJasperDialog(jasperFiles,view.getContext().getContext(),env);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }

                }
            }
        });

    }

    @Override
    public String getName() {
        return Utilities.getString("exportJasper");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if(activatedNodes.length == 0 ) return false;
        final GISView view = activatedNodes[0].getLookup().lookup(GISView.class);
        if(view == null) return false;
        final ViewComponent viewcomp = view.getComponent(false);
        return viewcomp != null;
    }

}
