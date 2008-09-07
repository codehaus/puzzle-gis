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

package org.puzzle.core.swing;

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
import org.geotools.map.MapContext;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.puzzle.core.view.MapView;
import org.puzzle.core.view.RenderingService;

/**
 * Panel to choose the Renderer.
 * 
 * @author Johann Sorel
 */
public class RendererChooser extends JPanel implements WizardDescriptor.Panel {

    private final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
    private final ButtonGroup group = new ButtonGroup();
    private final Map<JRadioButton,RenderingService> link = new HashMap<JRadioButton, RenderingService>();
    private boolean flagok = false;
    
    private RendererChooser(){
        setLayout(new GridLayout(1, services.size()));
        
        for(RenderingService service : services){
            add(createServicePane(service));
        }
        
    }

    @Override
    public String getName() {
        return "Choose renderer";
    }
       
    
    
    private JPanel createServicePane(RenderingService service){
        JPanel pane = new JPanel(new BorderLayout());
        JLabel label = new JLabel(new ImageIcon(service.getIcon()));
        JRadioButton radio = new JRadioButton(service.getTitle(),true);
        group.add(radio);
        pane.add(BorderLayout.WEST,label);
        pane.add(BorderLayout.CENTER,radio);
        link.put(radio, service);
        return pane;        
    }    
    
    public MapView getView(MapContext context){
        
        if(flagok){
            Set<JRadioButton> radios = link.keySet();

            for(JRadioButton radio : radios){
                if(radio.isSelected()){
                    return link.get(radio).createView(context);
                }
            }
        }
        
        return null;
    }
    
    
    public static MapView showChooserDialog(MapContext context){
        final RendererChooser chooser = new RendererChooser();
        WizardDescriptor wizardDescriptor = new WizardDescriptor(chooser.getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Choose renderer");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
           chooser.flagok = true;
        }
        
        return chooser.getView(context);
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
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        return panels;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
    }

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

    @Override
    public final void addChangeListener(ChangeListener l) {
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
    }
    
    @Override
    public void readSettings(Object settings) {
    }

    @Override
    public void storeSettings(Object settings) {
    }
    
    
}
