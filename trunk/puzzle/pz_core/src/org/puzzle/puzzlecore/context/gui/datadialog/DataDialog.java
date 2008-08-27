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
package org.puzzle.puzzlecore.context.gui.datadialog;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import org.geotools.gui.swing.datachooser.DataPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author  Johann Sorel
 */
public class DataDialog extends JDialog {

    private final WaitingGlass glass = new WaitingGlass();
    private final BlurPanel blur = new BlurPanel();

    public static enum ACTION {

        APPROVE,
        CANCEL,
        CLOSE
    };
    private ACTION exitmode = ACTION.CLOSE;
    private List<DataPanel> types = new ArrayList<DataPanel>();
    private DataPanel activDataPanel = null;
    private JTabbedPane tabbedpane = new JTabbedPane();

    public DataDialog(java.awt.Frame parent, List<DataPanel> type) {
        super(parent, true);

        blur.setLayout(new BorderLayout());

        blur.add(BorderLayout.CENTER, type.get(0).getChooserComponent());
        blur.revalidate();

        setContentPane(blur);
        setGlassPane(glass);

        setSize(640, 480);
        setLocationRelativeTo(null);
        

        Thread t = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(2000);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    setLoading(true);
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    setLoading(false);
                }
            }
        };
        t.start();
        
        setVisible(true);
    }

    public void setLoading(boolean b) {
        blur.setBlur(b);
        getGlassPane().setVisible(b);
        repaint();
    }
}
