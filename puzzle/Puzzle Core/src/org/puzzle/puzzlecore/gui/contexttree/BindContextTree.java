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
import org.puzzle.puzzlecore.struct.ContextEvent;
import org.puzzle.puzzlecore.struct.ContextListener;
import org.puzzle.puzzlecore.struct.ContextManager;

/**
 *
 * @author johann sorel
 */
public class BindContextTree extends JContextTree {

    private ContextListener applicationListener = new ContextListener() {

        public void contextAdded(ContextEvent event) {
            addContext(event.getMapContext());
        }

        public void contextRemoved(ContextEvent event) {
            removeContext(event.getMapContext());
        }

        public void contextActivated(ContextEvent event) {
            setActiveContext(event.getMapContext());
        }

        public void contextMoved(ContextEvent event) {
            moveContext(event.getMapContext(), event.getToIndex());
        }
    };
    private TreeContextListener treeContextListener = new TreeContextListener() {

        public void contextAdded(TreeContextEvent event) {
            CORE.getContextManager().addContext(event.getContext());
        }

        public void contextRemoved(TreeContextEvent event) {
            CORE.getContextManager().removeContext(event.getContext());
        }

        public void contextActivated(TreeContextEvent event) {
            MapContext context = event.getContext();

            if (context != null) {
                CORE.getContextManager().setActiveContext(event.getContext());
            } else {
                setActiveContext(CORE.getContextManager().getActiveContext());
            }

        }

        public void contextMoved(TreeContextEvent event) {
            CORE.getContextManager().moveContext(event.getContext(), event.getToIndex());
        }
    };
    private final ContextManager application;

    public BindContextTree(ContextManager application) {
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
