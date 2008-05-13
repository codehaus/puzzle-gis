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

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.puzzle.shell.gdal.ENV;

/**
 * @author johann sorel
 */
public class Ogr2ogr extends AbstractGDALShellBean implements Serializable {

    public static enum FORMAT {
        DGN("DGN"),
        GML("GML"),
        KML("KML"),
        MAPINFO("MapInfo File"),
        SHAPE("ESRI Shapefile");
            
        private final String sh;
        FORMAT(String sh){
            this.sh = sh;
        }
        
        String getShellValue(){
            return sh;
        }
                
    };
    
    public static final String PROP_IN_FILE = "inFile";
    public static final String PROP_OUT_FILE = "outFile";
    public static final String PROP_OUT_FORMAT = "outFormat";
    private File inFile = null;
    private File outFile = null;
    private FORMAT outFormat = FORMAT.GML;

    public Ogr2ogr() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public File getInFile() {
        return inFile;
    }

    public void setInFile(File inFile) {
        File old = this.inFile;
        this.inFile = inFile;
        propertySupport.firePropertyChange(PROP_IN_FILE, old, this.inFile);
        parseCommand();
    }

    public File getOutFile() {
        return outFile;
    }

    public void setOutFile(File outFile) {
        File old = this.outFile;
        this.outFile = outFile;
        propertySupport.firePropertyChange(PROP_OUT_FILE, old, this.outFile);
        parseCommand();
    }

    public FORMAT getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(FORMAT outFormat) {        
        FORMAT old = this.outFormat;
        this.outFormat = outFormat;
        propertySupport.firePropertyChange(PROP_OUT_FILE, old, this.outFormat);
        parseCommand();
    }

    public String getTitle() {
        return "Ogr2ogr";
    }

    @Override
    protected String[] createCommand() {
        List<String> cmds = new ArrayList<String>();

        cmds.add(ENV.getOGR2OGRPath());
        cmds.add("-f");
        cmds.add(outFormat.getShellValue());
        cmds.add( (outFile == null) ? "" : outFile.getPath() );
        cmds.add( (inFile == null) ? "" : inFile.getPath() );
        
        return cmds.toArray(new String[cmds.size()]);
    }
}
