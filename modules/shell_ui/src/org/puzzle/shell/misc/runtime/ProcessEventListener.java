/*
 * ProcessEventListener.java
 *
 * Cr�� le 1 f�vrier 2007, 15:06
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
