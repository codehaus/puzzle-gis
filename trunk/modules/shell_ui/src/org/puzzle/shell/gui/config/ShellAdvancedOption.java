/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.shell.gui.config;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class ShellAdvancedOption extends AdvancedOption {

    public String getDisplayName() {
        return NbBundle.getMessage(ShellAdvancedOption.class, "AdvancedOption_DisplayName_Shell");
    }

    public String getTooltip() {
        return NbBundle.getMessage(ShellAdvancedOption.class, "AdvancedOption_Tooltip_Shell");
    }

    public OptionsPanelController create() {
        return new ShellOptionsPanelController();
    }
}
