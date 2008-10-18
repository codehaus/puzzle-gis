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
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class GISSourceInfo {

    public static final int UNREGISTERED_ID = -1;

    private final int id;
    private final String serviceName;
    private final Map<String,String> parameters;

    public GISSourceInfo(int id, String serviceName, Map<String, String> parameters) {
        if(serviceName == null){
            throw new NullPointerException("Service name can not be null");
        }
        this.id = id;
        this.serviceName = serviceName;

        //make a defensive copy immutable
        Map params = new HashMap(parameters);
        this.parameters = Collections.unmodifiableMap(params);
    }

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
    public Map<String,String> getParameters(){
        return parameters;
    }
    
}
