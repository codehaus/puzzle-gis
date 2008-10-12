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
package org.puzzle.print.action;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.geotools.gui.swing.misc.filter.FileFilterFactory;
import org.geotools.gui.swing.misc.filter.FileFilterFactory.FORMAT;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.puzzle.core.view.MapView;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class SaveWizard extends javax.swing.JPanel implements WizardDescriptor.Panel {

    private final Collection views;
    private final List<String> lstViews;
    private File file = null;
    
    private SaveWizard(final Collection views){
        initComponents();
        this.views = views;
        lstViews = new ArrayList<String>();
        for(Object v : views){
            MapView view = (MapView) v;
            lstViews.add(view.getDisplayName());
        }

        ComboBoxModel model = new ListComboBoxModel(lstViews);
        guiView.setModel(model);
        guiFileChooser.setMultiSelectionEnabled(false);
        guiFileChooser.addChoosableFileFilter(FileFilterFactory.createFileFilter(FORMAT.PORTABLE_NETWORK_GRAPHICS));
        
        guiFileChooser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        guiFileChooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                file = guiFileChooser.getSelectedFile();
                fireChangeEvent();
            }
        });
        
    }

    private void save(){
        System.out.println("here");
        final int index = guiView.getSelectedIndex();

        if(index != -1 && file != null){
            List lst = new ArrayList(views);
            MapView view = (MapView) lst.get(index);
            Image img = view.getMap().getCanvas().getSnapShot();
            BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(img, 0, 0, null);
            System.out.println(file);
            try {
                ImageIO.write(bi, "image/png", file);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }


    @Override
    public String getName() {
        return "Export view as image.";
    }
    
    public static void showChooserDialog(final Collection views){
        final SaveWizard chooser = new SaveWizard(views);
        WizardDescriptor wizardDescriptor = new WizardDescriptor(chooser.getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Export view as image.");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;

        if (!cancelled) {
            chooser.save();
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
        return (file != null);
    // If it depends on some condition (form filled out...), then:
    // return someCondition();
    // and when this condition changes (last form field filled in...) then:
//     fireChangeEvent();
    // and uncomment the complicated stuff below.
    }

    EventListenerList listeners = new EventListenerList();
    
    @Override
    public final void addChangeListener(ChangeListener l) {
        listeners.add(ChangeListener.class, l);
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
        listeners.remove(ChangeListener.class, l);
    }

    private void fireChangeEvent(){
        ChangeListener[] lsts = listeners.getListeners(ChangeListener.class);
        for(ChangeListener lst : lsts){
            lst.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void readSettings(Object settings) {
    }

    @Override
    public void storeSettings(Object settings) {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        guiView = new javax.swing.JComboBox();
        guiFileChooser = new javax.swing.JFileChooser();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SaveWizard.class, "SaveWizard.jLabel1.text")); // NOI18N

        guiFileChooser.setAcceptAllFileFilterUsed(false);
        guiFileChooser.setControlButtonsAreShown(false);
        guiFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiView, 0, 427, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(guiFileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(guiView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiFileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser guiFileChooser;
    private javax.swing.JComboBox guiView;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
