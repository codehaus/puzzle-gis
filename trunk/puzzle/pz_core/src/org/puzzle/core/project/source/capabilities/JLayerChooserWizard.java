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
package org.puzzle.core.project.source.capabilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.MessageFormat;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.geotoolkit.map.MapContext;
import org.geotoolkit.map.MapLayer;

import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JLayerChooserWizard extends JPanel implements WizardDescriptor.Panel, LayerChooserMonitor {

    private final EventListenerList listeners = new EventListenerList();
    private final Collection<? extends MapContext> contexts;
    private boolean valid = false;

    JLayerChooserWizard(Collection<MapContext> contexts) {
        this.contexts = contexts;
        initComponents();
        toolbar.setLayout(new FlowLayout());

        guiContexts.setModel( new DefaultComboBoxModel(contexts.toArray()) );
        guiContexts.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if(value instanceof MapContext){
                    String title = ((MapContext)value).getDescription().getTitle().toString();
                    return super.getListCellRendererComponent(list, title, index, isSelected, cellHasFocus);
                }else{
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }
        });

        guiContexts.setSelectedItem(contexts.iterator().next());

    }

    @Override
    public String getName() {
        return Utilities.getString("chooselayer");
    }

    public void setChooser(JComponent component){
        add(BorderLayout.CENTER,component);
    }

    public MapContext getContext(){
        return (MapContext) guiContexts.getSelectedItem();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbar = new JToolBar();
        jLabel1 = new JLabel();
        guiContexts = new JComboBox();

        setLayout(new BorderLayout());

        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | Font.BOLD));
        jLabel1.setText(Utilities.getString("map")); // NOI18N
        toolbar.add(jLabel1);

        guiContexts.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        toolbar.add(guiContexts);

        add(toolbar, BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JComboBox guiContexts;
    private JLabel jLabel1;
    private JToolBar toolbar;
    // End of variables declaration//GEN-END:variables


    /**
     * Show a wizard to choose the rendering engine and return the resulting
     * map view.
     * @return MapView or null if wizard was canceled.
     */
    public static void showChooserDialog(final Collection<MapContext> contexts, final LayerCreation lc){
        final JLayerChooserWizard chooser = new JLayerChooserWizard(contexts);
        final JLayerChooser comp = lc.createChooser(chooser);
        chooser.setChooser(comp);
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(chooser.getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(Utilities.getString("chooselayer"));
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            MapContext context = chooser.getContext();
            MapLayer[] layers = comp.getLayers();

            for(MapLayer layer : layers){
                context.layers().add(layer);
            }
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
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.FALSE);
                }
            }
        return panels;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getComponent() {
        return this;
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
        return valid;
    }


    /**
     * {@inheritDoc }
     */
    @Override
    public final void addChangeListener(ChangeListener l) {
        listeners.add(ChangeListener.class, l);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public final void removeChangeListener(ChangeListener l) {
        listeners.remove(ChangeListener.class, l);
    }

    protected final void fireChangeEvent() {
        ChangeEvent ev = new ChangeEvent(this);
        ChangeListener[] lst = listeners.getListeners(ChangeListener.class);

        for(ChangeListener cl : lst){
            cl.stateChanged(ev);
        }

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

    @Override
    public void setReady(boolean ready) {
        valid = ready;
        fireChangeEvent();
    }

}
