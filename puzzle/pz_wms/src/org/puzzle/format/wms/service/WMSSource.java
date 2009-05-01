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

package org.puzzle.format.wms.service;

import java.awt.Image;
import java.net.URL;
import java.util.Map;

import org.geotoolkit.wms.WebMapServer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotoolkit.map.MapBuilder;
import org.geotoolkit.map.MapLayer;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.wms.map.WMSMapLayer;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.core.project.source.GISSourceState;
import org.puzzle.core.project.source.capabilities.JLayerChooser;
import org.puzzle.core.project.source.capabilities.LayerChooserMonitor;
import org.puzzle.core.project.source.GISLayerSource;
import org.puzzle.core.project.source.capabilities.LayerCreation;

/**
 * PostGIS source object.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WMSSource extends GISSource{

    public static final String LAYERS_PROP = "layers";

    private WebMapServer server = null;
    
    WMSSource(final GISSourceInfo info){
        super(info);
        content.add(new WMSLayerCreation());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/puzzle/format/wms/wms.png");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void unload() {
        server = null;
        setState(GISSourceState.UNLOADED);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void load() {
        if(server != null) return;

        final Map<String,String> infosParams = getInfo().getParameters();
        final String url = infosParams.get(WMSSourceService.URL_PROP);
        final String version = infosParams.get(WMSSourceService.VERSION_PROP);

        WebMapServer temp = null;
        try {
            temp = new WebMapServer(new URL(url),version);
        } catch (Exception ex) {
            setState(GISSourceState.LOADING_ERROR);
            Exceptions.printStackTrace(ex);
            return;
        }
        server = temp;
        setState(GISSourceState.LOADED);
        

    }

    private class WMSLayerCreation implements LayerCreation{

        /**
         * {@inheritDoc }
         */
        @Override
        public MapLayer createLayer(Map<String, String> parameters) {
            load();

            final String featureName = parameters.get(LAYERS_PROP);
            final MapLayer layer;
            if(server != null){
                layer = new WMSMapLayer(server,featureName);
            }else{
                layer = MapBuilder.createEmptyMapLayer();
            }

            final GISLayerSource source = new GISLayerSource(getInfo().getID(), parameters,WMSSource.this);
            layer.setUserPropertie(GISLayerSource.KEY_LAYER_INFO, source);
            layer.setDescription(new DefaultStyleFactory().description(featureName,"") );

            return layer;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public JLayerChooser createChooser(LayerChooserMonitor monitor) {
            load();
            return new LayerCreationComponent(monitor, server, WMSSource.this);
        }
    }

}
