/*
 * ZulCatalog.java
 *
 * Created on February 27, 2007, 9:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.loaders;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Iterator;
import org.netbeans.modules.xml.catalog.spi.CatalogDescriptor;
import org.netbeans.modules.xml.catalog.spi.CatalogListener;
import org.netbeans.modules.xml.catalog.spi.CatalogReader;
import org.openide.util.Utilities;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author magic
 */
public class ZulCatalog implements CatalogReader, CatalogDescriptor, org.xml.sax.EntityResolver{
    
    public static final String JAVAEE_NS = "http://java.sun.com/xml/ns/javaee";  // NOI18N
    private static final String ZUL_2_3_0_XSD="zul.xsd"; // NOI18N
    private static final String ZUL_2_3_0=JAVAEE_NS+"/"+ZUL_2_3_0_XSD; // NOI18N
    public static final String ZUL_ID_2_3_0="SCHEMA:"+ZUL_2_3_0; // NOI18N
    private static final String URL_ZUL_2_3_0="nbres:/net/sf/rem/resources/zul.xsd"; // NOI18N
    private static final String URL_ZUL_2_3_0_DTD = "nbres:/net/sf/rem/resources/zul.dtd"; // NOI18N
    
    /** Creates a new instance of ZulCatalog */
    public ZulCatalog() {
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if(ZUL_ID_2_3_0.equals(publicId)){
            return new org.xml.sax.InputSource(URL_ZUL_2_3_0_DTD);
            //return new org.xml.sax.InputSource("nbres:/net/sf/rem/resources/zul.dtd");
        }
        return null;
    }

    public Iterator getPublicIDs() {
        java.util.List<String> list = new java.util.ArrayList<String>();
        list.add(ZUL_ID_2_3_0);
        return list.listIterator();
    }

    public void refresh() {
    }

    public String getSystemID(String string) {
        return URL_ZUL_2_3_0_DTD;
    }

    public String resolveURI(String string) {
        return null;
    }

    public String resolvePublic(String string) {
        return null;
    }

    public void addCatalogListener(CatalogListener catalogListener) {
    }

    public void removeCatalogListener(CatalogListener catalogListener) {
    }

    public Image getIcon(int type) {
        return Utilities.loadImage("net/sf/rem/resources/zulCatalog.png");
    }

    public String getDisplayName() {
        return "ZUL Catalog";
    }

    public String getShortDescription() {
        return "XML Catalog for ZUL 3.0.1";
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }
    
}
