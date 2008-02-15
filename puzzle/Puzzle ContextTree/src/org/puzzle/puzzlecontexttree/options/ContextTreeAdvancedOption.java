/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007 Puzzle-GIS
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
package org.puzzle.puzzlecontexttree.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

/**
 * 
 * @author johann sorel
 */
public final class ContextTreeAdvancedOption extends AdvancedOption {

    public String getDisplayName() {
        return NbBundle.getMessage(ContextTreeAdvancedOption.class, "AdvancedOption_DisplayName_ContextTree");
    }

    public String getTooltip() {
        return NbBundle.getMessage(ContextTreeAdvancedOption.class, "AdvancedOption_Tooltip_ContextTree");
    }

    public OptionsPanelController create() {
        return new ContextTreeOptionsPanelController();
    }
}
