/*
 * ZulEditorUtilities.java
 *
 * Created on March 18, 2007, 2:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.editor;

import javax.swing.text.BadLocationException;
import org.netbeans.editor.BaseDocument;
import org.openide.ErrorManager;

/**
 *
 * @author magic
 */
public class ZulEditorUtilities {
    
    /** The constant from XML editor
     */
    protected static int XML_ATTRIBUTE = 5;
    protected static int XML_ATTRIBUTE_VALUE = 7;
    protected static int XML_PI_CONTENT = 15;
    
    public static String END_LINE = System.getProperty("line.separator");
    
    /** Creates a new instance of ZulEditorUtilities */
    public ZulEditorUtilities() {
    }
    
    /**
     *  write zscript section to document.
     */
    public static int writeZscript(BaseDocument doc, int offset){
        int position = -1;
        StringBuffer appendText = new StringBuffer();
        appendText.append("<zscript><![CDATA[" + END_LINE + END_LINE);
        appendText.append("]]></zscript>");
        try {
            position = writeString(doc,appendText.toString(),offset);
        } catch (BadLocationException ex) {
            ErrorManager.getDefault().notify(ex);
        }
        return position;
    }
    
    /**
     *  write zscript section string to document.
     */
    private static int writeString(BaseDocument doc, String text, int offset) throws BadLocationException {
        int formatLength = 0;      
        try{
            doc.atomicLock();
            offset = doc.getFormatter().indentLine(doc, offset);
            doc.insertString(Math.min(offset, doc.getLength()), text, null );
            formatLength = doc.getFormatter().reformat(doc, offset, offset + text.length());
        }
        finally{
            doc.atomicUnlock();
        }
        
        int length = ("<zscript><![CDATA[" + END_LINE + END_LINE).length();
        return Math.min(offset + length - 1, doc.getLength());
    }
}
