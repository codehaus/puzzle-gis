/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.process;

import org.openide.util.Lookup;

/**
 *
 * @author Administrateur
 */
public interface ProcessService extends Lookup.Provider{

    void addProcess(Process process);

    void removeProcess(Process process);
}
