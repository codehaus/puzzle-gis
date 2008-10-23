/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.core.view;

import java.util.ResourceBundle;

/**
 * I18N for this package
 * @author Johann Sorel (Puzzle-GIS)
 */
final class Utilities {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org/puzzle/core/view/Bundle");

    static final String getString(final String key){
        return BUNDLE.getString(key);
    }

}
