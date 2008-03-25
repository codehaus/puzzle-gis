/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.gui.contexttree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows ContextTree component.
 */
public class ContextTreeAction extends AbstractAction {

    public ContextTreeAction() {
        super(NbBundle.getMessage(ContextTreeAction.class, "CTL_ContextTreeAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ContextTreeTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ContextTreeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
