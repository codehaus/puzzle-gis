/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.analyze.processing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.openide.util.ImageUtilities;
import org.puzzle.analyze.resources.AnalyzeResource;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JSequencePane extends JPanel{

    private static final ImageIcon ICON_EXEC = ImageUtilities.loadImageIcon("/org/puzzle/analyze/resources/execute.png", true);

    private final SequenceDataObject sequence;
    private final VMDGraphScene graph;

    public JSequencePane(final SequenceDataObject sequence){
        super(new BorderLayout());
        if(sequence == null){
            throw new NullPointerException("Sequence can not be null");
        }

        this.sequence = sequence;

        graph = new VMDGraphScene();


        add(BorderLayout.CENTER,new JScrollPane(graph.createView()));

        final JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.setLayout(new FlowLayout(SwingConstants.RIGHT));

        JButton but = new JButton(ICON_EXEC);
        but.setText(AnalyzeResource.getString("execute"));
        but.setHorizontalTextPosition(SwingConstants.LEFT);
        but.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sequence.execute();
            }
        });

        bar.add(but);

        add(BorderLayout.SOUTH,bar);
    }


}
