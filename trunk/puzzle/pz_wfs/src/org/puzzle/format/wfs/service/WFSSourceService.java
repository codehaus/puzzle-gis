/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Johann Sorel
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
package org.puzzle.format.wfs.service;

import org.geotoolkit.data.wfs.WFSDataStoreFactory;
import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.capabilities.SourceCreationPane;
import org.puzzle.core.project.source.capabilities.DistantSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.wfs.resources.WFSResource;

/**
 * WFS source service.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WFSSourceService extends AbstractGISSourceService implements DistantSourceCreation{

    public static final String SERVICE_ID = "WebFeatureServer";
    public static final String URL_PROP = WFSDataStoreFactory.SERVER_URI.getName().getCode();

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
        return new WFSSource(info);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return WFSResource.getString("wfsTitle");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SourceCreationPane createPanel() {
        return new WFSCreationPane();
    }
}
