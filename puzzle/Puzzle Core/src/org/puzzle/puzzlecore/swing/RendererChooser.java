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

package org.puzzle.puzzlecore.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.geotools.map.MapContext;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.puzzle.puzzlecore.view.MapView;
import org.puzzle.puzzlecore.view.RenderingService;

/**
 * Panel to choose the Renderer.
 * 
 * @author Johann Sorel
 */
public class RendererChooser extends JPanel implements ActionListener{

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
        final RendererChooser pane = new RendererChooser();
        DialogDescriptor desc = new DialogDescriptor(pane, "Choose the renderer", true, pane);
        DialogDisplayer.getDefault().notify(desc);

        return pane.getView(context);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().toLowerCase().equals("ok")){
            flagok = true;
        }
    }
    
    
}
