/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.project.filetype;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class GISSourceDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/gissource+xml";
    private static final long serialVersionUID = 1L;

    public GISSourceDataLoader() {
        super("org.puzzle.puzzlecore.project.filetype.GISSourceDataObject");
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

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new GISSourceDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
