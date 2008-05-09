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

package org.puzzle.shell.misc.runtime;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;
import org.jdesktop.swingx.JXTable;
import org.openide.util.Utilities;
import org.puzzle.shell.misc.runtime.tabletool.ProcessCellEditor;
import org.puzzle.shell.misc.runtime.tabletool.ProcessCellRenderer;
import org.puzzle.shell.misc.runtime.tabletool.ProcessHeaderRenderer;

/** 
 * 
 * @author johann Sorel
 */
public class ProcessObjectPanel extends JPanel implements ProcessEventListener{
        
    private final static ImageIcon ICON_WARNING = new ImageIcon(Utilities.loadImage("org/puzzle/shell/misc/runtime/warning.png"));
    private final static ImageIcon ICON_ERROR = new ImageIcon(Utilities.loadImage("org/puzzle/shell/misc/runtime/error.png"));
    private final static String WARNING = ResourceBundle.getBundle("org/puzzle/shell/misc/runtime/Bundle").getString("warning");
    private final static String ERROR = ResourceBundle.getBundle("org/puzzle/shell/misc/runtime/Bundle").getString("error");
    
    
    private ArrayList<ProcessObject> liste = new ArrayList<ProcessObject>();
    private ProcessTableModel modele;
    private JXTable table ;
    private JScrollPane jsp;
    
    private boolean marche = false;
    private int numencours = 0;
    private int nummax = 0;
    private int numsimul = 2;
    private int compte = 0;
    
    private StatusListener.STATUS myState = StatusListener.STATUS.WAITING;
        
    private EventListenerList listeners	= new EventListenerList();
    
    
    /** Creates a new instance of ProcessObjectTable */
    public ProcessObjectPanel(){
        super(new BorderLayout());
        modele = new ProcessTableModel();
        table = new JXTable(modele);
        table.setRowHeight(22);
        table.setHorizontalScrollEnabled(false);
        
        table.getColumnExt(1).setMaxWidth(100);
        table.getColumnExt(2).setMaxWidth(100);
        
        table.getColumnModel().getColumn(0).setHeaderRenderer(new ProcessHeaderRenderer());
        table.getColumnModel().getColumn(1).setHeaderRenderer(new ProcessHeaderRenderer());
        table.getColumnModel().getColumn(1).setHeaderValue(new JLabel(WARNING, ICON_WARNING, JLabel.CENTER));
        table.getColumnModel().getColumn(2).setHeaderRenderer(new ProcessHeaderRenderer());
        table.getColumnModel().getColumn(2).setHeaderValue(new JLabel(ERROR, ICON_ERROR, JLabel.CENTER));
        
        table.getColumn(0).setCellRenderer(new ProcessCellRenderer());
        table.getColumn(1).setCellRenderer(new ProcessCellRenderer());
        table.getColumn(2).setCellRenderer(new ProcessCellRenderer());
        table.getColumn(1).setCellEditor(new ProcessCellEditor(ProcessCellEditor.INBUTTON));
        table.getColumn(2).setCellEditor(new ProcessCellEditor(ProcessCellEditor.ERRORBUTTON));
        
        table.setShowGrid(false,true);
        table.setPreferredSize(new Dimension(100,100));
        setPreferredSize(new Dimension(100,100));
        
        jsp = new JScrollPane(table);
        jsp.setPreferredSize(new Dimension(100,100));
        add(BorderLayout.CENTER,jsp);
        
    }
        
    /**
     * Ajouter un ProcessObject � la liste
     * @param po
     */
    public void addProcess(ProcessObject po){
        po.addProcessEventListener(this);
        getListe().add(po);
        modele.addLine(po);
    }
    
    /**
     * vider la liste des ProcessObject
     */
    public void empty(){
        for(int i=0;i<getListe().size();i++){
            getListe().get(i).removeProcessEventListener(this);
        }
        liste = new ArrayList<ProcessObject>();
        modele.empty();
        compte = 0;
        nummax = 0;
        numencours = 0;
    }
    
    /**
     * d�marrer l'�xecution de la liste de ProcessObject
     */
    public void start(){
        myState = StatusListener.STATUS.WORKING;
        fireStateUpdated();
        
        if(marche == false){
            marche = true;
            nummax = 0;
            numencours = 0;
            
            while( numencours<numsimul && nummax<getListe().size() ){
                getListe().get(nummax).setPriority(Thread.MIN_PRIORITY);
                getListe().get(nummax).start();
                nummax++;
                numencours++;
            }
            
            marche = false;
        }
        
        if(getListe().size() == 0){
            myState = StatusListener.STATUS.WAITING;
        fireStateUpdated();
        }
        
    }
    
    /**
     * 
     * @return est actif ou non
     */
    public boolean isWorking(){
        return marche;
    }
    
    
    public int getProcessNumber(){
        return getListe().size();
    }
    
    private synchronized void nextProcess(){
        compte++;
        if( nummax<getListe().size() ){
            getListe().get(nummax).start();
            nummax++;            
        }
        
        if(compte == getListe().size()){
            myState = StatusListener.STATUS.WAITING;
            fireStateUpdated();
        }
    }
    
    
    /**
     * @return nombre d'execution en simultan�e
     */
    public int getParallelNumber() {
        return numsimul;
    }
    
    /**
     * nombre d'execution simultan�e de ProcessObject 
     */
    public void setParallelNumber(int numsimul) {
        this.numsimul = numsimul;
    }
    
    private void fireStateUpdated(){
        StatusListener[] lst = getStatusListeners();
        for(int i =0;i<lst.length;i++){
            lst[i].statusUpdated(myState);
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    

    public void processStateUpdated(ProcessObject pro, String in, String out) {
        int row = getListe().indexOf(pro);
        modele.fireCellUpdated(row,0);
        modele.fireCellUpdated(row,1);
        modele.fireCellUpdated(row,2);
    }
    

    public void processStarted(ProcessObject p) {
        final ProcessObject pro = p;
        final int row = getListe().indexOf(pro);
        
        
        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while( pro.getStatus() != ProcessObject.STATUS.FINISHED && pro.getStatus() != ProcessObject.STATUS.ALERTS && pro.getStatus() != ProcessObject.STATUS.ERRORS  ){
                        sleep(100);
                        modele.fireCellUpdated(row,0);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    

    public void processStopped(ProcessObject pro) {
        
        int row = getListe().indexOf(pro);
        modele.fireCellUpdated(row,0);
        modele.fireCellUpdated(row,1);
        modele.fireCellUpdated(row,2);
        nextProcess();
    }
    
    
    
      
    /** S ajouter a l ecoute 
     * @param con : StatusEventLitener
     */
    public void addStatusListener(StatusListener con ){
        listeners.add(StatusListener.class, con);
    }
    
    /** retirer un element 
     * @param con
     */
    public void removeStatusListener( StatusListener con ){
        listeners.remove(StatusListener.class, con);
    }
    
    /** retourne toutes les StatusListeners.
     * @return StatusListener[]
     */
    public StatusListener[] getStatusListeners(){
        return listeners.getListeners(StatusListener.class);
    }

    public ArrayList<ProcessObject> getListe() {
        return liste;
    }
    
    
}