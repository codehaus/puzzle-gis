/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.gui.contexttree;

import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.TreeContextEvent;
import org.geotools.gui.swing.contexttree.TreeContextListener;
import org.geotools.map.MapContext;
import org.puzzle.puzzlecore.struct.CORE;
import org.puzzle.puzzlecore.struct.ApplicationContextEvent;
import org.puzzle.puzzlecore.struct.ApplicationContextListener;

/**
 *
 * @author johann sorel
 */
public class BindContextTree extends JContextTree {

    private ApplicationContextListener applicationListener = new ApplicationContextListener() {

        public void contextAdded(ApplicationContextEvent event) {
            addContext(event.getMapContext());
        }

        public void contextRemoved(ApplicationContextEvent event) {
            removeContext(event.getMapContext());
        }

        public void contextActivated(ApplicationContextEvent event) {
            setActiveContext(event.getMapContext());
        }

        public void contextMoved(ApplicationContextEvent event) {
            moveContext(event.getMapContext(), event.getToIndex());
        }
    };
    private TreeContextListener treeContextListener = new TreeContextListener() {

        public void contextAdded(TreeContextEvent event) {
            CORE.getInstance().addContext(event.getContext());
        }

        public void contextRemoved(TreeContextEvent event) {
            CORE.getInstance().removeContext(event.getContext());
        }

        public void contextActivated(TreeContextEvent event) {
            MapContext context = event.getContext();

            if (context != null) {
                CORE.getInstance().setActiveContext(event.getContext());
            } else {
                setActiveContext(CORE.getInstance().getActiveContext());
            }

        }

        public void contextMoved(TreeContextEvent event) {
            CORE.getInstance().moveContext(event.getContext(), event.getToIndex());
        }
    };
    private final CORE application;

    public BindContextTree(CORE application) {
        super();
        this.application = application;


        MapContext[] contexts = application.getContexts();

        for (MapContext context : contexts) {
            addContext(context);
        }

        application.addApplicationContextListener(applicationListener);
        addTreeContextListener(treeContextListener);
    }

    public void dispose() {
        application.removeApplicationContextListener(applicationListener);
        removeTreeContextListener(treeContextListener);
    }
}
