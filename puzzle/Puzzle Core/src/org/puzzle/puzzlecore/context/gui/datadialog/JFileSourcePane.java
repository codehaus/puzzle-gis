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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.Lookup;
import org.puzzle.puzzlecore.project.source.GISFileSourceService;
import org.puzzle.puzzlecore.project.source.GISSource;

/**
 * File generic chooser. This will call all GISFileSourceService
 * 
 * @author  Johann Sorel
 */
public class JFileSourcePane extends javax.swing.JPanel {
    
    private final Collection<? extends GISFileSourceService> services;
    
    public JFileSourcePane() {
       this(null);
    }
      
    /** 
     * Creates new form 
     * @param openPath
     * @param communPaths 
     */
    public JFileSourcePane(File openPath) {
        initComponents();
                
        services = Lookup.getDefault().lookupAll(GISFileSourceService.class);
        
        for(GISFileSourceService service : services){
            gui_choose.addChoosableFileFilter(service.createFilter());
        }
        
        if(openPath != null){
            gui_choose.setCurrentDirectory(openPath);
        }
        gui_choose.setMultiSelectionEnabled(true);

    }

    public void setDirectory(File directory){
        gui_choose.setCurrentDirectory(directory);
    }
    
    public File getDirectory(){
        return gui_choose.getCurrentDirectory();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gui_choose = new javax.swing.JFileChooser();

        gui_choose.setControlButtonsAreShown(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gui_choose, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gui_choose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser gui_choose;
    // End of variables declaration//GEN-END:variables

    public Collection<GISSource> getGISSources(){
        Collection<GISSource> sources = new ArrayList<GISSource>();
        File[] files = gui_choose.getSelectedFiles();
        
        file_loop:
        for(File f : files){
            
            for(GISFileSourceService service : services){
                GISSource source = null;
                
                try{ source = service.createSource(f);
                }catch(IllegalArgumentException ex){
                    ex.printStackTrace();}
                
                if(source != null){
                    sources.add(source);
                    continue file_loop;
                }
            }
        }
        
        return sources;
    }
    
}
