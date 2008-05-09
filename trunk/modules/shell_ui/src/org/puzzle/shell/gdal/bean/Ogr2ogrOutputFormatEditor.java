/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.shell.gdal.bean;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

/**
 *
 * @author johann sorel
 */
public class Ogr2ogrOutputFormatEditor extends PropertyEditorSupport{

    
    private String[] tags ={
        "DGN",
        "GML",
        "Google KML",
        "MapInfo TAB",
        "Esri SHAPE"};

    @Override
    public String getAsText() {
        int index = ((Ogr2ogr.FORMAT)getValue()).ordinal();
        return tags[index];
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int index = Arrays.asList(tags).indexOf(text);
        if(index >=0 )setValue(Ogr2ogr.FORMAT.values()[index]);
    }
    
    @Override
    public String[] getTags(){
        return tags;
    }
    
    @Override
    public String getJavaInitializationString(){
        return Ogr2ogr.FORMAT.class.getName().replace('$', '.')+"."+getValue();
    }
    
}
