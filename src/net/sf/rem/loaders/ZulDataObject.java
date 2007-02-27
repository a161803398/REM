package net.sf.rem.loaders;

import java.io.IOException;
import org.netbeans.spi.xml.cookies.CheckXMLSupport;
import org.netbeans.spi.xml.cookies.DataObjectAdapters;
import org.netbeans.spi.xml.cookies.ValidateXMLSupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;
import org.xml.sax.InputSource;

public class ZulDataObject extends MultiDataObject {
    
    public ZulDataObject(FileObject pf, ZulDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        
        InputSource is = DataObjectAdapters.inputSource(this);
        cookies.add(new CheckXMLSupport(is));
        cookies.add(new ValidateXMLSupport(is));
    }
    
    protected Node createNodeDelegate() {
        return new ZulDataNode(this);
    }
    
}
