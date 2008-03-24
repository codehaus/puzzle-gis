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
package org.puzzle.puzzlecontexttree.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import org.geotools.gui.swing.contexttree.JContextTree;
import org.geotools.gui.swing.contexttree.JContextTreePopup;
import org.geotools.gui.swing.contexttree.TreeContextEvent;
import org.geotools.gui.swing.contexttree.TreeContextListener;
import org.geotools.gui.swing.contexttree.column.OpacityTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.StyleTreeTableColumn;
import org.geotools.gui.swing.contexttree.column.TreeTableColumn;
import org.geotools.gui.swing.contexttree.column.VisibleTreeTableColumn;
import org.geotools.gui.swing.contexttree.node.SourceGroup;
import org.geotools.gui.swing.contexttree.node.StyleGroup;
import org.geotools.gui.swing.contexttree.node.SubNodeGroup;
import org.geotools.gui.swing.contexttree.popup.ContextActiveItem;
import org.geotools.gui.swing.contexttree.popup.ContextPropertyItem;
import org.geotools.gui.swing.contexttree.popup.CopyItem;
import org.geotools.gui.swing.contexttree.popup.CutItem;
import org.geotools.gui.swing.contexttree.popup.DeleteItem;
import org.geotools.gui.swing.contexttree.popup.DuplicateItem;
import org.geotools.gui.swing.contexttree.popup.LayerFeatureItem;
import org.geotools.gui.swing.contexttree.popup.LayerPropertyItem;
import org.geotools.gui.swing.contexttree.popup.LayerVisibilityItem;
import org.geotools.gui.swing.contexttree.popup.PasteItem;
import org.geotools.gui.swing.contexttree.popup.RuleMaxScaleItem;
import org.geotools.gui.swing.contexttree.popup.RuleMinScaleItem;
import org.geotools.gui.swing.contexttree.popup.SeparatorItem;
import org.geotools.gui.swing.contexttree.popup.TreePopupItem;
import org.geotools.gui.swing.propertyedit.LayerCRSPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerFilterPropertyPanel;
import org.geotools.gui.swing.propertyedit.LayerGeneralPanel;
import org.geotools.gui.swing.propertyedit.LayerStylePropertyPanel;
import org.geotools.gui.swing.propertyedit.PropertyPane;
import org.geotools.gui.swing.propertyedit.filterproperty.JCQLPropertyPanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JSimpleStylePanel;
import org.geotools.gui.swing.propertyedit.styleproperty.JXMLStylePanel;
import org.geotools.map.MapContext;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.puzzle.puzzlecontexttree.options.ContextTreePanel;
import org.puzzle.puzzlecore.gtextend.widget.sldeditor.JAdvancedStylePanel;
import org.puzzle.puzzlecore.struct.Application;
import org.puzzle.puzzlecore.struct.ApplicationContextEvent;
import org.puzzle.puzzlecore.struct.ApplicationContextListener;

/**
 *
 * @author johann sorel
 */
public class ContextTreeManager {

    private static ContextTreeManager instance;
    private JContextTree contextTree = null;
    private ApplicationContextListener applicationListener = new ApplicationContextListener() {

        public void contextAdded(ApplicationContextEvent event) {
            getContextTree().addContext(event.getMapContext());
        }

        public void contextRemoved(ApplicationContextEvent event) {
            getContextTree().removeContext(event.getMapContext());
        }

        public void contextActivated(ApplicationContextEvent event) {
            getContextTree().setActiveContext(event.getMapContext());
        }

        public void contextMoved(ApplicationContextEvent event) {
            getContextTree().moveContext(event.getMapContext(), event.getToIndex());
        }
    };
    private TreeContextListener treeContextListener = new TreeContextListener() {

        public void contextAdded(TreeContextEvent event) {
            Application.getInstance().addContext(event.getContext());
        }

        public void contextRemoved(TreeContextEvent event) {
            Application.getInstance().removeContext(event.getContext());
        }

        public void contextActivated(TreeContextEvent event) {
            MapContext context = event.getContext();
            
            if(context != null){
                Application.getInstance().setActiveContext(event.getContext());                
            }else{
                contextTree.setActiveContext(Application.getInstance().getActiveContext());
            }
            
        }

        public void contextMoved(TreeContextEvent event) {
            Application.getInstance().moveContext(event.getContext(), event.getToIndex());
        }
    };

    private ContextTreeManager() {
    }

    public JContextTree getContextTree() {

        if (contextTree == null) {
            contextTree = new JContextTree();

            Application.getInstance().addApplicationContextListener(applicationListener);

            Preferences preferences = NbPreferences.forModule(ContextTreePanel.class);


            //columns
            if (preferences.getBoolean(ContextTreePanel.PREF_COL_VISIBILITY, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_COL_VISIBILITY, true);
                contextTree.addColumn(new VisibleTreeTableColumn());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_COL_OPACITY, false)) {
                contextTree.addColumn(new OpacityTreeTableColumn());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_COL_STYLE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_COL_STYLE, true);
                contextTree.addColumn(new StyleTreeTableColumn());
            }

