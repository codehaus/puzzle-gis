/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.core.project.source;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * this class holds the parameters to save and restore the map layer.
 *
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 */
public final class GISLayerSource {

    /**
     * Key providing the saving information of this layer.
     * should be a Layersource object.
     */
    public static final String KEY_LAYER_INFO = "pz_layer_source_info";

    private final int sourceId;
    private final Map<String,String> params = new HashMap<String, String>();
    private final GISSource source;
    
    public GISLayerSource(int sourceId, Map<String,String> parameters, GISSource source){
        if(sourceId <=0){
            throw new NullPointerException("SourceID can not be null");
        }
        if(parameters == null){
            parameters = Collections.emptyMap();
        }

        this.sourceId = sourceId;
        this.source = source;
        if(parameters != null) this.params.putAll(parameters);
    }

    /**
     * Retuens the ID of the original GIS source.
     */
    public final int getSourceId(){
        return sourceId;
    }

    /**
     * Returns the parameters that will be used by the GIS source
     * to restore the layer.
     */
    public final Map<String,String> getParameters(){
        return Collections.unmodifiableMap(params);
    }

    /**
     * Return the original GIS source of this layer.
     */
    public final GISSource getGISSource(){
        return source;
    }
    
}
