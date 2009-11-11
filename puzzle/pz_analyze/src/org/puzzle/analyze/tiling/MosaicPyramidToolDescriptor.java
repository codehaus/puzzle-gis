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

package org.puzzle.analyze.tiling;

import java.awt.Component;
import java.awt.Image;
import org.geotoolkit.internal.wizard.MosaicWizard;
import org.netbeans.api.wizard.WizardDisplayer;
import org.puzzle.analyze.resources.AnalyzeResource;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class MosaicPyramidToolDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE_CREATE.getPath();

    private String title = AnalyzeResource.getString("mosaic_creation");

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
        final MosaicWizard wizard = new MosaicWizard();
            WizardDisplayer.showWizard(wizard.createWizard());
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
