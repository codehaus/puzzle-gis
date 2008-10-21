/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gdal.database.postgis;

import java.awt.Component;
import java.awt.Image;
import java.beans.BeanInfo;
import java.util.ResourceBundle;
import org.openide.util.Utilities;
import org.puzzle.core.tool.AbstractToolDescriptor;
import org.puzzle.shell.gdal.GDALProcessWidgetTool;
import org.puzzle.shell.misc.ShellTreePathConstants;

/**
 *
 * @author johann sorel
 */
public class PostGISToolDescriptor2 extends AbstractToolDescriptor{

    private final Image ICON_COLOR_32 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_color_32.png");
    private final Image ICON_COLOR_16 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_color_16.png");
    private final Image ICON_BW_32 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_bw_32.png");
    private final Image ICON_BW_16 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_bw_16.png");
    
    private final String title = ResourceBundle.getBundle("org/puzzle/shell/gdal/database/postgis/Bundle").getString("title2");
    
    
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getPath() {
        return ShellTreePathConstants.getInstance().GDAL_SIMPLE.getPath();
    }

    public Component getComponent() {
        return new GDALProcessWidgetTool(new PostGISTool2());
    }

    @Override
    public Image getIcon(int type) {
        
        if(type == BeanInfo.ICON_COLOR_16x16){
            return ICON_COLOR_16;
        }else if(type == BeanInfo.ICON_COLOR_32x32){
            return ICON_COLOR_32;
        }else if(type == BeanInfo.ICON_MONO_16x16){
            return ICON_BW_16;
        }else if(type == BeanInfo.ICON_MONO_32x32){
            return  ICON_BW_32;
        }
        
        return super.getIcon(type);
    }
    
}
