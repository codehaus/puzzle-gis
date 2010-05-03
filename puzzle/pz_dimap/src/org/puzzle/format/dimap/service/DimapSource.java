/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2010, Johann Sorel
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
package org.puzzle.format.dimap.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.geotoolkit.coverage.WorldImageReaderUtilities;
import org.geotoolkit.coverage.io.CoverageStoreException;
import org.geotoolkit.coverage.io.GridCoverageReader;
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
 * This is a {@code GISSource} used to reference a Dimap file in
 * a {@link org.puzzle.core.project.GISProject}. Dimap files are :
 * <ul>
 *  <li>TIF.</li>
 * </ul>
 * 
 * @author Johann Sorel
 * @see org.puzzle.core.project.source.GISSource
 */
public class DimapSource extends GISSource{

    private static final File CACHE_FOLDER = new File("tileCache");

    static{
        CACHE_FOLDER.mkdirs();
    }

    private final File dimapImage;
    private final String name;
    private GridCoverageReader reader = null;

    /**
     * Constructor.
     * Initializes a {@code DimapSource} from the Dimap Image file.
     * @param file          The dimap file.
     * @param serviceName   The name of the service to use to manage this source.
     * @param id            The ID of the source.
     * @param parameters    The parameters (from the XML file).
     */
    public DimapSource(final GISSourceInfo info, final File file) {
        super(info);
        this.dimapImage = file;
        this.name = file.getName();
        content.add(new  DimapLayerCreation());
    }

    /** 
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/dimap/resources/dimap.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void unload() {
        reader = null;
        setState(GISSourceState.UNLOADED);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void load() {
        if(reader != null) return;

        final File cache = new File(CACHE_FOLDER.getAbsolutePath() + File.separator + name);
        cache.mkdirs();

        try {
            reader = WorldImageReaderUtilities.createSimpleReader(dimapImage);
        } catch (IOException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }
        catch (CoverageStoreException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        setState(GISSourceState.LOADED);
    }

    private class DimapLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            load();

            final MapLayer layer;
            if(reader != null){
                final MutableStyle style = RandomStyleFactory.createRasterStyle();
                layer = MapBuilder.createCoverageLayer(reader, style, DimapSource.this.name);
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,DimapSource.this);
            layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);
            layer.setDescription(new DefaultStyleFactory().description(name,"") );

            return layer;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            load();
            return new LayerCreationComponent(monitor, DimapSource.this, DimapSource.this.name);
        }
    }

}
