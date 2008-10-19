/*
 * zkddPaletteCustomizerAction.java
 *
 * Created on November 20, 2005, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.editor.ZKwebxml.palette;



import java.io.IOException;
import org.openide.ErrorManager;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author Administrator
 */
public class zkddPaletteCustomizerAction extends CallableSystemAction {

    private static String name;
    
    public zkddPaletteCustomizerAction () {
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
    }

    protected boolean asynchronous() {
        return false;
    }

    /** Human presentable name of the action. This should be
     * presented as an item in a menu.
     * @return the name of the action
     */
    public String getName() {
        if (name == null)
            name = NbBundle.getBundle(zkddPaletteCustomizerAction.class).getString("ACT_OpenJbossddCustomizer"); // NOI18N
        
        return name;
    }

    /** Help context where to find more about the action.
     * @return the help context for this action
     */
    public HelpCtx getHelpCtx() {
        return null;
    }

    /** This method is called by one of the "invokers" as a result of
     * some user's action that should lead to actual "performing" of the action.
     */
    public void performAction() {
        try {
            zkddPaletteFactory.getPalette().showCustomizer();
        }
        catch (IOException ioe) {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ioe);
        }
    }

}
