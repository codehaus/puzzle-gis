/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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

package org.puzzle.puzzletoolbox.topcomponents;

import java.awt.GridLayout;
import java.io.Serializable;
import java.util.logging.Logger;
import org.geotools.gui.swing.toolbox.tooltree.JToolTree;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;
import org.puzzle.puzzletoolbox.struct.ToolBoxManager;

/**
 * Top component which displays something.
 * @author johann sorel
 */
final class ToolNavigationTopComponent extends TopComponent {

    private static ToolNavigationTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "ToolNavigationTopComponent";
    private JToolTree tree;
    
    
    private ToolNavigationTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ToolNavigationTopComponent.class, "CTL_ToolNavigationTopComponent"));
        setToolTipText(NbBundle.getMessage(ToolNavigationTopComponent.class, "HINT_ToolNavigationTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
    }

    private void initTree() {
        tree = ToolBoxManager.getInstance().getToolTree();

        removeAll();
        setLayout(new GridLayout(1, 1));
        add(tree);

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ToolNavigationTopComponent getDefault() {
        if (instance == null) {
            instance = new ToolNavigationTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ToolNavigationTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ToolNavigationTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ToolNavigationTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ToolNavigationTopComponent) {
            return (ToolNavigationTopComponent) win;
        }
        Logger.getLogger(ToolNavigationTopComponent.class.getName()).warning(
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
        initTree();
    }

    @Override
    public void componentClosed() {
        tree = null;
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
            return ToolNavigationTopComponent.getDefault();
        }
    }
}
