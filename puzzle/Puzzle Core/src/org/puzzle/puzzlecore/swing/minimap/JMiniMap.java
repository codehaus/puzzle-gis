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
package org.puzzle.puzzlecore.swing.minimap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gui.swing.icon.IconBundle;
import org.geotools.gui.swing.map.map2d.Map2D;
import org.geotools.gui.swing.map.map2d.decoration.ColorDecoration;
import org.geotools.gui.swing.map.map2d.decoration.MapDecoration;
import org.geotools.gui.swing.map.map2d.stream.JStreamNavMap;
import org.geotools.gui.swing.map.map2d.stream.StreamingMap2D;
import org.geotools.gui.swing.map.map2d.stream.control.StreamRefresh;
import org.geotools.gui.swing.map.map2d.stream.control.StreamZoomAll;
import org.geotools.gui.swing.map.map2d.stream.event.MapEvent;
import org.geotools.gui.swing.map.map2d.stream.event.StrategyEvent;
import org.geotools.gui.swing.map.map2d.stream.listener.MapListener;
import org.geotools.gui.swing.map.map2d.stream.listener.StrategyListener;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author johann sorel
 */
public class JMiniMap extends JComponent {

    private static final ImageIcon ICON_LAYER_VISIBLE = IconBundle.getResource().getIcon("16_maplayer_visible");
    private static final ImageIcon ICON_ZOOM_ALL = IconBundle.getResource().getIcon("16_zoom_all");
    private static final ImageIcon ICON_REFRESH = IconBundle.getResource().getIcon("16_data_reload");
    private static final MapLayer[] EMPTY_LAYER_ARRAY = {};
    private StreamingMap2D map = new JStreamNavMap();
    private StreamingMap2D relatedMap = null;
    private MiniMapDecoration deco = new MiniMapDecoration();
    private MapContext activeContext = null;
    private WeakHashMap<MapContext, WeakHashMap<MapLayer,Boolean>> contexts = new WeakHashMap<MapContext, WeakHashMap<MapLayer,Boolean>>();
    private final StrategyListener strategyListen = new StrategyListener() {

        public void setRendering(boolean rendering) {
        }

        public void mapContextChanged(StrategyEvent event) {
            setContext(event.getContext());
        }

        public void mapAreaChanged(StrategyEvent event) {
        }

    };
    private final MapListener mapListen = new MapListener() {
        
        public void mapStrategyChanged(MapEvent mapEvent) {
            mapEvent.getPreviousStrategy().removeStrategyListener(strategyListen);
            mapEvent.getStrategy().addStrategyListener(strategyListen);
        }

        public void mapActionStateChanged(MapEvent mapEvent) {
        }
    };

    public JMiniMap() {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, map.getComponent());

        
        map.setBackgroundDecoration(new ColorDecoration());
        map.addDecoration(deco);
    }

    private void setContext(MapContext context) {
        activeContext = context;

        MapContext ctx = new DefaultMapContext(DefaultGeographicCRS.WGS84);

        try {
            ctx.setCoordinateReferenceSystem(context.getCoordinateReferenceSystem());
        } catch (TransformException ex) {
            ex.printStackTrace();
        } catch (FactoryException ex) {
            ex.printStackTrace();
        }


        if (contexts.containsKey(context)) {
            WeakHashMap<MapLayer,Boolean> newmap = new WeakHashMap<MapLayer,Boolean>();
            WeakHashMap<MapLayer,Boolean> map = contexts.get(context);            
            Set<MapLayer> set = map.keySet();    
            
            MapLayer[] layers = context.getLayers();
            for(MapLayer layer : set){
                boolean exists = false;
                for(MapLayer l : layers){
                    if(layer == l){
                        exists = true;
                    }
                }
                
                if(exists){
                    newmap.put(layer,true);
                }
            }
            
            contexts.put(context, newmap);
            
            ctx.addLayers(newmap.keySet().toArray(EMPTY_LAYER_ARRAY));
        } else {
            WeakHashMap<MapLayer,Boolean> map = new WeakHashMap<MapLayer,Boolean>();
            Set<MapLayer> set = map.keySet();            
            set.toArray(EMPTY_LAYER_ARRAY);
            
            
            MapLayer[] layers = context.getLayers();

            for (MapLayer layer : layers) {
                map.put(layer,true);
            }

            contexts.put(context, map);

            ctx.addLayers(set.toArray(EMPTY_LAYER_ARRAY));
        }


        map.getRenderingStrategy().setContext(ctx);

        try {
            ReferencedEnvelope env = context.getLayerBounds();
            if (env != null) {
                map.getRenderingStrategy().setMapArea(env);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }




    }

    public void setRelatedMap2D(StreamingMap2D map) {

        if (relatedMap != null) {
            relatedMap.removeMap2DListener(mapListen);
            relatedMap.getRenderingStrategy().removeStrategyListener(strategyListen);
        }

        relatedMap = map;

        if (relatedMap != null) {
            relatedMap.addMap2DListener(mapListen);
            relatedMap.getRenderingStrategy().addStrategyListener(strategyListen);

            setContext(relatedMap.getRenderingStrategy().getContext());
        }

        deco.setRelatedMap2D(map);
    }

    //--------private classes---------------------------------------------------
    private class MiniMapMenu extends JPopupMenu {

        @Override
        public void setVisible(boolean b) {

            removeAll();

            if (b) {
                if (activeContext != null) {

                    MapLayer[] layers = activeContext.getLayers();
                    WeakHashMap<MapLayer,Boolean> checkedLayers = contexts.get(activeContext);

                    for (final MapLayer layer : layers) {

                        JCheckBoxMenuItem m = new JCheckBoxMenuItem(layer.getTitle());
                        m.setSelected(checkedLayers.containsKey(layer));

                        m.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                if (((JCheckBoxMenuItem) e.getSource()).isSelected()) {
                                    contexts.get(activeContext).put(layer,true);
                                } else {
                                    contexts.get(activeContext).remove(layer);
                                }
                                setContext(activeContext);
                                map.getRenderingStrategy().refresh();
                            }
                        });

                        add(m);
                    }

                }
            }

            super.setVisible(b);
        }
    }

    

    class MiniMapDecoration extends JPanel implements MapDecoration,MouseListener {

        private StreamingMap2D map = null;
        private StreamingMap2D relatedMap = null;
        private StreamZoomAll zoomAllAction = new StreamZoomAll();
        private final StreamRefresh refreshAction = new StreamRefresh();
        private final StrategyListener strategyListen = new StrategyListener() {

            public void setRendering(boolean rendering) {
            }

            public void mapContextChanged(StrategyEvent event) {
            }

            public void mapAreaChanged(StrategyEvent event) {
                revalidate();
                repaint();
            }

        };
        private final MapListener mapListen = new MapListener() {

            public void mapStrategyChanged(MapEvent mapEvent) {
                mapEvent.getPreviousStrategy().removeStrategyListener(strategyListen);
                mapEvent.getStrategy().addStrategyListener(strategyListen);
            }

            public void mapActionStateChanged(MapEvent mapEvent) {
            }
        };

        public MiniMapDecoration() {
            super(new BorderLayout(0, 0));

            addMouseListener(this);
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2,2));
            panel.setOpaque(false);

            JButton but = new JButton(zoomAllAction);
            but.setContentAreaFilled(false);
            but.setOpaque(false);
            but.setBorder(null);
            but.setMargin(new Insets(2,2,2,2));
            but.setBorderPainted(false);
            but.setIcon(ICON_ZOOM_ALL);

            panel.add(but);

            but = new JButton(refreshAction);
            but.setContentAreaFilled(false);
            but.setOpaque(false);
            but.setBorder(null);
            but.setMargin(new Insets(2,2,2,2));
            but.setBorderPainted(false);
            but.setIcon(ICON_REFRESH);

            panel.add(but);

            but = new JButton();
            but.setContentAreaFilled(false);
            but.setOpaque(false);
            but.setBorder(null);
            but.setMargin(new Insets(2,2,2,2));
            but.setBorderPainted(false);
            but.setIcon(ICON_LAYER_VISIBLE);

