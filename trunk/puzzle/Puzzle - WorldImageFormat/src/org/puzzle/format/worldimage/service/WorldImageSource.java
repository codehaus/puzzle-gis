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
package org.puzzle.format.worldimage.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.feature.SchemaException;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.MapContext;
import org.geotools.style.MutableStyle;
import org.opengis.referencing.operation.TransformException;
import org.openide.WizardDescriptor;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.context.LayerSource;
import org.puzzle.puzzlecore.context.RichMapLayer;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 * This is a {@code GISSource} used to reference a World Image file in
 * a {@link org.puzzle.puzzlecore.project.GISProject}. World Image files are :
 * <ul>
 *  <li>JPEG,</li>
 *  <li>PNG,</li>
 *  <li>BMP.</li>
 * </ul>
 * 
 * @author  Thomas Bonavia
 * 
 * @see     org.puzzle.puzzlecore.project.source.GISSource
 */
public class WorldImageSource implements GISSource{
    private static final String IMAGE_ICON_BASE = "org/puzzle/format/worldimage/worldimage.png";
    
    private final int id;
    private final Map<String,String> parameters;
    private final String name;
    private final String serviceName;
    private GridCoverage2D gc2d = null;

    /**
     * Constructor.
     * Initializes a {@code WorldImageSource} from the World Image file.
     * @param worldImage    The world image file.
     * @param serviceName   The name of the service to use to manage this source.
     * @param id            The ID of the source.
     * @param parameters    The parameters (from the XML file).
     */
    public WorldImageSource(File worldImage, String serviceName, int id, Map<String, String> parameters) {
        this.id = id;
        this.parameters = parameters;
        this.name = worldImage.getName();
        this.serviceName = serviceName;
        
        WorldImageReader reader;
        try {
            reader = new WorldImageReader(worldImage);
            gc2d = (GridCoverage2D)reader.read(null);
        } catch (DataSourceException ex) {
            Logger.getLogger(WorldImageSource.class.getName()).log(Level.SEVERE,
                    "Unable to read "+worldImage.getName(),ex);
        } catch (IOException ioe){
            Logger.getLogger(WorldImageSource.class.getName()).log(Level.SEVERE,
                    "Unable to read "+worldImage.getName(),ioe);
        }
    }
    
    /** {@inheritDoc } */
    public RichMapLayer createLayer(Map<String, String> parameters) {
        MutableStyle style = new RandomStyleFactory().createRasterStyle();
        LayerSource source = new LayerSource(id, parameters);
        RichMapLayer layer = null;
        try{
            layer = new RichMapLayer(gc2d, style,source);
        }catch(TransformException te){
            
        }catch(SchemaException se){
            
        }
        
        layer.setTitle(name);
        
        return layer;
    }

    /** {@inheritDoc } */
    public int getID() {
        return id;
    }

    /** {@inheritDoc } */
    public Image getIcon(int type) {
        return Utilities.loadImage(IMAGE_ICON_BASE);
    }

    /** {@inheritDoc } */
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /** {@inheritDoc } */
    public String getServiceName() {
        return serviceName;
    }

    /** {@inheritDoc } */
    public String getTitle() {
        return name;
    }

    public WizardDescriptor createLayerWizard(Collection<? extends MapContext> contexts, GISProject project) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
