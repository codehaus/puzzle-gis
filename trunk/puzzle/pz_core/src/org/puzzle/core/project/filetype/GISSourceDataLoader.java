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
import org.openide.util.NbBundle;

/**
 * This class is a loader for the {@code GISSourceDataObject}.
 * It allows creating {@code GISSourceDataObject} from XML files representing
 * any kind of source.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @author  Thomas Bonavia (comments)
 * 
 * @see     org.openide.loaders.UniFileLoader
 */
public class GISSourceDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/gissource+xml";
    private static final long serialVersionUID = 1L;

    public GISSourceDataLoader() {
        super("org.puzzle.core.project.filetype.GISSourceDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(GISSourceDataLoader.class, "LBL_GISSource_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GISSourceDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