//            but.addActionListener(new ActionListener() {
//
//                public void actionPerformed(ActionEvent e) {
//                    ((JButton) e.getSource()).getComponentPopupMenu().setLocation(((JButton) e.getSource()).getLocationOnScreen());
//                    ((JButton) e.getSource()).getComponentPopupMenu().setVisible(true);
//                }
//            });

            but.setComponentPopupMenu(new MiniMapMenu());

            panel.add(but);

            add(BorderLayout.NORTH, panel);

            setOpaque(false);

        }

        public void refresh() {

        }

        public void setMap2D(Map2D map) {
            if(map instanceof StreamingMap2D){
                this.map = (StreamingMap2D)map;
                zoomAllAction.setMap(this.map);
                refreshAction.setMap(this.map);
            }
        }

        public Map2D getMap2D() {
            return map;
        }

        public void setRelatedMap2D(StreamingMap2D map) {

            if (relatedMap != null) {
                relatedMap.removeMap2DListener(mapListen);
                relatedMap.getRenderingStrategy().removeStrategyListener(strategyListen);
            }

            relatedMap = map;

            if (relatedMap != null) {
                relatedMap.addMap2DListener(mapListen);
                relatedMap.getRenderingStrategy().addStrategyListener(strategyListen);
            }

        }

        public Map2D getRelatedMap2D() {
            return relatedMap;
        }

        public JComponent geComponent() {
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {

            if (map != null && relatedMap != null) {
                Envelope env = relatedMap.getRenderingStrategy().getMapArea();
                Coordinate minCoord = new Coordinate(env.getMinX(), env.getMinY());
                Coordinate maxCoord = new Coordinate(env.getMaxX(), env.getMaxY());

                Point minPoint = map.getRenderingStrategy().toComponentCoord(minCoord);
                Point maxPoint = map.getRenderingStrategy().toComponentCoord(maxCoord);

                g.setColor(Color.RED);
                g.drawRect(minPoint.x, getHeight() - (maxPoint.y - minPoint.y) - minPoint.y, maxPoint.x - minPoint.x, maxPoint.y - minPoint.y);

                int x = (maxPoint.x + minPoint.x) / 2;
                int y = (maxPoint.y + minPoint.y) / 2;

                g.drawLine(x, 0, x, getHeight() - (maxPoint.y - minPoint.y) - minPoint.y);
                g.drawLine(x, getHeight() - minPoint.y, x, getHeight());

//                y = getHeight() + (-maxPoint.y - minPoint.y) / 2;

                g.drawLine(0, y, minPoint.x, y);
                g.drawLine(maxPoint.x, y, getWidth(), y);
            }

            super.paintComponent(g);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if(relatedMap != null){
                Coordinate coord = map.getRenderingStrategy().toMapCoord(e.getX(), e.getY());
                
                Envelope env = relatedMap.getRenderingStrategy().getMapArea();
                
                Coordinate c1 = new Coordinate(coord.x-env.getWidth()/2,coord.y-env.getHeight()/2);
                Coordinate c2 = new Coordinate(coord.x+env.getWidth()/2,coord.y+env.getHeight()/2);
                
                Envelope newenv = new Envelope(c1,c2);
                
                relatedMap.getRenderingStrategy().setMapArea(newenv);
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void dispose() {
        }

    }
}
