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

package org.puzzle.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Panel to choose the rendering engine.
 * It can used in a wizard.
 * 
 * @author Johann Sorel (Puzzle-GIS)
 */
public class RendererChooser implements WizardDescriptor.Panel {

    private final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
    private final ButtonGroup group = new ButtonGroup();
    private final Map<JRadioButton,RenderingService> link = new HashMap<JRadioButton, RenderingService>();
    private final JPanel component = new JPanel();
    private boolean flagok = false;
    
    
    public RendererChooser(){
        component.setLayout(new GridLayout(services.size(),1));
        
        for(final RenderingService service : services){
            component.add(createServicePane(service));
        }

        component.setName(Utilities.getString("chooseRenderer"));
    }
    
    private JPanel createServicePane(RenderingService service){
        JPanel pane = new JPanel(new BorderLayout());
        JLabel label = new JLabel(new ImageIcon(service.getIcon()));
        JRadioButton radio = new JRadioButton(service.getTitle(),true);
        group.add(radio);
        pane.add(BorderLayout.WEST,label);
        pane.add(BorderLayout.CENTER,radio);
        link.put(radio, service);
        radio.setSelected(true);
        return pane;        
    }    
    
    public RenderingService getSelectedService(){
        
        if(flagok){
            Set<JRadioButton> radios = link.keySet();

            for(JRadioButton radio : radios){
                if(radio.isSelected()){
                    return link.get(radio);
                }
            }
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
        wizardDescriptor.setTitle(Utilities.getString("chooseRenderer"));
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
