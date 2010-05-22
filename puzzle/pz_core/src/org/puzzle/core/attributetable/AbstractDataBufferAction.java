/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.core.attributetable;

import java.lang.ref.WeakReference;
import org.geotoolkit.gui.swing.propertyedit.JFeaturePanelAction;
import org.geotoolkit.map.FeatureMapLayer;
import org.opengis.filter.Filter;

/**
 * Acts as a buffer to copie datas from one source to another.
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class AbstractDataBufferAction extends JFeaturePanelAction{

    private static WeakReference<FeatureMapLayer> bufferLayer = null;
    private static Filter bufferFilter = null;

    protected static Filter getFilter(){
        return bufferFilter;
    }

    protected static void setFilter(Filter filter){
        bufferFilter = filter;
    }

    protected static FeatureMapLayer getLayer(){
        if(bufferLayer == null) return null;
        FeatureMapLayer layer = bufferLayer.get();
        return layer;
    }

    protected static void setLayer(FeatureMapLayer layer){
        bufferLayer = new WeakReference<FeatureMapLayer>(layer);
    }

}
