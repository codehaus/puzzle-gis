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

package org.puzzle.format.shapefile.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.collections.map.SingletonMap;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.MapContext;
import org.geotools.styling.Style;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.context.LayerSource;
import org.puzzle.puzzlecore.context.RichMapLayer;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 *
 * @author  Johann Sorel
 */
public class ShapeFileSource implements GISSource{

    private static final String IMAGE_ICON_BASE = "org/puzzle/format/shapefile/shapefile.png";
    
    private final int id;
    private final Map<String,String> parameters;
    private final String name;
    private final String serviceName;
    private FeatureSource featureSource = null;
    
    
    ShapeFileSource(File shapefile, String serviceName,int id, Map<String,String> parameters){
        this.id = id;
        this.name = shapefile.getName();
        this.serviceName = serviceName;
        this.parameters = parameters;
        DataStore store = null;
        try {
            store = DataStoreFinder.getDataStore(new SingletonMap("url", shapefile.getPath()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    public RichMapLayer createLayer(Map<String, String> parameters) {
        Style style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
        LayerSource source = new LayerSource(id, parameters);
        RichMapLayer layer = new RichMapLayer(featureSource, style,source);
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

    public String getTitle() {
        return name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public WizardDescriptor createLayerWizard(Collection<? extends MapContext> contexts, GISProject project) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
