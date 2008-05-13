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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.EventListenerList;

/**
 * Classe permettant d'�x�cuter un processus externe
 * @author johann sorel
 */
public class ProcessObject extends Thread{
    
    /**
     * etat du processObejct
     */
    public static enum STATUS{
        /**
         * est en execution
         */
        WORKING,
        /**
         * est termin� avec des alertes
         */
        ALERTS,
        /**
         * es termin� avec des erreurs
         */
        ERRORS,
        /**
         * est termin� sans errreurs
         */
        FINISHED,
        /**
         * est en attente
         */
        WAITING
    };
    
      
    private String[] parametres;
    private String parametre;
    private String[] env;
    private File dir;
    private String name = "";
    private String texte = "";
    private Process process;
    
    private List<String> lst_in = new ArrayList<String>();
    private List<String> lst_err = new ArrayList<String>();
    
    private boolean b_in_finish = false;
    private boolean b_err_finish = false;
    
    private String lastIN = null;
    private String lastERROR = null;
    
    
    private long timedep = 0;
    private long timeend = 0;
    private Date time = null;
    
    private STATUS myState = STATUS.WAITING;
    
    private EventListenerList listeners	= new EventListenerList();
    
    
    
    /**
     *  cr�ation d'un processObject
     * @param parametre : parametres d'execution
     * @param name  : nom du processus
     */
    public ProcessObject(String parametre, String name){
        this.parametre = parametre;
        this.name = name;
        texte = name;
    }
    
    /**
     * cr�ation d'un processObject
     * @param parametre : parametres d'execution
     * @param env : parametres d'environnement
     * @param name : nom du processus
     */
    public ProcessObject(String parametre, String[] env, String name) {
        this.parametre = parametre;
        this.env = env;
        this.name = name;
        texte = name;
    }
    
    /**
     * cr�ation d'un processObject
     * @param parametre : parametres d'execution
     * @param env  : parametres d'environnement
     * @param dir : repertoire d'execution
     * @param name  : nom du processus
     */
    public ProcessObject(String parametre, String[] env, File dir, String name){
        this.parametre = parametre;
        this.env = env;
        this.dir = dir;
        this.name = name;
        texte = name;
    }
    
    /**
     * cr�ation d'un processObject
     * @param parametres : parametres d'execution
     * @param name  : nom du processus
     */
    public ProcessObject(String[] parametres,String name){
        this.parametres = parametres;
        this.name = name;
        texte = name;
    }
    
    /**
     * cr�ation d'un processObject
     * @param parametres  : parametres d'execution
     * @param env : parametres d'environnement
     * @param name  : nom du processus
     */
    public ProcessObject(String[] parametres, String[] env, String name){
        this.parametres = parametres;
        this.env = env;
        this.name = name;
        texte = name;
    }
    
