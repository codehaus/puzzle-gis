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
 * Interface pour l'�coute d'un ProcessObject
 * @author johann Sorel
 */
public interface ProcessEventListener extends EventListener{
       
   /**
    * se d�clenche sur le d�marrage du processObject
    * @param obj 
    */
   public void processStarted(ProcessObject obj);
   
   /**
    * nouvelles entr�es du processObejct
    * @param obj : processObject
    * @param in : dernier message d'entr�e
    * @param out : dernier message d'erreur
    */
   public void processStateUpdated(ProcessObject obj, String in, String out);
   
   /**
    * se d�clenche sur la fin d'�xecution du processObject
    * @param obj 
    */
   public void processStopped(ProcessObject obj);
    
}
