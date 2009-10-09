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
package org.puzzle.core.project.source;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Information of a GIS Source, this hold the ID and every parameters
 * needed to save and restore the GISSource.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class GISSourceInfo {

    public static final int UNREGISTERED_ID = -1;

    private final int id;
    private final String serviceName;
    private final Map<String,Serializable> parameters;

    public GISSourceInfo(int id, String serviceName, Map<String, Serializable> parameters) {
        if(serviceName == null){
            throw new NullPointerException("Service name can not be null");
        }
        this.id = id;
        this.serviceName = serviceName;

        //make a defensive copy immutable
        final Map<String,Serializable> params = new HashMap<String,Serializable>(parameters);
        this.parameters = Collections.unmodifiableMap(params);
    }

    /**
     * Returns the ID of the source, unique in the current project.
     */
    public int getID(){
        return id;
    }
    
    /**
     * Get the {@code GISSourceService} name to use for managing the data
     * represented by each {@code GISSource}.
     * @return  A {@code String} containing the name of the service.
     */
    public String getServiceName(){
        return serviceName;
    }
    
    /**
     * The parameters describing the {@code GISSource}.
     * This parameters are retrieved from the XML storing the {@code GISSource}.
     * @return A {@code Map} containing all parameters.
     */
    public Map<String,Serializable> getParameters(){
        return parameters;
    }
    
}
