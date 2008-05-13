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

package org.puzzle.shell.gdal.database.esriaccess;

import java.awt.Component;
import java.awt.Image;
import java.beans.BeanInfo;
import org.openide.util.NbBundle;
import org.puzzle.puzzlecore.tool.AbstractToolDescriptor;
import org.puzzle.shell.gdal.ENV;
import org.puzzle.shell.gdal.GDALProcessWidgetTool;
import org.puzzle.shell.misc.ShellTreePathConstants;

/**
 *
 * @author johann sorel
 */
public class ESRIAccessToolDescriptor extends AbstractToolDescriptor{
    
    private final String title = NbBundle.getMessage(ESRIAccessToolDescriptor.class, "title");
    
    public String getTitle() {
        return title;
    }

    @Override
    public String[] getPath() {
        return ShellTreePathConstants.getInstance().GDAL_SIMPLE.getPath();
    }

    public Component getComponent() {
        return new GDALProcessWidgetTool(new ESRIAccessTool());
    }

    @Override
    public Image getIcon(int type) {
        
        if(type == BeanInfo.ICON_COLOR_16x16){
            return ENV.ICON_COLOR_16;
        }else if(type == BeanInfo.ICON_COLOR_32x32){
            return ENV.ICON_COLOR_32;
        }else if(type == BeanInfo.ICON_MONO_16x16){
            return ENV.ICON_BW_16;
        }else if(type == BeanInfo.ICON_MONO_32x32){
            return ENV.ICON_BW_32;
        }
        
        return super.getIcon(type);
    }
    
}
