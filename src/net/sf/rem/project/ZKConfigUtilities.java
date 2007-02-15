/*
 * ZKConfigUtilities.java
 *
 * Created on February 15, 2007, 3:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.project;

import org.netbeans.modules.j2ee.dd.api.web.DDProvider;
import org.netbeans.modules.j2ee.dd.api.web.Servlet;
import org.netbeans.modules.j2ee.dd.api.web.WebApp;
import org.openide.filesystems.FileObject;

/**
 *
 * @author magic
 */
public class ZKConfigUtilities {
    
    public static Servlet getZKLoaderServlet(FileObject dd){
        if (dd == null) {
            return null;
        }
        try {
            WebApp webApp = DDProvider.getDefault().getDDRoot(dd);
            
            // Try to find according the servlet class name. 
            return (Servlet) webApp
                    .findBeanByName("Servlet", "ServletClass", "org.zkoss.zk.ui.http.DHtmlLayoutServlet"); //NOI18N;
        } catch (java.io.IOException e) {
            return null;
        }
    }
    
    public static Servlet getAuEngineServlet(FileObject dd){
        if (dd == null) {
            return null;
        }
        try {
            WebApp webApp = DDProvider.getDefault().getDDRoot(dd);
            
            // Try to find according the servlet class name. 
            return (Servlet) webApp
                    .findBeanByName("Servlet", "ServletClass", "org.zkoss.zk.au.http.DHtmlUpdateServlet"); //NOI18N;
        } catch (java.io.IOException e) {
            return null;
        }
    }
}
