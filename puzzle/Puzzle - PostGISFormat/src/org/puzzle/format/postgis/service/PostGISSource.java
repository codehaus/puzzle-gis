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

package org.puzzle.format.postgis.service;

import java.awt.Image;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.FeatureSource;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.map.MapContext;
import org.geotools.style.MutableStyle;
import org.openide.WizardDescriptor;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.context.LayerSource;
import org.puzzle.puzzlecore.context.RichMapLayer;
import org.puzzle.puzzlecore.project.GISProject;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 *
 * @author  Johann Sorel
 */
public class PostGISSource implements GISSource{

    private static final String IMAGE_ICON_BASE = "org/puzzle/format/postgis/postgres.png";
    
    private final int id;
    private final Map<String,String> parameters;
    private final String name;
    private final String serviceName;
    private FeatureSource featureSource = null;
    
    
    PostGISSource(String serviceName,int id, Map<String,String> parameters){
        this.id = id;
        this.name = "hahaha";
        this.serviceName = serviceName;
        this.parameters = parameters;
                
    }
    
    public RichMapLayer createLayer(Map<String, String> parameters) {
        MutableStyle style = new RandomStyleFactory().createRandomVectorStyle(featureSource);
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

    @Override
    public void showLayerWizard(Collection<? extends MapContext> contexts, GISProject project) {
    }

}
