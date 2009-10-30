/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.analyze.metadata;

import java.awt.Component;
import java.awt.Image;
import org.geotoolkit.gui.swing.image.ImageFileChooser;
import org.puzzle.analyze.resources.MessageBundle;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.core.windows.toolbox.tree.ToolTreeConstants;

/**
 *
 * @author eclesia
 */
public class ImageMetaDataToolDescriptor extends AbstractToolDescriptor{

    private final String[] path = ToolTreeConstants.getInstance().FILE.getPath();

    private String title = MessageBundle.getString("image_metadata");


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
        ImageFileChooser chooser = new ImageFileChooser("", true);
        chooser.showDialog(null, null);
        return null;
    }

    @Override
    public Image getIcon(int iconType) {
        return null;
    }

}
