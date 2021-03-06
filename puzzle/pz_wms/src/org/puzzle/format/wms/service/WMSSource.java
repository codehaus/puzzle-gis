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
package org.puzzle.format.wms.service;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.geotoolkit.wms.WebMapServer;
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
        return ImageUtilities.loadImage("org/puzzle/format/wms/resources/wms.png");
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

        final Map<String,Serializable> infosParams = getInfo().getParameters();
        final String url = infosParams.get(WMSSourceService.URL_PROP).toString();
        final String version = infosParams.get(WMSSourceService.VERSION_PROP).toString();

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
