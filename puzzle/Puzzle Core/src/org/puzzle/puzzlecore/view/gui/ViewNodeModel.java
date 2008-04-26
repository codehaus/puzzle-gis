package org.puzzle.puzzlecore.view.gui;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.Collection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.view.MapGroup;
import org.puzzle.puzzlecore.view.MapView;
import org.puzzle.puzzlecore.view.ViewService;

public class ViewNodeModel extends Children.Keys implements LookupListener {

    public static final Image ICON_VIEW = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/view.png");
    public static final Image ICON_GROUP = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/group.png");
    public static final Image ICON_SCALE = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/scale.png");
    public static final Image ICON_ROTATION = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/rotation.png");
    public static final Image ICON_TRANSLATION = Utilities.loadImage ("/org/puzzle/puzzlecore/gtextend/widget/iconset/translation.png");
    
    
    private Lookup.Result result = null;

    public ViewNodeModel() {

        //we listen the views changes
        ViewService v = Lookup.getDefault().lookup(ViewService.class);
        if (v != null) {
            result = v.getLookup().lookupResult(MapView.class);
            result.addLookupListener(this);
        }

        Collection c = result.allInstances();
        setKeys(c);
    }

    @Override
    protected Node[] createNodes(Object o) {
        Node[] nodes = {};

        if (o instanceof MapView) {
            MapView view = (MapView) o;
            AbstractNode result;
            try {
                result = new ViewNode(view);
                nodes = new Node[]{result};
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else if (o instanceof MapGroup) {
        } else {
        }

        return nodes;
    }

//    @Override
//    protected void addNotify() {
//                
//        Collection c = result.allInstances();
////        if (!c.isEmpty()) {
////            lbl_nom.setText(new Integer(c.size()).toString());
//////            MapView o = (MapView) c.iterator().next();
//////            lbl_nom.setText(o.getName() );
////        } else {
////            lbl_nom.setText("[no selection]");
////        }
////        
////        
////        MapView[] objs = new MapView[5];
////        for (int i = 0; i < objs.length; i++) {
////            objs[i] = new MapView(new JDefaultMap2D());
////        }
////        MapView[] objs = CORE.getViewManager().getViews();
//        setKeys(c);
//    }
    public void resultChanged(LookupEvent lookupEvent) {
        Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
        Collection c = r.allInstances();

        setKeys(c);
//        if (!c.isEmpty()) {
//            lbl_nom.setText(new Integer(c.size()).toString());
////            MapView o = (MapView) c.iterator().next();
////            lbl_nom.setText(o.getName() );
//        } else {
//            lbl_nom.setText("[no selection]");
//        }


    }
}
