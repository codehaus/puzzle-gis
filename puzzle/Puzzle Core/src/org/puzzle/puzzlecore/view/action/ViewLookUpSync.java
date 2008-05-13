/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.puzzle.puzzlecore.view.action;

import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.puzzle.puzzlecore.view.MapView;

/**
 *
 * @author johann sorel
 */
public class ViewLookUpSync {

    private Lookup.Result<MapView> resultViews = null;
    private final MapAction action;

    ViewLookUpSync(MapAction act) {
        action = act;

        resultViews = Utilities.actionsGlobalContext().lookupResult(MapView.class);
        resultViews.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent lookupEvent) {
                Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
                Collection<MapView> c = r.allInstances();

                if (c.isEmpty()) {
                    action.setMap(null);
                } else {
                    action.setMap(c.iterator().next().getMap());
                }
            }
        });


    }
}
