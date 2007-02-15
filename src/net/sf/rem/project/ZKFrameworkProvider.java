/*
 * ZKFrameworkProvider.java
 *
 * Created on February 7, 2007, 10:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Set;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.libraries.Library;
import org.netbeans.api.project.libraries.LibraryManager;
import org.netbeans.modules.j2ee.dd.api.common.InitParam;
import org.netbeans.modules.j2ee.dd.api.web.DDProvider;
import org.netbeans.modules.j2ee.dd.api.web.Listener;
import org.netbeans.modules.j2ee.dd.api.web.MimeMapping;
import org.netbeans.modules.j2ee.dd.api.web.Servlet;
import org.netbeans.modules.j2ee.dd.api.web.ServletMapping;
import org.netbeans.modules.j2ee.dd.api.web.SessionConfig;
import org.netbeans.modules.j2ee.dd.api.web.WebApp;
import org.netbeans.modules.j2ee.dd.api.web.WelcomeFileList;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.spi.webmodule.FrameworkConfigurationPanel;
import org.netbeans.modules.web.spi.webmodule.WebFrameworkProvider;
import org.netbeans.spi.java.project.classpath.ProjectClassPathExtender;
import org.openide.DialogDescriptor;
import org.openide.ErrorManager;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.util.NbBundle;

/**
 *
 * @author magic
 */
public class ZKFrameworkProvider extends WebFrameworkProvider {
    
    /** Creates a new instance of ZKFrameworkProvider */
    public ZKFrameworkProvider() {
        super(NbBundle.getMessage(ZKFrameworkProvider.class,"ZK_Name"),
                NbBundle.getMessage(ZKFrameworkProvider.class,"ZK_Description"));
    }
    
    // Add ZK library and change config files
    public Set extend(WebModule webModule) {
        FileObject fo = webModule.getDocumentBase();
        Project project = FileOwnerQuery.getOwner(fo);
        
        Library lib = LibraryManager.getDefault().getLibrary("ZK");
        if(lib!=null){
            // add ZK library to project classpath
            ProjectClassPathExtender cpExtender = (ProjectClassPathExtender)project.getLookup
                    ().lookup(ProjectClassPathExtender.class);
            if(cpExtender !=null){
                try {
                    cpExtender.addLibrary(lib);
                } catch (IOException ex) {
                    ErrorManager.getDefault().notify(ex);
                }
            } else {
                ErrorManager.getDefault().log("WebProjectClassPathExtender not found in the project lookup of project: "
                        + project.getProjectDirectory().getPath());    //NOI18N
            }
            
            // update web config files
            try {
                FileSystem fs = webModule.getWebInf().getFileSystem();
                fs.runAtomicAction(new CreateZKConfig(webModule));
                
            } catch (FileNotFoundException exc) {
                ErrorManager.getDefault().notify(exc);
                return null;
            } catch (IOException exc) {
                ErrorManager.getDefault().notify(exc);
                return null;
            }
        }
        return null;
    }
    
    public boolean isInWebModule(WebModule webModule) {
        FileObject dd = webModule.getDeploymentDescriptor();
        return (dd!=null && ZKConfigUtilities.getZKLoaderServlet(dd)!=null
                && ZKConfigUtilities.getAuEngineServlet(dd)!=null);
    }
    
    public File[] getConfigurationFiles(WebModule webModule) {
        return null;
    }
    
    public FrameworkConfigurationPanel getConfigurationPanel(WebModule webModule) {
        return null;
    }
    
    private static String readResource(InputStream is, String encoding) throws IOException {
        // read the config from resource first
        StringBuffer sb = new StringBuffer();
        String lineSep = System.getProperty("line.separator");//NOI18N
        BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(lineSep);
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }
    
    private class CreateZKConfig implements FileSystem.AtomicAction {
        WebModule wm;
        
        public CreateZKConfig(WebModule wm){
            this.wm = wm;
        }
        
