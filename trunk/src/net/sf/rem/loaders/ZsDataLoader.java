package net.sf.rem.loaders;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class ZsDataLoader extends UniFileLoader {
    
    public static final String REQUIRED_MIME = "text/x-zs";
    
    private static final long serialVersionUID = 1L;
    
    public ZsDataLoader() {
        super("net.sf.rem.loaders.ZsDataObject");
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(ZsDataLoader.class, "LBL_Zs_loader_name");
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new ZsDataObject(primaryFile, this);
    }
    
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
    
}
