/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.gui.views;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows ViewTree component.
 */
public class ViewTreeAction extends AbstractAction {

    public ViewTreeAction() {
        super(NbBundle.getMessage(ViewTreeAction.class, "CTL_ViewTreeAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ViewTreeTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ViewTreeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
