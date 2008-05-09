/*
 * JRuntimePanel.java
 *
 * Cr�� le 31 janvier 2007, 14:00
 *
 * Copyright 2004,2007 Johann Sorel
 *
 * JSL-ToolBox is free library; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JSL-ToolBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.puzzle.shell.misc.runtime;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.openide.util.Exceptions;

/** 
 * @author johann Sorel
 * Permet d'afficher dans une zone de texte les messages g�n�r�s par un ProcessObject
 */
public class JRuntimePanel extends JPanel implements ProcessEventListener{
    
    /**
     * d�termine � quel type d'entr�e le composant r�agit
     */
    public static enum TYPE{
        /**
         *  �coute uniquement les entr�es
         */
        LISTEN_IN,
        /**
         * �coute uniquement les retours d'erreurs
         */
        LISTEN_ERROR,
        /**
         * �coute les entr�es et les erreurs
         */
        LISTEN_BOTH,
        /**
         * n'�coute rien
         */
        LISTEN_NONE
    };
    
    
    private JTextPane text = new JTextPane();
    private Document doc = new DefaultStyledDocument();
    private AttributeSet attr = null;
    private JScrollPane jsp = new JScrollPane(text);
    private ProcessObject po = null;
        
    private TYPE type = TYPE.LISTEN_NONE;

    
    /** 
     * Cr�er un JRuntimePanel
     * @param p : processObject a �couter
     * @param type : type d'�coute, JRuntime.TYPE.LISTEN_IN,JRuntime.TYPE.LISTEN_ERROR,JRuntime.TYPE.LISTEN_BOTH,JRuntime.TYPE.LISTEN_NONE
     */
    public JRuntimePanel(ProcessObject p, TYPE type) {
        super(new GridLayout(1,1));
        po = p;
        this.type = type;
        po.addProcessEventListener(this);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        text.setEditable(false);
        text.setDocument(doc);
        
        StyleContext sc = StyleContext.getDefaultStyleContext();
        attr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
                    
        add(jsp);
                       
         switch (type){
            case LISTEN_IN :
                for(int i=0;i<po.getInList().size();i++)
                    addText(po.getInList().get(i),false);
                break;
            case LISTEN_ERROR :
                for(int i=0;i<po.getErrorList().size();i++)
                    addText(po.getErrorList().get(i),true);
                break;
            case LISTEN_BOTH :
                for(int i=0;i<po.getInList().size();i++)
                    addText(po.getInList().get(i),false);
                for(int i=0;i<po.getErrorList().size();i++)
                    addText(po.getErrorList().get(i),true);
                break;
        }
        
    }
    
    
    private void addText(String str, boolean error){
        if(error){
                        
            try {
                doc.insertString(doc.getLength(), str + "\n", attr);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else{
            try {
                doc.insertString(doc.getLength(), str + "\n", null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
    }
    
    public void processStarted(ProcessObject po) {
    }
    
    public void processStateUpdated(ProcessObject po,String in, String err) {
        
        switch (type){
            case LISTEN_IN :
                if(in != null)
                    addText(in,false);
                break;
            case LISTEN_ERROR :
                if(err != null)
                    addText(err,true);
                break;
            case LISTEN_BOTH :
                if(in != null)
                    addText(in,false);
                if(err != null)
                    addText(err,true);
                break;
        }
        
    }
    
    public void processStopped(ProcessObject po) {
        addText("Process Ended",false);
    }
    
}
