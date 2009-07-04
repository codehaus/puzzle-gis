/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.analyze.tiling;

import java.awt.Component;
import java.awt.Image;
import org.puzzle.core.tool.ToolDescriptor;
import org.geotoolkit.internal.wizard.MosaicWizard;
import org.netbeans.api.wizard.WizardDisplayer;
import org.openide.util.NbBundle;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;

/**
 *
 * @author eclesia
 */
public class MosaicPyramidToolDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE_CREATE.getPath();

    private String title = NbBundle.getMessage(MosaicPyramidToolDescriptor.class, "mosaic_creation");


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getPath() {
        return path;
    }

    @Override
    public Component getComponent() {
        final MosaicWizard wizard = new MosaicWizard();
            WizardDisplayer.showWizard(wizard.createWizard());
            return null;
    }

    @Override
    public Image getIcon(int iconType) {
        return null;
    }

}
