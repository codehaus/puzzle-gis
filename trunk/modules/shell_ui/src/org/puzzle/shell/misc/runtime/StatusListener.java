/*
 * StatusListener.java
 *
 * Cr�� le 15 mars 2007, 09:21
 *
 * Projet de Convertisseur de format SIG,
 * Projet tutor� SI17 2007
 */

package org.puzzle.shell.misc.runtime;

import java.util.EventListener;

/**
 * interface pour l'ecoute d'un ProcessObjectPanel
 * @author johann Sorel
 */
public interface StatusListener extends EventListener{
    
    /**
     * Etat du ProcessObjectPanel
     */
    public static enum STATUS{
        /**
         * en Attente
         */
        WAITING,
        /**
         * actif
         */
        WORKING,
        /**
         * termin�, avec erreurs
         */
        ERROR
    };
    
    
    /**
     * changement de l'�tat du panneau
     * @param stat 
     */
    public void statusUpdated(STATUS stat);
    
}
