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

import java.util.Map;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.geotools.gui.swing.datachooser.JPostGISDataPanel;
import org.puzzle.core.project.source.GISDistantSourceService;
import org.puzzle.core.project.source.GISSource;

/**
 *
 * @author  Johann Sorel
 */
public class PostGISSourceService implements GISDistantSourceService{

    private static final String TITLE = "PostGIS";
    
    public String getIdentifier(){
        return "PostGIS";
    }
    
    public GISSource restoreSource(Map<String, String> parameters, int id) throws IllegalArgumentException{
        final String url = parameters.get("url");
        
        if(url == null) throw new IllegalArgumentException("missing parameter url");
        
        GISSource postgisSource = new PostGISSource(getIdentifier(),id,parameters);
        return postgisSource;
    }

    public DataPanel createDataPanel() {
        return new JPostGISDataPanel();
    }

    public GISSource createSource(Map parameters) throws IllegalArgumentException {
        return null;
    }

    public String getTitle() {
        return TITLE;
    }
}
