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

import org.puzzle.core.project.source.SourceCreationPane;
import org.puzzle.core.project.source.GISDistantSourceService;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.postgis.ui.JPostGISDataPanel;

/**
 *
 * @author  Johann Sorel (Puzzle-GIS)
 */
public class PostGISSourceService implements GISDistantSourceService{

    private static final String TITLE = "PostGIS";
    
    @Override
    public String getIdentifier(){
        return "PostGIS";
    }
    
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException{
//        final String url = parameters.get("url");
//
//        if(url == null) throw new IllegalArgumentException("missing parameter url");
//
//        GISSource postgisSource = new PostGISSource(getIdentifier(),id,parameters);
        return null;
    }

    @Override
    public GISSource createSource(Map parameters) throws IllegalArgumentException {
        return null;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public SourceCreationPane createPanel() {
        return new JPostGISDataPanel();
    }
}
