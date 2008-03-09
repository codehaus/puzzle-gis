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

package org.puzzle.puzzlecore.struct.favorites;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTabbedPane;

/**
 *
 * @author johann sorel
 */
public class FavoritesManager {

    File origine = new File("favorites.ser");
    
    private static FavoritesManager instance = null;
    
    private Map<FavoriteType,List> favoritesMap = new HashMap<FavoriteType,List>();
    
    private FavoritesManager(){
        
        if(origine.exists()){
            
            
            
        }
        
    }
    
    public void registerType(FavoriteType type){
        if(!favoritesMap.containsKey(type)){
            favoritesMap.put(type,type.createEmptyList());
        }
    }
    
    public boolean isRegistered(FavoriteType type){
        return favoritesMap.containsKey(type);
    }
        
    public void addFavorite(FavoriteType type, Object favorite){
        favoritesMap.get(type).add(favorite);
    }
  
    public Component getManagerPanel(){
        JTabbedPane pane = new JTabbedPane();
                
        Set<FavoriteType> keys = favoritesMap.keySet();
        
        for(FavoriteType ft : keys){
            Component c = ft.getManagingPanel( favoritesMap.get(ft) );
            pane.addTab(ft.getTitle(), ft.getIcon(), c);
        }
        
        return pane;
    }
    
    private Object readObjet(File inf) throws IOException, ClassCastException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(inf);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object o = in.readObject();
        return o;
    }

    private void writeObject(File fout, Object O) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(fout);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(O);
        out.close();
        fileOut.close();
    }
    
    
    public static FavoritesManager getInstance(){
        if(instance==null){
            instance = new FavoritesManager();
        }
        
        return instance;
    }
    
}
