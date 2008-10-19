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
package org.puzzle.core.windows.mapdetail;

import java.util.Collection;
import java.util.Iterator;

import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.TreeContextEvent;
import org.geotools.gui.swing.contexttree.TreeContextListener;
import org.geotools.map.MapContext;

import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import org.puzzle.core.context.ContextService;
import org.puzzle.core.context.ContextEvent;
import org.puzzle.core.context.ContextListener;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class BindContextTree extends JContextTree implements LookupListener {

    private ContextService service = Lookup.getDefault().lookup(ContextService.class);
    private Lookup.Result<MapContext> result = null;
    private ContextListener listener = new ContextListener() {

        @Override
        public void contextActivated(ContextEvent event) {
            setActiveContext(event.getMapContext());
        }
        
    };
    private TreeContextListener treeContextListener = new TreeContextListener() {

        @Override
        public void contextAdded(TreeContextEvent event) {
            service.addContext(event.getContext());
        }

        @Override
        public void contextRemoved(TreeContextEvent event) {
            service.removeContext(event.getContext());
        }

        @Override
        public void contextActivated(TreeContextEvent event) {
            MapContext context = event.getContext();
            service.setActiveContext(context);
        }

        @Override
        public void contextMoved(TreeContextEvent event) {
        }
    };

    public BindContextTree() {
        super();

        if (service != null) {
            result = service.getLookup().lookupResult(MapContext.class);
            result.addLookupListener(this);
            reload(null);
            
            service.addContextListener(listener);
        }

        addTreeContextListener(treeContextListener);
    }

    public void dispose() {
        removeTreeContextListener(treeContextListener);
    }

    private void reload(LookupEvent lookupEvent) {
        MapContext[] contexts = getContexts();
       
        Collection<? extends MapContext> c = null;

        if (lookupEvent == null) {
            c = result.allInstances();
        } else {
            Lookup.Result<MapContext> r = (Lookup.Result<MapContext>) lookupEvent.getSource();
            c = r.allInstances();
        }
        
        //remove contexts non in the lookup
        for (MapContext context : contexts) {
            if(!c.contains(context))
                removeContext(context);
        }
        
        //add new contexts
        Iterator<? extends MapContext> ite = c.iterator();
        while (ite.hasNext()) {
            MapContext context = ite.next();
            addContext(context);
        }
    }

    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        reload(lookupEvent);
    }
}
