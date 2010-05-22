/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2010, Johann Sorel
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
package org.puzzle.core.view;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Image;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;

import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.puzzle.core.resources.CoreResource;

/**
 * Panel to choose the rendering engine.
 * It can used in a wizard.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class RendererChooser implements WizardDescriptor.Panel {

    private final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
    private final JList lst = new JList();
    private boolean flagok = false;
    
    
    public RendererChooser(){

        SortedSet<RenderingService> set = new TreeSet<RenderingService>(new Comparator<RenderingService>() {
            @Override
            public int compare(RenderingService c1, RenderingService c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });
        set.addAll(services);

        final DefaultListModel model = new DefaultListModel();
        for(RenderingService serv : set){
            model.addElement(serv);
        }
        lst.setModel(model);
        lst.setCellRenderer(new ServiceRenderer());
        lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lst.setSelectedIndex(0);
    }
        
    public RenderingService getSelectedService(){        
        if(flagok){
            return (RenderingService) lst.getSelectedValue();
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
        wizardDescriptor.setTitle(CoreResource.getString("chooseRenderer"));
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        final boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
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
        final JScrollPane component = new JScrollPane(lst);
        component.setName(CoreResource.getString("chooseRenderer"));
        return component;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValid() {
        return true;
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

    private static class ServiceRenderer extends DefaultListCellRenderer{

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if(value instanceof RenderingService){
                final RenderingService serv = (RenderingService) value;
                final Image img = serv.getIcon();
                lbl.setIcon(img==null?null:new ImageIcon(img));
                lbl.setText(serv.getTitle());
            }

            return lbl;
        }

    }
    
}