        public void run() throws IOException {
            // Enter servlet into the deployment descriptor
            FileObject dd = wm.getDeploymentDescriptor();
            WebApp ddRoot = DDProvider.getDefault().getDDRoot(dd);
            if(ddRoot!=null){
                try {
                    // add listener
                    Listener listener = (Listener)ddRoot.createBean("Listener");
                    listener.setDescription("Used to cleanup when a session is destroyed");
                    listener.setDisplayName("ZK Session Cleaner");
                    listener.setListenerClass("org.zkoss.zk.ui.http.HttpSessionListener");
                    
                    ddRoot.addListener(listener);
                    
                    // add zkLoader servlet
                    Servlet zkLoader = (Servlet)ddRoot.createBean("Servlet");
                    zkLoader.setDescription("ZK loader for ZUML pages");
                    zkLoader.setServletName("zkLoader");
                    zkLoader.setServletClass("org.zkoss.zk.ui.http.DHtmlLayoutServlet");
                    
                    ddRoot.addServlet(zkLoader);
                    
                    InitParam param = (InitParam)zkLoader.createBean("InitParam");
                    param.setParamName("update-uri");
                    param.setParamValue("/zkau");
                    zkLoader.addInitParam(param);
                    zkLoader.setLoadOnStartup(new BigInteger("1"));
                    
                    // add servlet mapping for zkLoader
                    ServletMapping mapping = (ServletMapping)ddRoot.createBean("ServletMapping");
                    mapping.setServletName("zkLoader");
                    mapping.setUrlPattern("*.zul");
                    
                    ddRoot.addServletMapping(mapping);
                    
                    mapping = (ServletMapping)ddRoot.createBean("ServletMapping");
                    mapping.setServletName("zkLoader");
                    mapping.setUrlPattern("*.zhtml");
                    
                    ddRoot.addServletMapping(mapping);
                    
                    // add auEngine serlvet
                    Servlet auEngine = (Servlet)ddRoot.createBean("Servlet");
                    auEngine.setDescription("The asynchronous update engine for ZK");
                    auEngine.setServletName("auEngine");
                    auEngine.setServletClass("org.zkoss.zk.au.http.DHtmlUpdateServlet");
                    
                    ddRoot.addServlet(auEngine);
                    
                    // add servlet mapping for auEngine
                    mapping = (ServletMapping)ddRoot.createBean("ServletMapping");
                    mapping.setServletName("auEngine");
                    mapping.setUrlPattern("/zkau/*");
                    
                    ddRoot.addServletMapping(mapping);
                    
                    // add session config
                    SessionConfig sessionConfig = (SessionConfig)ddRoot.createBean("SessionConfig");
                    sessionConfig.setSessionTimeout(new BigInteger("120"));
                    
                    ddRoot.setSessionConfig(sessionConfig);
                    
                    // add MIME mapping
                    MimeMapping mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("doc");
                    mime.setMimeType("application/vnd.ms-word");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("gif");
                    mime.setMimeType("image/gif");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("htm");
                    mime.setMimeType("text/html");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("html");
                    mime.setMimeType("text/html");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("jnlp");
                    mime.setMimeType("application/x-java-jnlp-file");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("jpeg");
                    mime.setMimeType("image/jpeg");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("jpg");
                    mime.setMimeType("image/jpeg");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("js");
                    mime.setMimeType("application/x-javascript");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("pdf");
                    mime.setMimeType("application/pdf");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("png");
                    mime.setMimeType("image/png");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("txt");
                    mime.setMimeType("text/plain");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("xls");
                    mime.setMimeType("application/vnd.ms-excel");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("xml");
                    mime.setMimeType("text/xml");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("xul");
                    mime.setMimeType("application/vnd.mozilla.xul-xml");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("zhtml");
                    mime.setMimeType("text/html");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("zip");
                    mime.setMimeType("application/x-zip");
                    ddRoot.addMimeMapping(mime);
                    
                    mime = (MimeMapping)ddRoot.createBean("MimeMapping");
                    mime.setExtension("zul");
                    mime.setMimeType("text/html");
                    ddRoot.addMimeMapping(mime);
                    
                    // add welcome file list
                    WelcomeFileList list = (WelcomeFileList)ddRoot.createBean("WelcomeFileList");
                    list.addWelcomeFile("index.zul");
                    list.addWelcomeFile("index.zhtml");
                    list.addWelcomeFile("index.html");
                    list.addWelcomeFile("index.htm");
                    
                    ddRoot.setWelcomeFileList(list);
                    
                    ddRoot.write(dd);
                } catch (ClassNotFoundException ex) {
                    ErrorManager.getDefault().notify(ex);
                }
                
                // copy index.zul
                if(canCreateNewFile(wm.getDocumentBase(),"index.zul")){
                    String content = readResource(Repository.getDefault().
                            getDefaultFileSystem().findResource("net-sf-rem/index.zul").getInputStream(), "UTF-8");
                    FileObject target = FileUtil.createData(wm.getDocumentBase(),"index.zul");
                    createFile(target,content,"UTF-8");
                    
                    // I want to delete index.jsp, but this causes some exceptions when create
                    // new ZK project. So I keep index.jsp and change its content here.
                    FileObject documentBase = wm.getDocumentBase();
                    FileObject indexjsp = documentBase.getFileObject("index.jsp");
                    if(indexjsp!=null){
                        changeIndexJSP(indexjsp);
                    }
                }
            }
        }
        
        private void changeIndexJSP(FileObject indexjsp) throws IOException {
            String content = readResource(indexjsp.getInputStream(), "UTF-8");
            String find = "<h1>JSP Page</h1>";
            String endLine = System.getProperty("line.separator");
            
            //If the content found is more than nothing, add a message in red font tags,
            //referring to the Bundle.properties file for the message to be displayed:
            if (content.indexOf(find) > 0) {
                StringBuffer replace = new StringBuffer();
                replace.append(find);
                replace.append(endLine);
                replace.append(" <br/>");
                replace.append(endLine);
                replace.append(" <p><font color='red'>");
                replace.append(NbBundle.getMessage(ZKFrameworkProvider.class,
                        "LBL_ZK_WELCOME_PAGE"));
                replace.append("</font></p>");
                //Here we replace the String 'find' with the String 'replace':
                content = content.replaceFirst(find, replace.toString());
                //And now we create the file, using the method from the start of this class:
                createFile(indexjsp, content, "UTF-8");
            }
        }
        
        private void createFile(FileObject target, String content, String encoding) throws IOException{
            FileLock lock = target.lock();
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(target.getOutputStream(lock), encoding));
                bw.write(content);
                bw.close();
                
            } finally {
                lock.releaseLock();
            }
        }
        
        private boolean canCreateNewFile(FileObject parent, String name){
            File fileToBe = new File(FileUtil.toFile(parent), name);
            boolean create = true;
            if (fileToBe.exists()){
                DialogDescriptor dialog = new DialogDescriptor(
                        NbBundle.getMessage(ZKFrameworkProvider.class, "MSG_OverwriteFile", fileToBe.getAbsolutePath()),
                        NbBundle.getMessage(ZKFrameworkProvider.class, "TTL_OverwriteFile"),
                        true, DialogDescriptor.YES_NO_OPTION, DialogDescriptor.NO_OPTION, null);
                java.awt.Dialog d = org.openide.DialogDisplayer.getDefault().createDialog(dialog);
                d.setVisible(true);
                create = (dialog.getValue() == org.openide.DialogDescriptor.NO_OPTION);
            }
            return create;
        }
    }
}
