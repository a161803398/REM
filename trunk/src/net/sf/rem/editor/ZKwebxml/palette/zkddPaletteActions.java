/*
 * zkddPaletteActions.java
 *
 * Created on November 20, 2005, 1:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.editor.ZKwebxml.palette;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.Utilities;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.text.ActiveEditorDrop;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;


/**
 *
 * @author Administrator
 */
public class zkddPaletteActions extends PaletteActions {
    
    /** Creates a new instance of FormPaletteProvider */
    public zkddPaletteActions() {
    }

    public Action[] getImportActions() {
        return new Action[0]; //TODO implement this
    }

    public Action[] getCustomCategoryActions(Lookup category) {
        return new Action[0]; //TODO implement this
    }

    public Action[] getCustomItemActions(Lookup item) {
        return new Action[0]; //TODO implement this
    }

    public Action[] getCustomPaletteActions() {
        return new Action[0]; //TODO implement this
    }

    public Action getPreferredAction( Lookup item ) {
        return new MFPaletteInsertAction(item);
    }
    
    private static class MFPaletteInsertAction extends AbstractAction {
        
        private Lookup item;
        
        MFPaletteInsertAction(Lookup item) {
            this.item = item;
        }
                
        public void actionPerformed(ActionEvent e) {
      
            ActiveEditorDrop drop = (ActiveEditorDrop) item.lookup(ActiveEditorDrop.class);
            
            JTextComponent target = Utilities.getFocusedComponent();
            if (target == null) {
                String msg = NbBundle.getMessage(zkddPaletteActions.class, "MSG_ErrorNoFocusedDocument");
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE));
                return;
            }
            
            try {
                drop.handleTransfer(target);
            }
            finally {
                Utilities.requestFocus(target);
            }
            
            try {
                PaletteController pc = zkddPaletteFactory.getPalette();
                pc.clearSelection();
            }
            catch (IOException ioe) {
            } //should not occur

        }
    }
    
}
