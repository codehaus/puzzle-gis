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
package org.puzzle.puzzlecore.struct;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.JContextTreePopup;
import org.geotools.gui.swing.contexttree.column.OpacityTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.StyleTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.VisibleTreeTableColumn;
import org.geotools.gui.swing.contexttree.popup.ContextActiveItem;
import org.geotools.gui.swing.contexttree.popup.CopyItem;
import org.geotools.gui.swing.contexttree.popup.CutItem;
import org.geotools.gui.swing.contexttree.popup.DeleteItem;
import org.geotools.gui.swing.contexttree.popup.DuplicateItem;
import org.geotools.gui.swing.contexttree.popup.LayerFeatureItem;
import org.geotools.gui.swing.contexttree.popup.LayerPropertyItem;
import org.geotools.gui.swing.contexttree.popup.LayerVisibilityItem;
import org.geotools.gui.swing.contexttree.popup.PasteItem;
import org.geotools.gui.swing.contexttree.popup.SeparatorItem;
import org.geotools.gui.swing.propertyedit.LayerCRSPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerFilterPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerGeneralPanel;
import org.geotools.gui.swing.propertyedit.LayerStylePropertyPanel;
import org.geotools.gui.swing.propertyedit.PropertyPane;
import org.geotools.gui.swing.propertyedit.filterproperty.JCQLPropertyPanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JSimpleStylePanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JXMLStylePanel;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.puzzle.puzzlecore.gtextend.widget.sldeditor.JAdvancedStylePanel;
import org.puzzle.puzzlecore.gui.contexttree.BindContextTree;

/**
 *
 * @author johann sorel
 */
public class ContextManager {

    private static ContextManager instance;
    private WeakReference<BindContextTree> treeRef = new WeakReference<BindContextTree>(null);
    
    
    private final MapContext[] EMPTY_CONTEXT_ARRAY = new MapContext[0];
    private final EventListenerList listeners = new EventListenerList();
    private MapContext activeContext = null;
    private final List<MapContext> contexts = new ArrayList<MapContext>();

    private ContextManager() {
        
        MapContext context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        context.setTitle("Context");
        contexts.add(context);
        activeContext = context;
    }

    public JContextTree getContextTree() {

        BindContextTree tree = treeRef.get();
                
        if (tree == null) {
            tree = new BindContextTree(this);

            tree.addColumn(new VisibleTreeTableColumn());
            tree.addColumn(new OpacityTreeTableColumn());
            tree.addColumn(new StyleTreeTableColumn());

            JContextTreePopup popupMenu = tree.getPopupMenu();
            popupMenu.addItem(new LayerVisibilityItem());
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new LayerFeatureItem());
            popupMenu.addItem(new ContextActiveItem(tree));
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new CutItem(tree));
            popupMenu.addItem(new CopyItem(tree));
            popupMenu.addItem(new PasteItem(tree));
            popupMenu.addItem(new DuplicateItem(tree));
            popupMenu.addItem(new SeparatorItem());
            popupMenu.addItem(new DeleteItem(tree));
            popupMenu.addItem(new SeparatorItem());

            LayerPropertyItem property = new LayerPropertyItem();
            List<PropertyPane> lstproperty = new ArrayList<PropertyPane>();
            lstproperty.add(new LayerGeneralPanel());
            lstproperty.add(new LayerCRSPropertyPanel());

            LayerFilterPropertyPanel filters = new LayerFilterPropertyPanel();
            filters.addPropertyPanel(new JCQLPropertyPanel());
            lstproperty.add(filters);

            LayerStylePropertyPanel styles = new LayerStylePropertyPanel();
            styles.addPropertyPanel(new JSimpleStylePanel());
            styles.addPropertyPanel(new JAdvancedStylePanel());
            styles.addPropertyPanel(new JXMLStylePanel());
            lstproperty.add(styles);

            property.setPropertyPanels(lstproperty);
            popupMenu.addItem(property);


            tree.revalidate();
        }

        return tree;
    }

    static ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }
        return instance;
    }
    
    
    
    ////////////////////////////////////////////////////////////////////////////////
