/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.core.project.filetype;

import java.io.IOException;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.puzzle.core.resources.MessageBundle;

/**
 * This class is a loader for the {@code GISContextDataObject}.
 * It allows creating {@code GISContextDataObject} from XML files representing
 * a {@code MapContext}.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
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
        return MessageBundle.getString("LBL_GISContext_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GISContextDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
