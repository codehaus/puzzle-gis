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
