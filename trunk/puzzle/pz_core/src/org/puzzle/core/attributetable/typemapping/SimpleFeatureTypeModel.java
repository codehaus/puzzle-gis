/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2009, Johann Sorel
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

package org.puzzle.core.attributetable.typemapping;

import java.util.HashMap;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class SimpleFeatureTypeModel extends AbstractTableModel{

    private final SimpleFeatureType type;
    private final Map<String,Object> values = new HashMap<String, Object>();

    public SimpleFeatureTypeModel(SimpleFeatureType type){
        this.type = type;

        for(AttributeDescriptor desc : type.getAttributeDescriptors()){
            values.put(desc.getLocalName(),desc.getDefaultValue());
        }
    }

    @Override
    public int getRowCount() {
        return type.getAttributeCount();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0: return "Property";
            case 1: return "Type";
            default:return "Value";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0: return String.class;
            case 1: return String.class;
            default:return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0: return type.getDescriptor(rowIndex).getLocalName();
            case 1: return type.getDescriptor(rowIndex).getType().getBinding().getSimpleName();
            default:return values.get(type.getDescriptor(rowIndex).getLocalName());
        }
    }

    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        if(columnIndex <= 1)return;
    }

}
