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
package org.puzzle.core.project.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Information of a GIS view, this hold the ID and every parameters
 * needed to save and restore the GIS view.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class GISViewInfo {

    public static final int UNREGISTERED_ID = -1;

    private final int id;
    private final String serviceName;
    private final Map<String,String> parameters;

    public GISViewInfo(int id, String serviceName, Map<String, String> parameters) {
        if(serviceName == null){
            throw new NullPointerException("Service name can not be null");
        }
        this.id = id;
        this.serviceName = serviceName;

        //make a defensive copy immutable
        final Map<String,String> params = new HashMap<String,String>(parameters);
        this.parameters = Collections.unmodifiableMap(params);
    }

    /**
     * Returns the ID of the view, unique in the current project.
     */
    public int getID(){
        return id;
    }

    /**
     * Get the {@code RenderingService} identifier.
     * @return  A {@code String} containing the name id of the engine service.
     */
    public String getServiceName(){
        return serviceName;
    }
    
    /**
     * The parameters describing the {@code GISView}.
     * This parameters are retrieved from the XML context.
     * @return A {@code Map} containing all parameters.
     */
    public Map<String,String> getParameters(){
        return parameters;
    }
    
}
