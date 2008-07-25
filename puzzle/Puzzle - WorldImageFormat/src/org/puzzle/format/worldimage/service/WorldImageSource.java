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
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.feature.SchemaException;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.styling.Style;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.context.LayerSource;
import org.puzzle.puzzlecore.context.RichMapLayer;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 *
 * @author  Thomas Bonavia
 */
public class WorldImageSource implements GISSource{
    private static final String IMAGE_ICON_BASE = "org/puzzle/format/worldimage/worldimage.png";
    
    private final int id;
    private final Map<String,String> parameters;
    private final String name;
    private final String serviceName;
    private GridCoverage2D gc2d = null;

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
    
    public RichMapLayer createLayer(Map<String, String> parameters) {
        Style style = new RandomStyleFactory().createRasterStyle();
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

    public int getID() {
        return id;
    }

    public Image getIcon(int type) {
        return Utilities.loadImage(IMAGE_ICON_BASE);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTitle() {
        return name;
    }
    
}
