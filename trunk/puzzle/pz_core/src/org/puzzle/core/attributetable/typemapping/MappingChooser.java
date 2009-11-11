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
package org.puzzle.core.attributetable.typemapping;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.puzzle.core.resources.CoreResource;

/**
 * Panel to choose the rendering engine.
 * It can used in a wizard.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class MappingChooser implements WizardDescriptor.Panel {

    private final JSplitPane component = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private boolean flagok = false;
    private final TypeGraph graph;
    private final TableModel model;
    
    public MappingChooser(SimpleFeatureType sourceType, SimpleFeatureType targetType){

        final List<AttributeDescriptor> validDescs = new ArrayList<AttributeDescriptor>();
        for(AttributeDescriptor desc : targetType.getAttributeDescriptors()){
            if(!(desc instanceof GeometryDescriptor)){
                validDescs.add(desc);
            }
        }

        final Object[][] datas = new Object[validDescs.size()][2];
        for(int i=0; i<validDescs.size(); i++){
            final AttributeDescriptor desc = validDescs.get(i);
            datas[i][0] = desc.getLocalName();
            datas[i][1] = desc.getDefaultValue();
        }

        model = new DefaultTableModel(datas,
                new String[]{CoreResource.getString("property"),CoreResource.getString("default")}){

            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                if(arg1 <1) return false;
                return super.isCellEditable(arg0, arg1);
            }
        };

        final JXTable table = new JXTable(model);
        table.setHighlighters(new Highlighter[]{HighlighterFactory.createAlternateStriping(Color.white, HighlighterFactory.QUICKSILVER, 1)});
        table.setShowGrid(true, true);
        table.setGridColor(Color.GRAY.brighter());


        graph = new TypeGraph(sourceType, targetType);

        component.setName("");
        component.setLeftComponent(new JScrollPane(table));
        component.setRightComponent(new JScrollPane(graph.createView()));
        component.setPreferredSize(new Dimension(620, 300));
        component.setDividerLocation(220);
        table.packAll();
    }
    
    
    public FeatureMapper getMapper(){
        
        if(flagok){
            final Map<AttributeDescriptor,Object> defaults = new HashMap<AttributeDescriptor, Object>();

            for(int i=0, n=model.getRowCount(); i<n; i++){
                String attName = model.getValueAt(i, 0).toString();
                Object value = model.getValueAt(i, 1);

                AttributeDescriptor desc = graph.getTargetType().getDescriptor(attName);
                if(desc != null){
                    defaults.put(desc, value);
                }
            }

            final Map<AttributeDescriptor,List<AttributeDescriptor>> mapping = graph.getMapping();
            return new DefaultFeatureMapper(graph.getSourceType(), graph.getTargetType(), mapping, defaults);
        }
        
        return null;
    }
    
    /**
     * Show a wizard to choose the rendering engine and return the resulting
     * map view.
     * @return MapView or null if wizard was canceled.
     */
    public void showChooserDialog(){

//        JOptionPane.showInputDialog(null, this, "gnagnagna", JOptionPane.OK_CANCEL_OPTION);

        final WizardDescriptor wizardDescriptor = new WizardDescriptor(this.getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(CoreResource.getString("configure_mapping"));
        wizardDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, false);
        wizardDescriptor.putProperty(WizardDescriptor.PROP_TITLE, false);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
           this.flagok = true;
        }
        
    }
        
    
    private WizardDescriptor.Panel[] panels;


    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
            panels = new WizardDescriptor.Panel[]{
                        this
                    };
            final String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                final Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) {
                    final JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
                }
            }
        return panels;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getComponent() {
        return component;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public final void addChangeListener(ChangeListener l) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public final void removeChangeListener(ChangeListener l) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void readSettings(Object settings) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void storeSettings(Object settings) {
    }
    
    
}
