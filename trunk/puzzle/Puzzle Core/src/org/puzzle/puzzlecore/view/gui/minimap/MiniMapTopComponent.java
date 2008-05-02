/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
 *  
 *  GPLv3 + Classpath exception
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.puzzle.puzzlecore.view.gui.minimap;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.logging.Logger;
import org.geotools.gui.swing.map.map2d.Map2D;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.puzzle.puzzlecore.swing.minimap.JMiniMap;

/**
 * Top component which displays something.
 * @author : johann sorel
 */
final class MiniMapTopComponent extends TopComponent {

    private JMiniMap map = null;
    private Map2D related = null;
        
    private static MiniMapTopComponent instance;
    static final String ICON_PATH = "org/puzzle/puzzlecore/view/gui/minimap/xmag.png";

    private static final String PREFERRED_ID = "MiniMapTopComponent";

    private MiniMapTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MiniMapTopComponent.class, "CTL_MiniMapTopComponent"));
        setToolTipText(NbBundle.getMessage(MiniMapTopComponent.class, "HINT_MiniMapTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized MiniMapTopComponent getDefault() {
        if (instance == null) {
            instance = new MiniMapTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MiniMapTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MiniMapTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MiniMapTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MiniMapTopComponent) {
            return (MiniMapTopComponent) win;
        }
        Logger.getLogger(MiniMapTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        removeAll();
        if(map == null){
            map = new JMiniMap();
            
//            listener = new ViewListener() {
//
//                public void viewAdded(ViewEvent event) {
//                }
//
//                public void viewRemoved(ViewEvent event) {
//                    if(event.getView().getMap() == related){
//                        related = null;
//                        map.setRelatedMap2D(null);
//                    }
//                }
//
//                public void viewActivated(ViewEvent event) {
//                    related = event.getView().getMap();
//                    map.setRelatedMap2D(related);
//                }
//
//                public void groupAdded(GroupEvent event) {
//                }
//
//                public void groupRemoved(GroupEvent event) {
//                }
//
//                public void groupChanged(GroupEvent event) {
//                }
//            };
            
//            CORE.getViewManager().addViewListener(listener);
        }
        
        add(BorderLayout.CENTER,map);
    }

    @Override
    public void componentClosed() {
//        CORE.getViewManager().removeViewListener(listener);
        
        removeAll();
        map = null;
        
//        CORE.getViewManager().removeViewListener(listener);
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return MiniMapTopComponent.getDefault();
        }
    }
}
