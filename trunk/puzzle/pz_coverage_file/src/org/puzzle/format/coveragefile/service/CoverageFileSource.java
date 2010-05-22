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
package org.puzzle.format.coveragefile.service;

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
 * This is a {@code GISSource} used to reference a coverage files in
 * a {@link org.puzzle.core.project.GISProject}. Potential coverage files
 * are found using the available ImageReaders.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @see     org.puzzle.core.project.source.GISSource
 */
public class CoverageFileSource extends GISSource{

    private static final File CACHE_FOLDER = new File("tileCache");

    static{
        CACHE_FOLDER.mkdirs();
    }

    private final File imageFile;
    private final String name;
    private GridCoverageReader reader = null;

    /**
     * Constructor.
     * Initializes a {@code CoverageFileSource} from a file.
     * @param worldImage    The image file.
     * @param GISSourceInfo Details of the source.
     */
    public CoverageFileSource(final GISSourceInfo info, final File worldImage) {
        super(info);
        this.imageFile = worldImage;
        this.name = worldImage.getName();
        content.add(new  CoverageFileLayerCreation());
    }

    /** 
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/coveragefile/resources/image.png");
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
            reader = WorldImageReaderUtilities.createSimpleReader(imageFile);
            //reader = WorldImageReaderUtilities.createMosaicReader(worldImage, 512, cache);
        } catch (IOException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }
//        catch (NoninvertibleTransformException ex) {
//            setState(GISSourceState.LOADING_ERROR);
//            Exceptions.printStackTrace(ex);
//            return;
//        }
        catch (CoverageStoreException ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }

        setState(GISSourceState.LOADED);
    }

    private class CoverageFileLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            load();

            final MapLayer layer;
            if(reader != null){
                final MutableStyle style = RandomStyleFactory.createRasterStyle();
                layer = MapBuilder.createCoverageLayer(reader, style, CoverageFileSource.this.name);
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,CoverageFileSource.this);
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
            return new LayerCreationComponent(monitor, CoverageFileSource.this, CoverageFileSource.this.name);
        }
    }

}
