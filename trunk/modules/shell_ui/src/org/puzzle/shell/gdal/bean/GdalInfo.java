/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.shell.gdal.bean;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.puzzle.shell.gdal.ENV;
import org.puzzle.shell.misc.runtime.ProcessObject;

/**
 * @author johann sorel
 */
public class GdalInfo extends AbstractGDALShellBean implements Serializable {

    public static final String PROP_IN_FILE = "inFile";
    public static final String PROP_MM = "mm";
    public static final String PROP_NOGCP = "nogcp";
    public static final String PROP_NOMD = "nomd";
    private File inFile = null;
    private boolean mm = false;
    private boolean nogcp = false;
    private boolean nomd = false;
    

    public GdalInfo() {
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

    public boolean isMm() {
        return mm;
    }

    public void setMm(boolean mm) {
        boolean old = this.mm;
        this.mm = mm;
        propertySupport.firePropertyChange(PROP_MM, old, this.mm);
        parseCommand();
    }

    public boolean isNogcp() {
        return nogcp;
    }

    public void setNogcp(boolean nogcp) {
        boolean old = this.nogcp;
        this.nogcp = nogcp;
        propertySupport.firePropertyChange(PROP_NOGCP, old, this.nogcp);
        parseCommand();
    }

    public boolean isNomd() {
        return nomd;
    }

    public void setNomd(boolean nomd) {
        boolean old = this.nomd;
        this.nomd = nomd;
        propertySupport.firePropertyChange(PROP_NOMD, old, this.nomd);
        parseCommand();
    }

    protected String[] createCommand() {
        List<String> cmds = new ArrayList<String>();

        cmds.add(ENV.getGDALINFOPath());
        if (mm) {
            cmds.add("-mm");
        }
        if (nogcp) {
            cmds.add("-nogcp");
        }
        if (nomd) {
            cmds.add("-nomd");
        }
        cmds.add((inFile == null) ? "" : inFile.getPath());

        return cmds.toArray(new String[cmds.size()]);
    } 

    public String getTitle() {
        return "GdalInfo";
    }
}
