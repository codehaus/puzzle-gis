package org.puzzle.puzzlecore.view.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JCheckBox;
import org.openide.util.Utilities;

public class LinkPropertyEditor extends PropertyEditorSupport{

    private static final Image ICON_LINK = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/link.png");
    private static final Image ICON_UNLINK = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/unlink.png");
    
    private boolean value = false;
    private VisbleCheck check = new VisbleCheck();
    
    public void setValue(Object value) {
        if(value instanceof Boolean){
            this.value = (Boolean)value;
        }
    }

    public Object getValue() {
        return value;
    }

    public boolean isPaintable() {
        return true;
    }

    public void paintValue(Graphics gfx, Rectangle box) {
        int x = (box.width-ICON_LINK.getWidth(null))/2;
        int y = (box.height-ICON_LINK.getHeight(null))/2;        
        gfx.drawImage((value)?ICON_LINK : ICON_UNLINK, x, y, null);
    }

    public String getJavaInitializationString() {
        return "";
    }

    public String getAsText() {
        return "";
    }

    public void setAsText(String text) throws IllegalArgumentException {
    }

    public String[] getTags() {
        return new String[]{};
    }

    public Component getCustomEditor() {
        return check;
    }
    
    public boolean supportsCustomEditor() {
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    
    class VisbleCheck extends JCheckBox{
    
     @Override
        public void paintComponent(Graphics g){
         
            int x = (getWidth()-16)/2;
            int y = (getHeight()-16)/2;
            g.drawImage( (isSelected())?ICON_LINK : ICON_UNLINK, x,y, this);
        }
}
    
}



