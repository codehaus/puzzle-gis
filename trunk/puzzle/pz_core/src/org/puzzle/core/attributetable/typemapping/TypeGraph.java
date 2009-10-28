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

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 *
 * @author Johann Sorel (Puzzle-GIS)
 */
public class TypeGraph extends VMDGraphScene{

    private static long edgeID = 0;

    private final SimpleFeatureType sourceType;
    private final SimpleFeatureType targetType;

    private LayerWidget interractionLayer = new LayerWidget (this);
    private WidgetAction connectAction = ActionFactory.createConnectAction(interractionLayer, new SceneConnectProvider ());
    private WidgetAction reconnectAction = ActionFactory.createReconnectAction(new SceneReconnectProvider());

    public TypeGraph(SimpleFeatureType sourceType, SimpleFeatureType targetType) {
        addChild (interractionLayer);

        this.sourceType = sourceType;
        this.targetType = targetType;

        createSFTWidget(targetType);
        createSFTWidget(sourceType);


        //create the default link for geometries
        createPPEdge(this,
                sourceType.getTypeName()+sourceType.getGeometryDescriptor().getLocalName(),
                targetType.getTypeName()+targetType.getGeometryDescriptor().getLocalName());

        GridGraphLayout<String, String> graphLayout = new GridGraphLayout<String, String> ();
        SceneLayout sceneGraphLayout = LayoutFactory.createSceneGraphLayout(this, graphLayout);
        sceneGraphLayout.invokeLayout ();
    }

    public SimpleFeatureType getSourceType() {
        return sourceType;
    }

    public SimpleFeatureType getTargetType() {
        return targetType;
    }

    public Map<AttributeDescriptor,List<AttributeDescriptor>> getMapping(){
        final Map<AttributeDescriptor,List<AttributeDescriptor>> mapping =
                new HashMap<AttributeDescriptor, List<AttributeDescriptor>>();

        final String typeName1 = sourceType.getTypeName();
        final String typeName2 = targetType.getTypeName();

        for(final String edge : getEdges()){
            final ConnectionWidget wid = (ConnectionWidget) findWidget(edge);
            final Widget w1 = wid.getSourceAnchor().getRelatedWidget();
            final Widget w2 = wid.getTargetAnchor().getRelatedWidget();
            String att1 = (String) findObject(w1);
            String att2 = (String) findObject(w2);

            final AttributeDescriptor desc1;
            final AttributeDescriptor desc2;
            if(att1.startsWith(typeName1)){
                att1 = att1.replaceAll(typeName1, "");
                att2 = att2.replaceAll(typeName2, "");
                desc1 = sourceType.getDescriptor(att1);
                desc2 = targetType.getDescriptor(att2);
            }else if(att2.startsWith(typeName1)){
                att2 = att2.replaceAll(typeName1, "");
                att1 = att1.replaceAll(typeName2, "");
                desc1 = sourceType.getDescriptor(att2);
                desc2 = targetType.getDescriptor(att1);
            }else{
                continue;
            }

            List<AttributeDescriptor> descs = mapping.get(desc1);
            if(descs == null){
                descs = new ArrayList<AttributeDescriptor>();
                mapping.put(desc1, descs);
            }
            descs.add(desc2);
        }
        
        return mapping;
    }

    private void createSFTWidget(SimpleFeatureType sft){
        final String typeName = sft.getTypeName();
        String mobile = createNode (this, 100, 100, null, typeName, "Type", null);

        for(final AttributeDescriptor desc : sft.getAttributeDescriptors()){
            final String attName = desc.getLocalName();
            createPin(this, mobile, typeName+attName, null, attName, "Element");
        }
    }

    @Override
    protected Widget attachPinWidget(String arg0, String arg1) {
        final Widget wid = super.attachPinWidget(arg0, arg1);
        if(wid != null){
            VMDPinWidget pin = (VMDPinWidget) wid;
            pin.getActions().addAction (connectAction);
            wid.getActions ().addAction (reconnectAction);
        }
        return wid;
    }

    @Override
    protected Widget attachEdgeWidget(String arg0) {
        final ConnectionWidget wid = (ConnectionWidget) super.attachEdgeWidget(arg0);

        wid.setTargetAnchorShape(AnchorShape.NONE);
        wid.setSourceAnchorShape(AnchorShape.NONE);

        for(WidgetAction act : new ArrayList<WidgetAction>(wid.getActions().getActions())){
            wid.getActions().removeAction(act);
        }

        if(wid != null){
            wid.getActions ().addAction (reconnectAction);
        }
        return wid;
    }



    private class SceneConnectProvider implements ConnectProvider {

        @Override
        public boolean isSourceWidget (Widget sourceWidget) {
            return sourceWidget instanceof VMDPinWidget;
        }

