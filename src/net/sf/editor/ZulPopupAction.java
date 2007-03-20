/*
 * ZulPopupAction.java
 *
 * Created on March 19, 2007, 10:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.editor;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseAction;
import org.netbeans.editor.BaseDocument;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

/**
 * Popup Menu for Zul file.
 * @author magic
 */
public class ZulPopupAction extends SystemAction implements Presenter.Popup{
    
    /** Creates a new instance of ZulPopupAction */
    public ZulPopupAction() {
    }

    public String getName() {
        return NbBundle.getMessage(ZulPopupAction.class, 
                "net-sf-editor-ZulPopupAction.instance"); // NOI18N
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    public void actionPerformed(ActionEvent actionEvent) {
    }

    public JMenuItem getPopupPresenter() {
        return new SubMenu(getName());
    }
    
    public class SubMenu extends JMenu {
        
        public SubMenu(String s){
            super(s);
        }
        
        /** Gets popup menu. Overrides superclass. Adds lazy menu items creation. */
        public JPopupMenu getPopupMenu() {
            JPopupMenu pm = super.getPopupMenu();
            pm.removeAll();
            pm.add(new AddZscriptAction());
            pm.pack();
            return pm;
        }
    }
    
    /**
     *  Add zscript section action.
     */
    public static class AddZscriptAction extends BaseAction {
        public AddZscriptAction(){
            super(NbBundle.getBundle(ZulPopupAction.class).getString("add-zscript-action"));
        }

        public void actionPerformed(ActionEvent actionEvent, JTextComponent target) {
            Document doc = target.getDocument();
            int offset = target.getCaretPosition();
            target.setCaretPosition(ZulEditorUtilities.writeZscript((BaseDocument)doc,offset));
        }
    }
}
