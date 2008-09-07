/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.core.project.filetype;

import java.io.IOException;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;
import org.puzzle.core.project.GISProject;

/**
 * This class is a loader for the {@code GISContextDataObject}.
 * It allows creating {@code GISContextDataObject} from XML files representing
 * a {@code MapContext}.
 * 
 * @author  Johann Sorel
 * @author  Thomas Bonavia  (comments)
 * 
 * @see     org.openide.loaders.UniFileLoader
 */
public class GISContextDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/giscontext+xml";
    private static final long serialVersionUID = 1L;

    public GISContextDataLoader() {
        super("org.puzzle.core.project.filetype.GISContextDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(GISContextDataLoader.class, "LBL_GISContext_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        GISContextDataObject dObject = new GISContextDataObject(primaryFile, this);
//        GISProject prj = (GISProject)FileOwnerQuery.getOwner(dObject.getPrimaryFile());

//        prj.addContext(dObject.getContext());
        
        return dObject;
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