        @Override
        public ConnectorState isTargetWidget(Widget pin1, Widget pin2) {

            if(pin1 instanceof VMDPinWidget && pin2 instanceof VMDPinWidget){
                final VMDPinWidget vmd1 = (VMDPinWidget) pin1;
                final VMDPinWidget vmd2 = (VMDPinWidget) pin2;
                
                if(pin1.getParentWidget().equals(pin2.getParentWidget())){
                    return ConnectorState.REJECT;
                }

                String attSource = (String) findObject(pin1);
                String attTarget = (String) findObject(pin2);
                final String typeSource = (String) findObject(pin1.getParentWidget());
                final String typeTarget = (String) findObject(pin2.getParentWidget());
                attSource = attSource.substring(typeSource.length(), attSource.length());
                attTarget = attTarget.substring(typeTarget.length(), attTarget.length());

                final Map<AttributeDescriptor,List<AttributeDescriptor>> mapping = getMapping();
                if(typeSource.equals(sourceType.getName().getLocalPart())){
                    //pin1 is the source, can be match to several attributs
                    //check that the link doesnt exist already
                    for(final List<AttributeDescriptor> lst : mapping.values()){
                        for(final AttributeDescriptor desc : lst){
                            if(desc.getLocalName().equals(attTarget)){
                                return ConnectorState.REJECT;
                            }
                        }
                    }
                }else{
                    //pin1 is the target, can be match to only one attributs
                    //check that the link doesnt exist already
                    for(final List<AttributeDescriptor> lst : mapping.values()){
                        for(final AttributeDescriptor desc : lst){
                            if(desc.getLocalName().equals(attSource)){
                                return ConnectorState.REJECT;
                            }
                        }
                    }
                }

                System.out.println("att source : " + attSource);
                System.out.println("type source : " + typeSource);
                System.out.println("att target : " + attTarget);
                System.out.println("type target : " + typeTarget);


                return ConnectorState.ACCEPT;
            }

            return ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomTargetWidgetResolver (Scene scene) {
            return false;
        }

        @Override
        public Widget resolveTargetWidget (Scene scene, Point sceneLocation) {
            return null;
        }

        @Override
        public void createConnection (Widget sourceWidget, Widget targetWidget) {
            String source = (String) findObject(sourceWidget);
            String target = (String) findObject(targetWidget);
            createPPEdge(TypeGraph.this, source, target);
        }

    }

    private class SceneReconnectProvider implements ReconnectProvider {

        String edge;
        String originalNode;
        String replacementNode;

        @Override
        public void reconnectingStarted (ConnectionWidget connectionWidget, boolean reconnectingSource) {
        }

        @Override
        public void reconnectingFinished (ConnectionWidget connectionWidget, boolean reconnectingSource) {
        }

        @Override
        public boolean isSourceReconnectable (ConnectionWidget connectionWidget) {
            Object object = findObject (connectionWidget);
            edge = isEdge (object) ? (String) object : null;
            originalNode = edge != null ? getEdgeSource (edge) : null;
            return originalNode != null;
        }

        @Override
        public boolean isTargetReconnectable (ConnectionWidget connectionWidget) {
            Object object = findObject (connectionWidget);
            edge = isEdge (object) ? (String) object : null;
            originalNode = edge != null ? getEdgeTarget (edge) : null;
            return originalNode != null;
        }

        @Override
        public ConnectorState isReplacementWidget (ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
            Object object = findObject (replacementWidget);
            replacementNode = isNode (object) ? (String) object : null;
            if (replacementNode != null)
                return ConnectorState.ACCEPT;
            return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomReplacementWidgetResolver (Scene scene) {
            return false;
        }

        @Override
        public Widget resolveReplacementWidget (Scene scene, Point sceneLocation) {
            return null;
        }

        @Override
        public void reconnect (ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
            if (replacementWidget == null)
                removeEdge (edge);
            else if (reconnectingSource)
                setEdgeSource (edge, replacementNode);
            else
                setEdgeTarget (edge, replacementNode);
        }

    }

    private static String createNode (VMDGraphScene scene, int x, int y, Image image, String name, String type, List<Image> glyphs) {
        String nodeID = name;
        VMDNodeWidget widget = (VMDNodeWidget) scene.addNode (nodeID);
        widget.setPreferredLocation (new Point (x, y));
        widget.setNodeProperties (image, name, type, glyphs);
        scene.addPin (nodeID, nodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
        return nodeID;
    }

    private static void createPin (VMDGraphScene scene, String nodeID, String pinID, Image image, String name, String type) {
        VMDPinWidget pinWidget = ((VMDPinWidget) scene.addPin (nodeID, pinID));
        pinWidget.setProperties(name, null);

    }

    private static void createEdge (VMDGraphScene scene, String sourcePinID, String targetNodeID) {
        String edgeID = "edge" + TypeGraph.edgeID++;
        scene.addEdge (edgeID);
        scene.setEdgeSource(edgeID, sourcePinID);
        scene.setEdgeTarget (edgeID, targetNodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
    }

    private static void createPPEdge (VMDGraphScene scene, String sourcePinID, String targetNodeID) {
        String edgeID = "edge" + TypeGraph.edgeID++;
        scene.addEdge (edgeID);
        scene.setEdgeSource(edgeID, sourcePinID);
        scene.setEdgeTarget (edgeID, targetNodeID);
    }

}
