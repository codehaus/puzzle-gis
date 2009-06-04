/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
