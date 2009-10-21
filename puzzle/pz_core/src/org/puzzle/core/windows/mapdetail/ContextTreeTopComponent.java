/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.core.windows.mapdetail;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.geotoolkit.gui.swing.contexttree.JContextTree;
import org.geotoolkit.gui.swing.contexttree.TreePopupItem;
import org.geotoolkit.gui.swing.contexttree.menu.SeparatorItem;
import org.geotoolkit.gui.swing.propertyedit.PropertyPane;
import org.geotoolkit.map.MapContext;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

import org.puzzle.core.contexttree.LayerPropertyItem;
import org.puzzle.core.project.filetype.GISContextDataObject;
import org.puzzle.core.project.filetype.GISContextViewNode;
import org.puzzle.core.project.view.GISView;
import org.puzzle.core.resources.MessageBundle;
import org.puzzle.core.view.ViewComponent;

/**
 * Top component which displays the context detail view.
 * 
 * @author : Johann Sorel (Puzzle-GIS)
 */
final class ContextTreeTopComponent extends TopComponent{

    private static Lookup.Result resultContext = null;
    private static Lookup.Result resultViews = null;
    private static Lookup.Result resultGISViews = null;

    static {
        resultContext = Utilities.actionsGlobalContext().lookupResult(GISContextDataObject.class);
        resultContext.addLookupListener(new LookupListener() {

            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                final Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
                final Collection c = r.allInstances();
                if (!c.isEmpty()) {

                    new Thread() {

                        @Override
                        public void run() {
                            final Iterator ite = c.iterator();
                            while (ite.hasNext()) {
                                final GISContextDataObject da = (GISContextDataObject) ite.next();
                                final MapContext candidate = da.getContext();
                                updateName(candidate);
                            }
                        }
                    }.start();

                }
            }
        });
        resultContext.allItems();

        resultViews = Utilities.actionsGlobalContext().lookupResult(ViewComponent.class);
        resultViews.addLookupListener(new LookupListener() {

            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                final Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
                final Collection c = r.allInstances();
                if (!c.isEmpty()) {

                    new Thread() {

                        @Override
                        public void run() {
                            final Iterator ite = c.iterator();
                            while (ite.hasNext()) {
                                final ViewComponent da = (ViewComponent) ite.next();
                                final MapContext candidate = da.getContext();
                                updateName(candidate);
                            }
                        }
                    }.start();

                }
            }
        });
        resultViews.allItems();

        resultGISViews = Utilities.actionsGlobalContext().lookupResult(GISView.class);
        resultGISViews.addLookupListener(new LookupListener() {

            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                final Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
                final Collection c = r.allInstances();
                if (!c.isEmpty()) {

                    new Thread() {

                        @Override
                        public void run() {
                            final Iterator ite = c.iterator();
                            while (ite.hasNext()) {
                                final GISView da = (GISView) ite.next();
                                final MapContext candidate = da.getContext().getContext();
                                updateName(candidate);
                            }
                        }
                    }.start();

                }
            }
        });
        resultGISViews.allItems();


    }

    private static void updateName(final MapContext candidate){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ContextTreeTopComponent window = findInstance();
                if (window != null && candidate != window.getContextTree().getContext()) {
                    window.getContextTree().setContext(candidate);
                    window.setDisplayName(MessageBundle.getString("contextTree") + " - " + candidate.getDescription().getTitle());
                }
            }
        });
    }

    private JContextTree tree = null;
    private static ContextTreeTopComponent instance;
    private static final String PREFERRED_ID = "ContextTreeTopComponent";

    private ContextTreeTopComponent() {
        initComponents();
        setDisplayName(MessageBundle.getString("contextTree"));
        setToolTipText(MessageBundle.getString("contextTreeHint"));
        setIcon(ImageUtilities.loadImage("org/puzzle/core/project/map.png", true));

    }

    public synchronized JContextTree getContextTree() {
        if(tree == null){
            final JContextTree tree = new JContextTree();

            //search available popup menu items
            Lookup lk = Lookups.forPath("/Puzzle/ContextTree/Actions");
            for(TreePopupItem item : lk.lookupAll(TreePopupItem.class)){
                tree.controls().add(item);
            }

            final List<PropertyPane> configPanes = new ArrayList<PropertyPane>();

            JPropertyTree propertyTree = new JPropertyTree();

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();

            //search available property panels
            lk = Lookups.forPath("/Puzzle/ContextTree/PropertyPanels");
            for(PropertyPane p : lk.lookupAll(PropertyPane.class)){
                root.add(new DefaultMutableTreeNode(p));
            }

            //search filter panels
            DefaultMutableTreeNode filterNodes = new DefaultMutableTreeNode(MessageBundle.getString("filter"));
            lk = Lookups.forPath("/Puzzle/ContextTree/FilterPanels");
            for(PropertyPane p : lk.lookupAll(PropertyPane.class)){
                filterNodes.add(new DefaultMutableTreeNode(p));
            }
            root.add(filterNodes);

            //search style panels
            DefaultMutableTreeNode styleNodes = new DefaultMutableTreeNode(MessageBundle.getString("symbology"));
            lk = Lookups.forPath("/Puzzle/ContextTree/StylePanels");
            for(PropertyPane p : lk.lookupAll(PropertyPane.class)){
                styleNodes.add(new DefaultMutableTreeNode(p));
            }
            root.add(styleNodes);

            if(!tree.controls().isEmpty()){
                tree.controls().add(new SeparatorItem());
            }

            LayerPropertyItem property = new LayerPropertyItem(root);
            tree.controls().add(property);

            tree.revalidate();
            tree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.tree = tree;
        }
        return tree;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ContextTreeTopComponent getDefault() {
        if (instance == null) {
            instance = new ContextTreeTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ContextTreeTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ContextTreeTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ContextTreeTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ContextTreeTopComponent) {
            return (ContextTreeTopComponent) win;
        }
        Logger.getLogger(ContextTreeTopComponent.class.getName()).warning(
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
        add(BorderLayout.CENTER, getContextTree());
        requestActive();
    }

    @Override
    public void componentClosed() {
        removeAll();
        tree = null;
    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
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
            return ContextTreeTopComponent.getDefault();
        }
    }

}
