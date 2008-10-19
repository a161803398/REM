/*
 * zkddPaletteFactory.java
 *
 * Created on November 20, 2005, 1:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.editor.ZKwebxml.palette;


import java.io.IOException;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;


/**
 *
 * @author Administrator
 */
public class zkddPaletteFactory {
    
   public static final String ZK_PALETTE_FOLDER = "JSPPalette";
    
    private static PaletteController palette = null;
    
    public static PaletteController getPalette() throws IOException {
    
        if (palette == null)
            palette = PaletteFactory.createPalette(ZK_PALETTE_FOLDER, new zkddPaletteActions());//, null, new HTMLDragAndDropHandler());
            
        return palette;
    }
    
}
