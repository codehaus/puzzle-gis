/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.gui.toolbox;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows ToolTree component.
 */
public class ToolTreeAction extends AbstractAction {

    public ToolTreeAction() {
        super(NbBundle.getMessage(ToolTreeAction.class, "CTL_ToolTreeAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ToolTreeTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ToolTreeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
