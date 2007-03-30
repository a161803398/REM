/*
 * ZulGrammarQueryManager.java
 *
 * Created on February 27, 2007, 10:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.loaders;

import java.beans.FeatureDescriptor;
import java.util.Enumeration;
import org.netbeans.api.xml.services.UserCatalog;
import org.netbeans.modules.xml.api.model.DTDUtil;
import org.netbeans.modules.xml.api.model.GrammarEnvironment;
import org.netbeans.modules.xml.api.model.GrammarQuery;
import org.openide.util.Enumerations;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author magic
 */
public class ZulGrammarQueryManager extends org.netbeans.modules.xml.api.model.GrammarQueryManager {
    
    /** Creates a new instance of ZulGrammarQueryManager */
    public ZulGrammarQueryManager() {
    }

    public Enumeration enabled(GrammarEnvironment grammarEnvironment) {
        return Enumerations.empty();
    }

    public GrammarQuery getGrammar(GrammarEnvironment grammarEnvironment) {
        UserCatalog catalog = UserCatalog.getDefault();
        if (catalog != null) {
            EntityResolver resolver = catalog.getEntityResolver();
            if (resolver != null) {
                try {
                    String schema = ZulCatalog.ZUL_ID_2_3_0;
                    InputSource inputSource = resolver.resolveEntity(schema, null);
                    if (inputSource!=null) {
                        // parse DTD file
                        return DTDUtil.parseDTD(true, inputSource);
                    }
                } catch(SAXException e) {
                    e.printStackTrace(System.out);
                } catch(java.io.IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return null;
    }

    public FeatureDescriptor getDescriptor() {
        return new java.beans.FeatureDescriptor();
    }
    
}