// MAPCONTEXT MANAGEMENT ///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    /**
     * get the active context
     * @return return the active MapContext, if none return null
     */
    public MapContext getActiveContext() {
        return activeContext;
    }

    /**
     * active the context if mapcontext is in the mapcontext list
     * @param context the mapcontext to active
     */
    public void setActiveContext(MapContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (contexts.contains(context)) {

            if (context != activeContext) {
                activeContext = context;
                fireContextActivated(context, contexts.indexOf(context));
            }
        } else {
            throw new IllegalArgumentException();
        }


    }

    /**
     * add context to application if not allready in it
     * @param context the context to add
     */
    public void addContext(MapContext context) {

        if (!contexts.contains(context)) {
            contexts.add(context);
            fireContextAdded(context, contexts.indexOf(context));
        }
    }

    /**
     * remove context 
     * @param context target mapcontext to remove
     */
    public void removeContext(MapContext context) {

        if (context == activeContext) {
            if (getContextCount() == 1) {
                MapContext ctx = new DefaultMapContext(DefaultGeographicCRS.WGS84);
                ctx.setTitle("Context");
                addContext(ctx);
                setActiveContext(ctx);
            } else {
                int n = getContextIndex(context);
                if (n == 0) {
                    setActiveContext(getContext(1));
                } else {
                    setActiveContext(getContext(0));
                }
            }
        }

        if (contexts.contains(context)) {
            int position = contexts.indexOf(context);
            contexts.remove(context);
            fireContextRemoved(context, position);
        }

    }

    /**
     * count MapContext 
     * @return number of mapcontext in the tree
     */
    public int getContextCount() {
        return contexts.size();
    }

    /**
     * return context at index i
     * @param i position of the mapcontext
     * @return the mapcontext a position i
     */
    public MapContext getContext(int i) {
        return contexts.get(i);
    }

    /**
     * get the index of a mapcontext
     * @param context the mapcontext to find
     * @return index of context
     */
    public int getContextIndex(MapContext context) {
        return contexts.indexOf(context);
    }

    /**
     * MapContext Array
     * @return empty Array if no mapcontexts
     */
    public MapContext[] getContexts() {
        return contexts.toArray(EMPTY_CONTEXT_ARRAY);
    }

    /**
     * move a mapcontext in the list
     * @param context the context to move, must be in the list
     * @param newplace new index of mapcontext
     */
    public void moveContext(MapContext context, int newplace) {
        if (contexts.contains(context) && newplace < contexts.size()) {

            int origine = contexts.indexOf(context);

            if (newplace != origine) {
                contexts.remove(context);
                contexts.add(newplace, context);
                fireContextMoved(context, origine, newplace);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }


////////////////////////////////////////////////////////////////////////////////
// FIREEVENT AND LISTENERS /////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    /**
     * generate a Applicationevent for an added node
     * @param mapcontext the added mapcontext
     * @param position the position of the mapcontext in the tree
     */
    private void fireContextAdded(MapContext mapcontext, int position) {
        ContextEvent kevent = new ContextEvent(this, mapcontext, position);

        ContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextAdded(kevent);
        }
    }

    /**
     * generate a Applicationevent for a mapcontext removed
     * @param mapcontext the removed mapcontext
     * @param position the last position of the mapcontext
     */
    private void fireContextRemoved(MapContext mapcontext, int position) {
        ContextEvent event = new ContextEvent(this, mapcontext, position);

        ContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextRemoved(event);
        }
    }

    /**
     * generate a Applicationevent for an activated mapcontext
     * @param mapcontext the activated mapcontext (null if none activated)
     * @param index the position of the activated context
     */
    private void fireContextActivated(MapContext mapcontext, int index) {
        ContextEvent event = new ContextEvent(this, mapcontext, index);

        ContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextActivated(event);
        }
    }

    /**
     * generate a Applicationevent for a moving context
     * @param mapcontext the moving mapcontext
     * @param begin the start position of the mapcontext
     * @param end the end position of the mapcontext
     */
    private void fireContextMoved(MapContext mapcontext, int begin, int end) {
        ContextEvent event = new ContextEvent(this, mapcontext, begin, end);

        ContextListener[] list = getApplicationContextListeners();
        for (int i = 0; i < list.length; i++) {
            list[i].contextMoved(event);
        }
    }

    /**
     * add ApplicationListener to Model
     * @param ker the new listener
     */
    public void addApplicationContextListener(ContextListener ker) {
        listeners.add(ContextListener.class, ker);
    }

    /**
     * remove ApplicationListener from Model
     * @param ker the listner to remove
     */
    public void removeApplicationContextListener(ContextListener ker) {
        listeners.remove(ContextListener.class, ker);
    }

    /**
     * get ApplicationListeners list
     * @return the listener's table
     */
    public ContextListener[] getApplicationContextListeners() {
        return listeners.getListeners(ContextListener.class);
    }
    
    
    
    
    
}