    /**
     * cr�ation d'un processObject
     * @param parametres  : parametres d'execution
     * @param env : parametres d'environnement
     * @param dir : repertoire d'execution
     * @param name  : nom du processus
     */
    public ProcessObject(String[] parametres, String[] env, File dir, String name){
        this.parametres = parametres;
        this.env = env;
        this.dir = dir;
        this.name = name;
        texte = name;
    }
    
    
    @Override
    public void run(){
        myState = STATUS.WORKING;
        texte = "En cours : " + name;
        time = new Date();
        timedep = time.getTime();
        
        fireStartProcess();
        
        try{
            if(getParametre() != null){
                if(getEnv() != null){
                    if(getDir() != null){
                        process = Runtime.getRuntime().exec(getParametre(), getEnv(), getDir());
                    }
                    else{
                        process = Runtime.getRuntime().exec(getParametre(), getEnv());
                    }
                }
                else{
                    process = Runtime.getRuntime().exec(getParametre());
                }
            }
            else if (getParametres() != null){
                 if(getEnv() != null){
                    if(getDir() != null){
                        process = Runtime.getRuntime().exec(getParametres(), getEnv(), getDir());
                    }
                    else{
                        process = Runtime.getRuntime().exec(getParametres(), getEnv());
                    }
                }
                else{
                    process = Runtime.getRuntime().exec(getParametres());
                }
            }
            
        } catch(Exception e){
            lastERROR = e.getMessage();
            lst_err.add(lastERROR);
            fireStateUpdated();
        }
        
        //traitement des entrees
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = "";
                    try {
                        while((line = reader.readLine()) != null) {
                            lastIN = line;
                            lst_in.add(line);
                            fireStateUpdated();
                        }
                    } finally {
                        reader.close();
                    }
                } catch(IOException ioe) {
                    lastERROR = ioe.getMessage();
                    lst_err.add(lastERROR);
                    fireStateUpdated();
                } finally{
                    b_in_finish = true;
                    statStop();
                }
            }
        }.start();
        
        
        // traitement des erreurs
        new Thread() {
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = "";
                    try {
                        while((line = reader.readLine()) != null) {
                            lastERROR = line;
                            lst_err.add(line);
                            fireStateUpdated();
                        }
                    } finally {
                        reader.close();
                    }
                } catch(IOException ioe) {
                    lastERROR = ioe.getMessage();
                    lst_err.add(lastERROR);
                    fireStateUpdated();
                }finally{
                    b_err_finish = true;
                    statStop();
                }
            }
        }.start();
        
        
    }
    
    
    
    private void statStop(){
        if(b_err_finish && b_in_finish){
            
            time = new Date();
            timeend = time.getTime();
            float val = Math.round((timeend-timedep)/10);
            texte = " -> " + name +" : "+ val/100 + " sec.";
            
            if(lst_err.size() > 0) myState = STATUS.ERRORS;
            else if(lst_in.size() > 0) myState = STATUS.ALERTS;
            else  myState = STATUS.FINISHED;
            
            fireStopProcess();
        }
        
    }
    
    private void fireStopProcess() {
        ProcessEventListener[] lst = getProcessEventListeners();
        for(int i =0;i<lst.length;i++){
            lst[i].processStopped(this);
        }
    }
    
    private void fireStartProcess(){
        ProcessEventListener[] lst = getProcessEventListeners();
        for(int i =0;i<lst.length;i++){
            lst[i].processStarted(this);
        }
    }
    
    private void fireStateUpdated(){
        ProcessEventListener[] lst = getProcessEventListeners();
        for(int i =0;i<lst.length;i++){
            lst[i].processStateUpdated(this,lastIN,lastERROR);
        }
        lastIN = null;
        lastERROR = null;
    }
        
    /**     
     * @return liste des chaines entr�es
     */
    public synchronized List<String> getInList(){
        return lst_in;
    }
    
    /**
     * @return liste des chaines d'erreurs 
     */
    public synchronized List<String> getErrorList(){
        return lst_err;
    }
    
    private synchronized int getInNumber(){
        return lst_in.size();
    }
    
    private synchronized int getErrorNumber(){
        return lst_err.size();
    }
    
    /**
     *
     * @return processObject format chaine
     */
    public synchronized String getString(){
        return getTexte();
    }
    
    /**
     *
     * @return etat du processObject, ProcessObject.STATUS
     */
    public STATUS getStatus(){
        return  myState;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    
    /** S ajouter a l ecoute du processus.
     * @param con : ConnectionEventLitener
     */
    public void addProcessEventListener(ProcessEventListener con ){
        listeners.add(ProcessEventListener.class, con);
    }
    
    /** retirer un element ProcessEventListener.
     * @param con
     */
    public void removeProcessEventListener( ProcessEventListener con ){
        listeners.remove(ProcessEventListener.class, con);
    }
    
    /** retourne toutes les ProcessEventListener.
     * @return ProcessEventListener[]
     */
    public ProcessEventListener[] getProcessEventListeners(){
        return listeners.getListeners(ProcessEventListener.class);
    }

    public String[] getParametres() {
        return parametres;
    }

    public String getParametre() {
        return parametre;
    }

    public String[] getEnv() {
        return env;
    }

    public File getDir() {
        return dir;
    }

    public String getTexte() {
        return texte;
    }
    
    
    
    
}
