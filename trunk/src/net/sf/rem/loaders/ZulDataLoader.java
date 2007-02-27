package net.sf.rem.loaders;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class ZulDataLoader extends UniFileLoader {
    
    public static final String REQUIRED_MIME = "text/x-zul+xml";
    
    private static final long serialVersionUID = 1L;
    
    public ZulDataLoader() {
        super("net.sf.rem.loaders.ZulDataObject");
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(ZulDataLoader.class, "LBL_Zul_loader_name");
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new ZulDataObject(primaryFile, this);
    }
    
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
    
}
