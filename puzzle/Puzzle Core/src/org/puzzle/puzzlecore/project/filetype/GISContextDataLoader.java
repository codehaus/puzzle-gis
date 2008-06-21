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

public class GISContextDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/giscontext+xml";
    private static final long serialVersionUID = 1L;

    public GISContextDataLoader() {
        super("org.puzzle.puzzlecore.project.filetype.GISContextDataObject");
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
        return new GISContextDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
