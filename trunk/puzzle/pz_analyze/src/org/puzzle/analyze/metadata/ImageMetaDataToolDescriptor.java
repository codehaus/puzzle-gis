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

package org.puzzle.analyze.metadata;

import java.awt.Component;
import java.awt.Image;
import org.geotoolkit.gui.swing.image.ImageFileChooser;
import org.puzzle.analyze.resources.AnalyzeResource;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;

/**
 * Tool which display an explorer. This explorer allow visualisation
 * of metadatas of coverags and a preview.
 * @author Johann Sorel
 */
public class ImageMetaDataToolDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE.getPath();

    private String title = AnalyzeResource.getString("image_metadata");

    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getPath() {
        return path;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getComponent() {
        final ImageFileChooser chooser = new ImageFileChooser("", true);
        chooser.showDialog(null, null);
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image getIcon(int iconType) {
        return null;
    }

}
