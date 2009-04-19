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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Information of a GIS view, this hold the ID and every parameters
 * needed to save and restore the GIS view.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public final class GISViewInfo {

    public static final String TITLE_PROPERTY = "title";
    public static final String PARAMETERS_PROPERTY = "parameters";

    public static final int UNREGISTERED_ID = -1;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final int id;
    private final String serviceName;
    private final Map<String,String> parameters = new HashMap<String, String>(){

        @Override
        public String put(String key, String value) {
            Map<String,String> old = new HashMap<String, String>(parameters);
            String put = super.put(key, value);
            support.firePropertyChange(PARAMETERS_PROPERTY, old, parameters);
            return put;
        }

        @Override
        public void clear() {
            Map<String,String> old = new HashMap<String, String>(parameters);
            super.clear();
            support.firePropertyChange(PARAMETERS_PROPERTY, old, parameters);
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {
            Map<String,String> old = new HashMap<String, String>(parameters);
            super.putAll(m);
            support.firePropertyChange(PARAMETERS_PROPERTY, old, parameters);
        }

        @Override
        public String remove(Object key) {
            Map<String,String> old = new HashMap<String, String>(parameters);
            String rm = super.remove(key);
            support.firePropertyChange(PARAMETERS_PROPERTY, old, parameters);
            return rm;
        }
        
    };

    private String title = "";

    public GISViewInfo(int id, String serviceName, String title, Map<String, String> parameters) {
        if(serviceName == null){
            throw new NullPointerException("Service name can not be null");
        }
        this.id = id;
        this.serviceName = serviceName;
        this.title = title;
        this.parameters.putAll(parameters);
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
    public Map<String,String> parameters(){
        return parameters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        support.firePropertyChange(TITLE_PROPERTY, old, title);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        support.removePropertyChangeListener(listener);
    }

}
