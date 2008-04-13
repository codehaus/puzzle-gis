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

    private ContextListener listener = new ContextListener() {

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

        application.addApplicationContextListener(listener);
        addTreeContextListener(treeContextListener);
    }

    public void dispose() {
        application.removeApplicationContextListener(listener);
        removeTreeContextListener(treeContextListener);
    }
}
