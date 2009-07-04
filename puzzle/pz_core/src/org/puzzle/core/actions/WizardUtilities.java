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
package org.puzzle.core.actions;

import java.awt.Component;
import java.text.MessageFormat;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public class WizardUtilities {

    public static WizardDescriptor createSimplewWizard(JComponent comp, String name) {
        final WizardDescriptor wdesc = new WizardDescriptor(toWizardPanes(comp, name));
        wdesc.setTitleFormat(new MessageFormat("{0}"));
        wdesc.setTitle(name);
        wdesc.setModal(true);

        return wdesc;
    }

    private static WizardDescriptor.Panel<WizardDescriptor>[] toWizardPanes(JComponent comp, String name) {
        final WizardDescriptor.Panel<WizardDescriptor>[] panels = new WizardDescriptor.Panel[1];
        panels[0] = new SimpleWizardPanel(comp, name);
        return panels;
    }

    private static class SimpleWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

        private final EventListenerList listeners = new EventListenerList();
        private final JComponent comp;

        SimpleWizardPanel(JComponent comp, String name) {
            this.comp = comp;

            final String[] steps = new String[1];
            steps[0] = name;

            comp.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(0));
            comp.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            comp.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
            comp.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
            comp.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.FALSE);
        }

        @Override
        public Component getComponent() {
            return comp;
        }

        @Override
        public HelpCtx getHelp() {
            return HelpCtx.DEFAULT_HELP;
        }

        @Override
        public void readSettings(WizardDescriptor arg0) {
        }

        @Override
        public void storeSettings(WizardDescriptor arg0) {
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            listeners.add(ChangeListener.class, l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            listeners.remove(ChangeListener.class, l);
        }

        protected final void fireChangeEvent() {
            ChangeEvent ev = new ChangeEvent(this);
            ChangeListener[] lst = listeners.getListeners(ChangeListener.class);

            for (ChangeListener cl : lst) {
                cl.stateChanged(ev);
            }

        }
    }
}
