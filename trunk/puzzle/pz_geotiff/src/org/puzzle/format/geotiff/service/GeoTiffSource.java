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
package org.puzzle.format.geotiff.service;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.MapBuilder;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * This is a {@code GISSource} used to reference a Geotiff file in
 * a {@link org.puzzle.core.project.GISProject}.
 * 
 * @author  Thomas Bonavia
 * @author  Johann Sorel (Puzzle-GIS)
 * 
 * @see     org.puzzle.core.project.source.GISSource
 */
public class GeoTiffSource extends GISSource{
    
    private final String name;
    private GridCoverage2D gc2d = null;

    /**
     * Constructor.
     * Initializes a {@code GeoTiffSource} from the Geotiff file.
     * @param geotiff       The geotiff file.
     * @param info          save/restore informations.
     */
    GeoTiffSource(final GISSourceInfo info,File geotiff){
        super(info);
        this.name = geotiff.getName();
        
        GeoTiffReader reader;
        try {
            reader = new GeoTiffReader(geotiff);
            gc2d = (GridCoverage2D)reader.read(null);
        } catch (DataSourceException ex) {
            Logger.getLogger(GeoTiffSource.class.getName()).log(Level.SEVERE,
                    "Unable to read "+geotiff.getName(),ex);
        } catch (IOException ioe){
            Logger.getLogger(GeoTiffSource.class.getName()).log(Level.SEVERE,
                    "Unable to read "+geotiff.getName(),ioe);
        }

    }
    
    /** {@inheritDoc } */
    @Override
    public MapLayer createLayer(Map<String, String> parameters) {
        final MutableStyle style = new RandomStyleFactory().createRasterStyle();
        if(parameters == null)parameters = Collections.emptyMap();
        final LayerSource source = new LayerSource(getInfo().getID(), parameters,this);

        final MapLayer layer = MapBuilder.getInstance().createCoverageLayer(gc2d, style, name);
        layer.setUserPropertie(PZLayerConstants.KEY_LAYER_INFO, source);
        layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(name,"") );
        
        return layer;
    }

    /** {@inheritDoc } */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/geotiff/geotiff.png");
    }

    /** {@inheritDoc } */
    @Override
    public void showLayerWizard(Collection<? extends MapContext> contexts, GISProject project) {
        WizardDescriptor.Panel<WizardDescriptor>[] pans = getPanels(contexts, project);
        WizardDescriptor wizardDescriptor = new WizardDescriptor(pans);
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(NbBundle.getMessage(GeoTiffSource.class, "createLayer"));
        DialogDisplayer.getDefault().notify(wizardDescriptor);
        
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            MapContext context = ((LayerCreationVisualPanel)panels[0].getComponent()).getContext();
            String title = ((LayerCreationVisualPanel)panels[0].getComponent()).getTitle();
            MapLayer layer = createLayer(null);
            layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(title,"") );
            context.layers().add(layer);
        }
        
    }

    private WizardDescriptor.Panel<WizardDescriptor>[] panels;
    
    private WizardDescriptor.Panel<WizardDescriptor>[] getPanels(Collection<? extends MapContext> contexts, GISProject project) {
            panels = new WizardDescriptor.Panel[]{
                        new LayerCreationWizardPanel(contexts,project)
                    };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
                }
            }
        return panels;
    }
}
