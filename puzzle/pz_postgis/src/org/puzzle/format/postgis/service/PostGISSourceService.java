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

import org.openide.util.NbBundle;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.SourceCreationPane;
import org.puzzle.core.project.source.DistantSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.capabilities.FeatureStoreConversion;
import org.puzzle.core.project.source.capabilities.JExportPane;
import org.puzzle.core.project.source.capabilities.JImportPane;
import org.puzzle.format.postgis.ui.JPostGISDataPanel;

/**
 * PostGIS source service.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class PostGISSourceService extends AbstractGISSourceService implements DistantSourceCreation{

    public static final String SERVICE_ID = "PostGIS";

    public PostGISSourceService(){
        super();
        content.add(new PostGISConverter());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getIdentifier(){
        return SERVICE_ID;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException{
        return new PostGISSource(info);
    }


    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return NbBundle.getMessage(PostGISSourceService.class, "postgisTitle");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SourceCreationPane createPanel() {
        return new JPostGISDataPanel();
    }

    private class PostGISConverter implements FeatureStoreConversion{

        @Override
        public JImportPane createImportPane() {
            return new JPostGisImportPane();
        }

        @Override
        public JExportPane createExportPane() {
            return new JPostGisExportPane();
        }

    }

}
