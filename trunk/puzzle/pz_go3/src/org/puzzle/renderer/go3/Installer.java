/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
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
package org.puzzle.renderer.go3;

import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        try{
            String os = System.getProperty("os.name");
            String arch = System.getProperty("os.arch");

            if(os.toLowerCase().contains("mac")){
                System.out.println("-> loading mac lwjgl.");
                System.loadLibrary("lwjgl");
            }else{
                System.out.println("-> loading win or linux lwjgl.");
                if(arch.toLowerCase().contains("64")){
                    System.loadLibrary("lwjgl64");
                }else{
                    System.loadLibrary("lwjgl");
                }
            }


            
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
