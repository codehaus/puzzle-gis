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
package org.puzzle.core.project.view;

import java.lang.ref.SoftReference;
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