            //nodes
            if (preferences.getBoolean(ContextTreePanel.PREF_NOD_SOURCE, false)) {
                contextTree.addSubNodeGroup(new SourceGroup());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_NOD_STYLE, false)) {
                contextTree.addSubNodeGroup(new StyleGroup());
            }

            JContextTreePopup popupMenu = contextTree.getPopupMenu();

            //popup items
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_LAYER_VISIBLE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_LAYER_VISIBLE, true);
                popupMenu.addItem(new LayerVisibilityItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_SEPARATOR_1, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_SEPARATOR_1, true);
                popupMenu.addItem(new SeparatorItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_LAYER_FEATURE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_LAYER_FEATURE, true);
                popupMenu.addItem(new LayerFeatureItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_CONTEXT_ACTIVE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_CONTEXT_ACTIVE, true);
                popupMenu.addItem(new ContextActiveItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_SEPARATOR_2, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_SEPARATOR_2, true);
                popupMenu.addItem(new SeparatorItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_CUT, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_CUT, true);
                popupMenu.addItem(new CutItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_COPY, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_COPY, true);
                popupMenu.addItem(new CopyItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_PASTE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_PASTE, true);
                popupMenu.addItem(new PasteItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_DUPLICATE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_DUPLICATE, true);
                popupMenu.addItem(new DuplicateItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_SEPARATOR_3, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_SEPARATOR_3, true);
                popupMenu.addItem(new SeparatorItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_DELETE, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_DELETE, true);
                popupMenu.addItem(new DeleteItem(contextTree));
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_SEPARATOR_4, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_SEPARATOR_4, true);
                popupMenu.addItem(new SeparatorItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_LAYER_PROPERTY, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_LAYER_PROPERTY, true);
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
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_CONTEXT_PROPERTY, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_CONTEXT_PROPERTY, true);
                popupMenu.addItem(new ContextPropertyItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_STYLE_RULE_MIN, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_STYLE_RULE_MIN, true);
                popupMenu.addItem(new RuleMinScaleItem());
            }
            if (preferences.getBoolean(ContextTreePanel.PREF_POP_STYLE_RULE_MAX, true)) {
                preferences.putBoolean(ContextTreePanel.PREF_POP_STYLE_RULE_MAX, true);
                popupMenu.addItem(new RuleMaxScaleItem());
            }



            preferences.addPreferenceChangeListener(new PreferenceChangeListener() {

                public void checkColumn(String PREF, Class<? extends TreeTableColumn> classe) {
                    boolean found = false;
                    TreeTableColumn[] cols = contextTree.getColumns();

                    if (NbPreferences.forModule(ContextTreePanel.class).getBoolean(PREF, false)) {
                        for (TreeTableColumn col : cols) {
                            if (classe.isInstance(col)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            try {
                                contextTree.addColumn(classe.newInstance());
                            } catch (InstantiationException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (IllegalAccessException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    } else {
                        for (TreeTableColumn col : cols) {
                            if (classe.isInstance(col)) {
                                contextTree.removeColumn(col);
                            }
                        }
                    }
                }

                public void checkNode(String PREF, Class<? extends SubNodeGroup> classe) {
                    boolean found = false;
                    SubNodeGroup[] nodes = contextTree.getSubNodeGroups();

                    if (NbPreferences.forModule(ContextTreePanel.class).getBoolean(PREF, false)) {
                        for (SubNodeGroup node : nodes) {
                            if (classe.isInstance(node)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            try {
                                contextTree.addSubNodeGroup(classe.newInstance());
                            } catch (InstantiationException ex) {
                                Exceptions.printStackTrace(ex);
                            } catch (IllegalAccessException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    } else {
                        System.out.println("ici");
                        for (SubNodeGroup node : nodes) {
                            if (classe.isInstance(node)) {
                                System.out.println("la");
                                contextTree.removeSubNodeGroup(node);
                            }
                        }
                    }
                }

                public void checkPopup(String PREF, Class<? extends TreePopupItem> classe) {
                    boolean found = false;
                    JContextTreePopup popupmanager = contextTree.getPopupMenu();
                    TreePopupItem[] popups = popupmanager.getControls();

                    if (NbPreferences.forModule(ContextTreePanel.class).getBoolean(PREF, false)) {
                        for (TreePopupItem item : popups) {
                            if (classe.isInstance(item)) {
                                found = true;
                            }
                        }
                        if (!found) {

                            if (classe.equals(ContextActiveItem.class)) {
                                popupmanager.addItem(new ContextActiveItem(contextTree));
                            } else if (classe.equals(CutItem.class)) {
                                popupmanager.addItem(new CutItem(contextTree));
                            } else if (classe.equals(CopyItem.class)) {
                                popupmanager.addItem(new CopyItem(contextTree));
                            } else if (classe.equals(PasteItem.class)) {
                                popupmanager.addItem(new PasteItem(contextTree));
                            } else if (classe.equals(DuplicateItem.class)) {
                                popupmanager.addItem(new DuplicateItem(contextTree));
                            } else if (classe.equals(DeleteItem.class)) {
                                popupmanager.addItem(new DeleteItem(contextTree));
                            } else {
                                try {
                                    popupmanager.addItem(classe.newInstance());
                                } catch (InstantiationException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (IllegalAccessException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }

                        }
                    } else {
                        for (TreePopupItem item : popups) {
                            if (classe.isInstance(item)) {
                                popupmanager.removeItem(item);
                            }
                        }
                    }
                }

                public void preferenceChange(PreferenceChangeEvent evt) {

                    //columns---------
                    if (evt.getKey().equals(ContextTreePanel.PREF_COL_VISIBILITY)) {
                        checkColumn(ContextTreePanel.PREF_COL_VISIBILITY, VisibleTreeTableColumn.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_COL_OPACITY)) {
                        checkColumn(ContextTreePanel.PREF_COL_OPACITY, OpacityTreeTableColumn.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_COL_STYLE)) {
                        checkColumn(ContextTreePanel.PREF_COL_STYLE, StyleTreeTableColumn.class);
                    } //nodes---------
                    else if (evt.getKey().equals(ContextTreePanel.PREF_NOD_SOURCE)) {
                        checkNode(ContextTreePanel.PREF_NOD_SOURCE, SourceGroup.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_NOD_STYLE)) {
                        checkNode(ContextTreePanel.PREF_NOD_STYLE, StyleGroup.class);
                    } //popup items--------
                    else if (evt.getKey().equals(ContextTreePanel.PREF_POP_LAYER_VISIBLE)) {
                        checkPopup(ContextTreePanel.PREF_POP_LAYER_VISIBLE, LayerVisibilityItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_SEPARATOR_1)) {
                        checkPopup(ContextTreePanel.PREF_POP_SEPARATOR_1, SeparatorItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_LAYER_FEATURE)) {
                        checkPopup(ContextTreePanel.PREF_POP_LAYER_FEATURE, LayerFeatureItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_CONTEXT_ACTIVE)) {
                        checkPopup(ContextTreePanel.PREF_POP_CONTEXT_ACTIVE, ContextActiveItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_SEPARATOR_2)) {
                        checkPopup(ContextTreePanel.PREF_POP_SEPARATOR_2, SeparatorItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_CUT)) {
                        checkPopup(ContextTreePanel.PREF_POP_CUT, CutItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_COPY)) {
                        checkPopup(ContextTreePanel.PREF_POP_COPY, CopyItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_PASTE)) {
                        checkPopup(ContextTreePanel.PREF_POP_PASTE, PasteItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_DUPLICATE)) {
                        checkPopup(ContextTreePanel.PREF_POP_DUPLICATE, DuplicateItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_SEPARATOR_3)) {
                        checkPopup(ContextTreePanel.PREF_POP_SEPARATOR_3, SeparatorItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_DELETE)) {
                        checkPopup(ContextTreePanel.PREF_POP_DELETE, DeleteItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_SEPARATOR_4)) {
                        checkPopup(ContextTreePanel.PREF_POP_SEPARATOR_4, SeparatorItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_LAYER_PROPERTY)) {
                        checkPopup(ContextTreePanel.PREF_POP_LAYER_PROPERTY, LayerPropertyItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_CONTEXT_PROPERTY)) {
                        checkPopup(ContextTreePanel.PREF_POP_CONTEXT_PROPERTY, ContextPropertyItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_STYLE_RULE_MIN)) {
                        checkPopup(ContextTreePanel.PREF_POP_STYLE_RULE_MIN, RuleMinScaleItem.class);
                    } else if (evt.getKey().equals(ContextTreePanel.PREF_POP_STYLE_RULE_MAX)) {
                        checkPopup(ContextTreePanel.PREF_POP_STYLE_RULE_MAX, RuleMaxScaleItem.class);
                    }

                }
            });




            contextTree.revalidate();

            MapContext[] contexts = Application.getInstance().getContexts();

            for (MapContext context : contexts) {
                contextTree.addContext(context);
            }

            contextTree.addTreeContextListener(treeContextListener);
        }

        return contextTree;
    }

    public static ContextTreeManager getInstance() {
        if (instance == null) {
            instance = new ContextTreeManager();
        }
        return instance;
    }
}
