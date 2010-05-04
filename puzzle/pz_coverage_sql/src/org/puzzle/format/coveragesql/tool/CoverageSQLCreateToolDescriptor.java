/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2010, Geomatys
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

package org.puzzle.format.coveragesql.tool;

import java.awt.Component;
import java.awt.Image;

import org.geotoolkit.internal.wizard.CoverageDatabaseWizard;

import org.netbeans.api.wizard.WizardDisplayer;

import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;
import org.puzzle.format.coveragesql.resources.CoverageSQLResource;

/**
 *
 * @author Johann Sorel
 */
public class CoverageSQLCreateToolDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().DATABASE_CREATE.getPath();

    private String title = CoverageSQLResource.getString("database_creation");

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
        final CoverageDatabaseWizard wizard = new CoverageDatabaseWizard();
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
