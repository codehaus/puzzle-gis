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

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collection;
import javax.swing.SwingUtilities;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.puzzle.core.project.filetype.GISContextDataObject;
import org.puzzle.core.view.RenderingService;
import org.puzzle.core.view.ViewComponent;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class GISView{

    private final GISContextDataObject contextData;
    private final GISViewInfo info;
    private SoftReference<ViewComponent> compRef;

    public GISView(GISContextDataObject context, GISViewInfo info) {
        this.contextData = context;
        this.info = info;
    }

    public String getTitle(){
        return info.getTitle();
    }

    public void setTitle(final String title){
        info.setTitle(title);

        if(compRef != null){
            final ViewComponent comp = compRef.get();

            if(comp == null) return;

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    comp.setDisplayName(title);
                }
            });
        }

    }

    public ViewComponent getComponent(boolean create) {

        ViewComponent comp = null;

        if(compRef == null){
            if(create){
                final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
                for(RenderingService service : services){
                    String id = service.getIdentifier();
                    if(id.equals(info.getServiceName())){
                        //this is the correct rendering engine
                        comp = service.restoreView(contextData.getContext(), this);
                        compRef = new SoftReference<ViewComponent>(comp);
                        break;
                    }
                }
            }
        }else{
            comp = compRef.get();

            if(comp == null && create){
                final Collection<? extends RenderingService> services = Lookup.getDefault().lookupAll(RenderingService.class);
                for(RenderingService service : services){
                    String id = service.getIdentifier();
                    if(id.equals(info.getServiceName())){
                        //this is the correct rendering engine
                        comp = service.restoreView(contextData.getContext(), this);
                        compRef = new SoftReference<ViewComponent>(comp);
                        break;
                    }
                }
            }
        }

        return comp;
    }

    public boolean isDisplayed(){
        return compRef != null;
    }

    public GISContextDataObject getContext() {
        return contextData;
    }

    public GISViewInfo getInfo() {
        return info;
    }

}
