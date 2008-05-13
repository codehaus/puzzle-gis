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

package org.puzzle.shell.gdal.bean;

import java.awt.Image;
import java.beans.BeanInfo;
import org.openide.util.Utilities;
import org.puzzle.shell.gui.AbstractShellBean;
import org.puzzle.shell.misc.ShellTreePathConstants;
import org.puzzle.shell.misc.runtime.ProcessObject;


/**
 *
 * @author johann sorel
 */
public abstract class AbstractGDALShellBean extends AbstractShellBean {

    private final Image ICON_COLOR_32 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_color_32.png");
    private final Image ICON_COLOR_16 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_color_16.png");
    private final Image ICON_BW_32 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_bw_32.png");
    private final Image ICON_BW_16 = Utilities.loadImage("org/puzzle/shell/gdal/gdal_bw_16.png");
    
    protected String command = null;
    
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

    @Override
    public String[] getPath() {
        return ShellTreePathConstants.getInstance().GDAL.getPath();
    }
    
    protected abstract String[] createCommand();
    
    protected void parseCommand() {
        String old = command;

        String[] parameters = createCommand();

        StringBuilder builder = new StringBuilder();
        for (String str : parameters) {
            builder.append('[');
            builder.append(str);
            builder.append("] ");
        }

        command = builder.toString();

        propertySupport.firePropertyChange(PROP_COMMAND, old, command);
    }
    
    public String getCommand() {
        return command;
    }
    
    public ProcessObject createProcess() {
        ProcessObject po = null;
        String[] parameters = createCommand();
        po = new ProcessObject(parameters, getTitle());
        return po;
    }
    
}
