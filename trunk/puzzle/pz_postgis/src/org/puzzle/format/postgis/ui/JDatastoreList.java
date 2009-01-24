
package org.puzzle.format.postgis.ui;

import java.io.IOException;
import javax.swing.JTable;
import org.geotools.data.DataStore;

/**
 * @author Johann Sorel (Puzzle-GIS)
 */
public class JDatastoreList extends JTable{

    public JDatastoreList(){
        setTableHeader(null);
        setModel(new DBModel(this));
    }

    public void setDataStore(DataStore store){
        ((DBModel)getModel()).clean();

        try {
            ((DBModel) getModel()).add(store.getTypeNames());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public String getSelected(){
        int row = getSelectedRow();

        if(row != -1){
            return getModel().getValueAt(row, 0).toString();
        }else{
            return null;
        }
    }

}
