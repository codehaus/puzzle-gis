/*
 *  Puzzle-GIS - OpenSource mapping program
 *  http://docs.codehaus.org/display/PUZZLEGIS
 *  Copyright (C) 2007-2008 Puzzle-GIS
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
package org.puzzle.puzzlecore.struct;

import java.awt.BorderLayout;
import org.geotools.gui.swing.map.map2d.JDefaultEditableMap2D;
import org.geotools.gui.swing.map.map2d.Map2D;
import org.openide.windows.TopComponent;

/**
 *
 * @author johann sorel
 */
public class MapView extends TopComponent {

    protected Map2D map = new JDefaultEditableMap2D();
    protected MapGroup group = null;

    private boolean scaleLink = false;
    private boolean rotationLink = false;
    private boolean translationLink = false;
    
    public MapView(){
        super();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,map.getComponent());
        revalidate();
    }
    
    public Map2D getMap() {
        return map;
    }

    public void setMap(Map2D map) {

        if (map == null) {
            throw new NullPointerException();
        }
        this.map = map;
        
        removeAll();
        add(BorderLayout.CENTER,map.getComponent());
        revalidate();
    }

    public MapGroup getGroup() {
        return group;
    }

    public void setGroup(MapGroup group) {
        this.group = group;
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        CORE.getViewManager().removeView(this);
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        CORE.getViewManager().setActiveContext(this);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    
    
    
    public boolean isScaleLink() {
        return scaleLink;
    }

    public void setScaleLink(boolean scaleLink) {
        this.scaleLink = scaleLink;
    }

    public boolean isRotationLink() {
        return rotationLink;
    }

    public void setRotationLink(boolean rotationLink) {
        this.rotationLink = rotationLink;
    }

    public boolean isTranslationLink() {
        return translationLink;
    }

    public void setTranslationLink(boolean translationLink) {
        this.translationLink = translationLink;
    }
    
    
    
}
