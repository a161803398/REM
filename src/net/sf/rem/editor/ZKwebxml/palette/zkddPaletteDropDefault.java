/*
 * zkddPaletteDropDefault.java
 *
 * Created on November 20, 2005, 4:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.editor.ZKwebxml.palette;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.openide.text.ActiveEditorDrop;

/**
 *
 * @author Administrator
 */

 public class zkddPaletteDropDefault implements ActiveEditorDrop {
    
    String body;

    public zkddPaletteDropDefault(String body) {
        this.body = body;
    }

    public boolean handleTransfer(JTextComponent targetComponent) {

        if (targetComponent == null)
            return false;

        try {
            zkddPaletteUtilities.insert(body, (JTextComponent)targetComponent);
        }
        catch (BadLocationException ble) {
            return false;
        }
        
        return true;
    }

}