/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlemap2d.topcomponents;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows MiniMap component.
 */
public class MiniMapAction extends AbstractAction {

    public MiniMapAction() {
        super(NbBundle.getMessage(MiniMapAction.class, "CTL_MiniMapAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(MiniMapTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = MiniMapTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
