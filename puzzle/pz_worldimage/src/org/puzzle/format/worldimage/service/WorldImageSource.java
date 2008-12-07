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
package org.puzzle.format.worldimage.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.geotools.coverage.io.CoverageReader;
import org.geotools.coverage.wi.WorldImageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapLayer;
import org.geotools.map.MapBuilder;
import org.geotools.style.MutableStyle;
import org.geotools.style.RandomStyleFactory;

import org.opengis.referencing.operation.NoninvertibleTransformException;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.JLayerChooser;
import org.puzzle.core.project.source.LayerChooserMonitor;
import org.puzzle.core.project.source.LayerSource;
import org.puzzle.core.project.source.PZLayerConstants;
import org.puzzle.core.project.GISProject;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;

/**
 * This is a {@code GISSource} used to reference a World Image file in
 * a {@link org.puzzle.core.project.GISProject}. World Image files are :
 * <ul>
 *  <li>JPEG,</li>
 *  <li>PNG,</li>
 *  <li>BMP.</li>
 * </ul>
 * 
 * @author  Thomas Bonavia
 * 
 * @see     org.puzzle.core.project.source.GISSource
 */
public class WorldImageSource extends GISSource{

    private static final File CACHE_FOLDER = new File("tileCache");

    static{
        CACHE_FOLDER.mkdirs();
    }
    
    private final String name;
    private CoverageReader reader = null;

    /**
     * Constructor.
     * Initializes a {@code WorldImageSource} from the World Image file.
     * @param worldImage    The world image file.
     * @param serviceName   The name of the service to use to manage this source.
     * @param id            The ID of the source.
     * @param parameters    The parameters (from the XML file).
     */
    public WorldImageSource(final GISSourceInfo info, final File worldImage) {
        super(info);
        this.name = worldImage.getName();

        final WorldImageFactory factory = new WorldImageFactory();
        final File cache = new File(CACHE_FOLDER.getAbsolutePath() + File.separator + name);
        cache.mkdirs();

        try {
            reader = factory.createMosaicReader(worldImage, 512, cache);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoninvertibleTransformException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    /** {@inheritDoc } */
    @Override
    public MapLayer createLayer(Map<String, String> parameters) {
        final MutableStyle style = new RandomStyleFactory().createRasterStyle();
        final LayerSource source = new LayerSource(getInfo().getID(), parameters,this);
        final MapLayer layer = MapBuilder.getInstance().createCoverageLayer(reader, style, name);
        layer.setUserPropertie(PZLayerConstants.KEY_LAYER_INFO, source);
        layer.setDescription(CommonFactoryFinder.getStyleFactory(null).createDescription(name,"") );
        return layer;
    }

    /** {@inheritDoc } */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/worldimage/worldimage.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JLayerChooser createChooser(LayerChooserMonitor monitor) {
        return new LayerCreationComponent(monitor, this,name);
    }
    
}
