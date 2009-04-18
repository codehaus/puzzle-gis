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
package org.puzzle.core.project.view;

import java.util.Collection;
import javax.swing.SwingUtilities;
import org.openide.util.Lookup;
import org.puzzle.core.project.filetype.GISContextDataObject;
import org.puzzle.core.view.RenderingService;
import org.puzzle.core.view.ViewComponent;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class GISView {

    private final GISContextDataObject contextData;
    private final GISViewInfo info;
    private ViewComponent component;
    private String title = "";

    public GISView(GISContextDataObject context, GISViewInfo info) {
        this.contextData = context;
        this.info = info;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(final String title){
        this.title = title;

        if(component != null){
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    component.setDisplayName(title);
                }
            });
        }

    }

    public ViewComponent getComponent() {
        if(component == null){
            final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
            for(RenderingService service : services){
                String id = service.getIdentifier();
                if(id.equals(info.getServiceName())){
                    //this is the correct rendering engine
                    component = service.restoreView(contextData.getContext(), this);
                    break;
                }
            }
        }

        return component;
    }

    public boolean isDisplayed(){
        return component != null;
    }

    public GISContextDataObject getContext() {
        return contextData;
    }

    public GISViewInfo getInfo() {
        return info;
    }

}
