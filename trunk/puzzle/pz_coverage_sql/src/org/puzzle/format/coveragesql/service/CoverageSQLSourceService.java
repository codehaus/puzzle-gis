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
package org.puzzle.format.coveragesql.service;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.project.source.capabilities.DistantSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.coveragesql.resources.CoverageSQLResource;
import org.puzzle.format.coveragesql.ui.JCoverageSQLDataPanel;

/**
 * PostGIS source service.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class CoverageSQLSourceService extends AbstractGISSourceService implements DistantSourceCreation{

    public static final String SERVICE_ID = "CoverageSQL";

    public CoverageSQLSourceService(){
        super();
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
        return new CoverageSQLSource(info);
    }


    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return CoverageSQLResource.getString("coveragesqlTitle");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SourceCreationPane createPanel() {
        return new JCoverageSQLDataPanel();
    }

}
