/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2008-2009, Johann Sorel, Thomas Bonavia
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
package org.puzzle.format.geotiff.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotoolkit.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.util.RandomStyleFactory;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceState;
import org.puzzle.core.project.source.capabilities.LayerCreation;

/**
 * This is a {@code GISSource} used to reference a Geotiff file in
 * a {@link org.puzzle.core.project.GISProject}.
 * 
 * @author  Thomas Bonavia
 * @author  Johann Sorel (Puzzle-GIS)
 * 
 * @see     org.puzzle.core.project.source.GISSource
 */
public class GeoTiffSource extends GISSource{
    
    private final String name;
    private final File geotiff;
    private GridCoverage2D gc2d = null;

    /**
     * Constructor.
     * Initializes a {@code GeoTiffSource} from the Geotiff file.
     * @param geotiff       The geotiff file.
     * @param info          save/restore informations.
     */
    GeoTiffSource(final GISSourceInfo info, File geotiff){
        super(info);
        this.geotiff = geotiff;
        this.name = geotiff.getName();

        content.add(new GeoTiffLayerCreation());
        
    }
    
    /** {@inheritDoc } */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/geotiff/geotiff.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void unload() {
        gc2d = null;
        setState(GISSourceState.UNLOADED);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void load() {
        if(gc2d != null) return;

        GeoTiffReader reader;
        try {
            reader = new GeoTiffReader(geotiff);
            gc2d = (GridCoverage2D)reader.read(null);
        } catch (DataSourceException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        } catch (IOException ex){
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }
        
        setState(GISSourceState.LOADED);
    }

    private class GeoTiffLayerCreation implements LayerCreation{

        /** {@inheritDoc } */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            if(parameters == null)parameters = Collections.emptyMap();
            load();

            final MapLayer layer;

            if(gc2d != null){
                final MutableStyle style = new RandomStyleFactory().createRasterStyle();
                layer = MapBuilder.createCoverageLayer(gc2d, style, name);
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,GeoTiffSource.this);
            layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);
            layer.setDescription(new DefaultStyleFactory().description(name,"") );

            return layer;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            return new LayerCreationComponent(monitor, this,name);
        }
    }
}
