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
package org.puzzle.puzzlecore.gtextend.widget.style;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.geotools.gui.swing.misc.Render.RandomStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;

/**
 *
 * @author johann sorel
 */
public class StyleTreeModel extends DefaultTreeModel {

    private static final StyleBuilder STYLE_BUILDER = new StyleBuilder();
    private static final RandomStyleFactory RANDOM_FACTORY = new RandomStyleFactory();
    public static final Style DEFAULT_STYLE = new StyleBuilder().createStyle();
    private Style style = null;

    /**
     * create a StyleTreeModel
     * @param style , can't be null
     */
    public StyleTreeModel(Style style) {
        super(new DefaultMutableTreeNode());

        if (style == null) {
            throw new NullPointerException("Style can't be null");
        }
        this.style = style;
        setRoot(parse(style));
    }

    /**
     * Set the model Style
     * @param style , can't be null
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException("Style can't be null");
        }
        this.style = style;

        setRoot(parse(style));
    }
    /**
     * 
     * @return Style
     */
    public Style getStyle() {
        return style;
    }

    //---------------------using nodes------------------------------------------
    
    
    public boolean isDeletable(DefaultMutableTreeNode node){
        
        boolean deletable = false;        
        Object removeObject = node.getUserObject();
        
        if(removeObject instanceof Style){
            
        }else if(removeObject instanceof FeatureTypeStyle){
            if(style.getFeatureTypeStyles().length > 1){
                deletable = true;
            }
        }else if (removeObject instanceof Rule){
            DefaultMutableTreeNode ftsnode = (DefaultMutableTreeNode)node.getParent();
            FeatureTypeStyle fts = (FeatureTypeStyle) ftsnode.getUserObject();
            if(fts.getRules().length > 1){
                deletable = true;
            }
            
        }else if (removeObject instanceof Symbolizer){
            DefaultMutableTreeNode rulenode = (DefaultMutableTreeNode)node.getParent();
            Rule rule = (Rule) rulenode.getUserObject();
            if(rule.getSymbolizers().length > 1){
                deletable = true;
            }
        }
        
        return deletable;        
    }
    
    /**
     * delete a node and his related style object
     * @param node
     * @return false if not removed
     */
    public boolean deleteNode(DefaultMutableTreeNode node) {
        
        boolean removed = false;        
        Object removeObject = node.getUserObject();
        
        if(removeObject instanceof Style){
            
        }else if(removeObject instanceof FeatureTypeStyle){
            if(style.getFeatureTypeStyles().length > 1){
                remove((FeatureTypeStyle)removeObject);
                removed = true;
            }
        }else if (removeObject instanceof Rule){
            DefaultMutableTreeNode ftsnode = (DefaultMutableTreeNode)node.getParent();
            FeatureTypeStyle fts = (FeatureTypeStyle) ftsnode.getUserObject();
            if(fts.getRules().length > 1){
                remove(ftsnode,(Rule)removeObject);
                removed = true;
            }
            
        }else if (removeObject instanceof Symbolizer){
            DefaultMutableTreeNode rulenode = (DefaultMutableTreeNode)node.getParent();
            Rule rule = (Rule) rulenode.getUserObject();
            if(rule.getSymbolizers().length > 1){
                remove(rulenode,(Symbolizer)removeObject);
                removed = true;
            }
        }
        
        return removed;
    }
    /**
     * move an existing node
     * @param movedNode :node to move
     * @param targetNode 
     * @return DefaultMutableTreeNode or null if node could not be moved
     */
    public DefaultMutableTreeNode moveNode(DefaultMutableTreeNode movedNode, DefaultMutableTreeNode targetNode) {
        Object movedObj = movedNode.getUserObject();
        DefaultMutableTreeNode parentMovedNode = (DefaultMutableTreeNode) movedNode.getParent();
        Object parentMovedObj = parentMovedNode.getUserObject();
        
        Object targetObj = targetNode.getUserObject();
        

        DefaultMutableTreeNode copy = null;

        

        if (targetObj instanceof FeatureTypeStyle && movedObj instanceof FeatureTypeStyle) {
            copy = moveAt(movedNode,(FeatureTypeStyle) movedObj, indexof(style, (FeatureTypeStyle) targetObj));
            
        } else if (targetObj instanceof FeatureTypeStyle && movedObj instanceof Rule) {
                        
            if (parentMovedNode == targetNode) {                
            } else if(parentMovedNode.getChildCount() == 1){
                Rule rule = RANDOM_FACTORY.duplicate((Rule) movedObj);
                copy = insert(targetNode, rule);
            } else{
                remove(parentMovedNode, (Rule) movedObj);
                copy = insert(targetNode, (Rule) movedObj);
            }


        } else if (targetObj instanceof Rule && movedObj instanceof Rule) {
            
            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode)targetNode.getParent();
            Rule targetRule = (Rule) targetObj;
            int targetIndex = indexof((FeatureTypeStyle)targetParentNode.getUserObject(),targetRule);
            
            if (parentMovedNode == targetParentNode) {          
                copy = moveAt(movedNode, (Rule)movedObj, targetIndex);
            } else if(parentMovedNode.getChildCount() == 1){
                Rule rule = RANDOM_FACTORY.duplicate((Rule) movedObj);                                
                FeatureTypeStyle parentfts = (FeatureTypeStyle) targetParentNode.getUserObject();                
                copy = insertAt(targetParentNode, rule, targetIndex );
            } else{
                remove(parentMovedNode, (Rule) movedObj);
                copy = insertAt(targetParentNode, (Rule) movedObj,targetIndex);
            }
                        
        } else if (targetObj instanceof Rule && movedObj instanceof Symbolizer) {
            
            if (parentMovedNode == targetNode) {                
            } else if(parentMovedNode.getChildCount() == 1){
                Symbolizer symbol = RANDOM_FACTORY.duplicate((Symbolizer) movedObj);
                copy = insert(targetNode, symbol);
            } else{
                remove(parentMovedNode, (Symbolizer) movedObj);
                copy = insert(targetNode, (Symbolizer) movedObj);
            }
            
        } else if (targetObj instanceof Symbolizer && movedObj instanceof Symbolizer) {
            
            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode)targetNode.getParent();
            Symbolizer targetSymbol = (Symbolizer) targetObj;
            int targetIndex = indexof((Rule)targetParentNode.getUserObject(),targetSymbol);
            
            if (parentMovedNode == targetParentNode) {          
                copy = moveAt(movedNode, (Symbolizer)movedObj, targetIndex);
            } else if(parentMovedNode.getChildCount() == 1){
                Symbolizer symbol = RANDOM_FACTORY.duplicate((Symbolizer) movedObj);                                
                Rule parentRule = (Rule) targetParentNode.getUserObject();                
                copy = insertAt(targetParentNode, symbol, targetIndex );
            } else{
                remove(parentMovedNode, (Symbolizer) movedObj);
                copy = insertAt(targetParentNode, (Symbolizer) movedObj,targetIndex);
            }
            
        }

        return copy;
    }
    /**
     * duplicate a node
     * @param node
     * @return DefaultMutableTreeNode or null if node could not be duplicate
     */
    public DefaultMutableTreeNode duplicateNode(DefaultMutableTreeNode node) {
        Object obj = node.getUserObject();
        DefaultMutableTreeNode parentnode = (DefaultMutableTreeNode) node.getParent();
        Object parentobj = parentnode.getUserObject();

        DefaultMutableTreeNode copy = null;

        if (obj instanceof FeatureTypeStyle) {
            FeatureTypeStyle fts = RANDOM_FACTORY.duplicate((FeatureTypeStyle) obj);
            int index = indexof(style, (FeatureTypeStyle) obj) + 1;
            copy = insertAt(fts, index);
        } else if (obj instanceof Rule) {
            Rule rule = RANDOM_FACTORY.duplicate((Rule) obj);
            int index = indexof((FeatureTypeStyle) parentobj, (Rule) obj) + 1;
            copy = insertAt(parentnode, rule, index);
        } else if (obj instanceof Symbolizer) {
            Symbolizer symbol = RANDOM_FACTORY.duplicate((Symbolizer) obj);
            int index = indexof((Rule) parentobj, (Symbolizer) obj) + 1;
            copy = insertAt(parentnode, symbol, index);
        }

        return copy;
    }
    
    /**
     * add a new FeatureTypeStyle
     * @return created node
     */
    public DefaultMutableTreeNode newFeatureTypeStyle() {
        FeatureTypeStyle fts = STYLE_BUILDER.createFeatureTypeStyle(RANDOM_FACTORY.createPointSymbolizer());
        return insert(fts);
    }
    /**
     * add a new rule
     * @param ftsnode 
     * @return created node
     */
    public DefaultMutableTreeNode newRule(DefaultMutableTreeNode ftsnode) {
        Rule rule = STYLE_BUILDER.createRule(RANDOM_FACTORY.createPointSymbolizer());
        return insert(ftsnode, rule);
    }
    /**
     * add a new symbolizer
     * @param rulenode 
     * @return created node
     */
    public DefaultMutableTreeNode newPointSymbolizer(DefaultMutableTreeNode rulenode) {
        Symbolizer symbol = RANDOM_FACTORY.createPointSymbolizer();
        return insert(rulenode, symbol);
    }
    /**
     * add a new symbolizer
     * @param rulenode 
     * @return created node
     */
    public DefaultMutableTreeNode newLineSymbolizer(DefaultMutableTreeNode rulenode) {
        Symbolizer symbol = RANDOM_FACTORY.createLineSymbolizer();
        return insert(rulenode, symbol);
    }
    /**
     * add a new symbolizer
     * @param rulenode 
     * @return created node
     */
    public DefaultMutableTreeNode newPolygonSymbolizer(DefaultMutableTreeNode rulenode) {
        Symbolizer symbol = RANDOM_FACTORY.createPolygonSymbolizer();
        return insert(rulenode, symbol);
    }
    /**
     * add a new symbolizer
     * @param rulenode 
     * @return created node
     */
    public DefaultMutableTreeNode newRasterSymbolizer(DefaultMutableTreeNode rulenode) {
        Symbolizer symbol = RANDOM_FACTORY.createRasterSymbolizer();
        return insert(rulenode, symbol);
    }
    
    /**
     * add a new symbolizer
     * @param rulenode 
     * @return created node
     */
    public DefaultMutableTreeNode newTextSymbolizer(DefaultMutableTreeNode rulenode) {
        Symbolizer symbol = STYLE_BUILDER.createTextSymbolizer();
        return insert(rulenode, symbol);
    }

    
    //-------------------utilities----------------------------------------------
    private int indexof(Style style, FeatureTypeStyle fts) {
        FeatureTypeStyle[] types = style.getFeatureTypeStyles();
        int index = -1;

        for (int i = 0,  n = types.length; i < n; i++) {
            if (types[i] == fts) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int indexof(FeatureTypeStyle fts, Rule rule) {
        Rule[] rules = fts.getRules();
        int index = -1;

        for (int i = 0,  n = rules.length; i < n; i++) {
            if (rules[i] == rule) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int indexof(Rule rule, Symbolizer symbol) {
        Symbolizer[] symbols = rule.getSymbolizers();
        int index = -1;

        for (int i = 0,  n = symbols.length; i < n; i++) {
            if (symbols[i] == symbol) {
                index = i;
                break;
            }
        }

        return index;
    }

    private DefaultMutableTreeNode insert(FeatureTypeStyle fts) {
        style.addFeatureTypeStyle(fts);

        DefaultMutableTreeNode node = parse(fts);
        insertNodeInto(node, getRoot(), indexof(style, fts));

        return node;
    }

    private DefaultMutableTreeNode insert(DefaultMutableTreeNode parentNode, Rule rule) {
        DefaultMutableTreeNode rulenode = parse(rule);

        FeatureTypeStyle fts = (FeatureTypeStyle) parentNode.getUserObject();
        fts.addRule(rule);

        insertNodeInto(rulenode, parentNode, indexof(fts, rule));

        return rulenode;
    }

    private DefaultMutableTreeNode insert(DefaultMutableTreeNode parentNode, Symbolizer symbol) {
        DefaultMutableTreeNode symbolNode = new DefaultMutableTreeNode(symbol);

        Rule rule = (Rule) parentNode.getUserObject();

        Symbolizer[] symbols = rule.getSymbolizers();
        Symbolizer[] nsymbols = new Symbolizer[symbols.length + 1];

        int i = 0;
        for (int n = symbols.length; i < n; i++) {
            nsymbols[i] = symbols[i];
        }
        nsymbols[i] = symbol;

        rule.setSymbolizers(nsymbols);


        insertNodeInto(symbolNode, parentNode, indexof(rule, symbol));

        return symbolNode;
    }

    private DefaultMutableTreeNode insertAt(FeatureTypeStyle fts, int index) {
        FeatureTypeStyle[] types = style.getFeatureTypeStyles();
        FeatureTypeStyle[] ntypes = new FeatureTypeStyle[types.length + 1];

        for (int i = 0,  j = 0,  n = ntypes.length; j < n; i++, j++) {
            if (j == index) {
                ntypes[j] = fts;
                i--;
            } else {
                ntypes[j] = types[i];
            }
        }

        style.setFeatureTypeStyles(ntypes);

        DefaultMutableTreeNode node = parse(fts);
        insertNodeInto(node, getRoot(), index);

        return node;
    }

    private DefaultMutableTreeNode insertAt(DefaultMutableTreeNode parentNode, Rule rule, int index) {
        FeatureTypeStyle fts = (FeatureTypeStyle) parentNode.getUserObject();
        Rule[] rules = fts.getRules();
        Rule[] nrules = new Rule[rules.length + 1];

        for (int i = 0,  j = 0,  n = nrules.length; j < n; i++, j++) {
            if (j == index) {
                nrules[j] = rule;
                i--;
            } else {
                nrules[j] = rules[i];
            }
        }

        fts.setRules(nrules);

        DefaultMutableTreeNode node = parse(rule);
        insertNodeInto(node, parentNode, index);

        return node;
    }

    private DefaultMutableTreeNode insertAt(DefaultMutableTreeNode parentNode, Symbolizer symbol, int index) {
        Rule rule = (Rule) parentNode.getUserObject();
        Symbolizer[] symbols = rule.getSymbolizers();
        Symbolizer[] nsymbols = new Symbolizer[symbols.length + 1];

        for (int i = 0,  j = 0,  n = nsymbols.length; j < n; i++, j++) {
            if (j == index) {
                nsymbols[j] = symbol;
                i--;
            } else {
                nsymbols[j] = symbols[i];
            }
        }

        rule.setSymbolizers(nsymbols);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(symbol);
        insertNodeInto(node, parentNode, index);

        return node;
    }

    private DefaultMutableTreeNode moveAt(DefaultMutableTreeNode ftsnode, FeatureTypeStyle fts, int target) {

        int origine = indexof(style, fts);

        if (origine != target) {
            FeatureTypeStyle[] types = style.getFeatureTypeStyles();
            List<FeatureTypeStyle> ntypes = new ArrayList<FeatureTypeStyle>();

            for (FeatureTypeStyle type : types) {
                ntypes.add(type);
            }

            ntypes.remove(fts);
            removeNodeFromParent(ftsnode);
                       
            ntypes.add(target, fts);
            insertNodeInto(ftsnode, getRoot(), target);
           
            
            style.setFeatureTypeStyles(ntypes.toArray(new FeatureTypeStyle[ntypes.size()]));

        }

        return ftsnode;
        
    }
    
    private DefaultMutableTreeNode moveAt(DefaultMutableTreeNode rulenode, Rule rule, int target) {

        DefaultMutableTreeNode parentnode = (DefaultMutableTreeNode)rulenode.getParent();
        FeatureTypeStyle fts = (FeatureTypeStyle) parentnode.getUserObject();
        
        int origine = indexof(fts,rule);

        if (origine != target) {
            Rule[] rules = fts.getRules();
            List<Rule> nrules = new ArrayList<Rule>();

            for (Rule r : rules) {
                nrules.add(r);
            }

            nrules.remove(rule);
            removeNodeFromParent(rulenode);
            
            nrules.add(target, rule);
            insertNodeInto(rulenode, parentnode, target);
                        
            fts.setRules(nrules.toArray(new Rule[nrules.size()]));

        }

        return rulenode;
        
    }
    
    private DefaultMutableTreeNode moveAt(DefaultMutableTreeNode symbolnode, Symbolizer symbol, int target) {

        DefaultMutableTreeNode parentnode = (DefaultMutableTreeNode)symbolnode.getParent();
        Rule rule = (Rule) ((DefaultMutableTreeNode)symbolnode.getParent()).getUserObject();
        
        int origine = indexof(rule,symbol);

        if (origine != target) {
            Symbolizer[] symbols = rule.getSymbolizers();
            List<Symbolizer> nsymbols = new ArrayList<Symbolizer>();

            for (Symbolizer s : symbols) {
                nsymbols.add(s);
            }

            nsymbols.remove(symbol);
            removeNodeFromParent(symbolnode);
                
            nsymbols.add(target, symbol);
            insertNodeInto(symbolnode, parentnode, target);
                        
            rule.setSymbolizers(nsymbols.toArray(new Symbolizer[nsymbols.size()]));
        }

        return symbolnode;        
    }
    
    private void remove(FeatureTypeStyle fts){
        DefaultMutableTreeNode ftsNode = (DefaultMutableTreeNode) getRoot().getChildAt(indexof(style, fts));
        
        FeatureTypeStyle[] types = style.getFeatureTypeStyles();
        FeatureTypeStyle[] ntypes = new FeatureTypeStyle[types.length-1];
        
        for(int i=0,j=0,n=types.length;i<n;i++,j++){
            if(types[i] == fts){
                j--;
            }else{
                ntypes[j] = types[i];
            }
        }
                
        style.setFeatureTypeStyles(ntypes);
        removeNodeFromParent(ftsNode);        
    }
    
    private void remove(DefaultMutableTreeNode parentNode, Rule rule){
        FeatureTypeStyle fts = (FeatureTypeStyle) parentNode.getUserObject();
        
        DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) parentNode.getChildAt(indexof(fts, rule));
        
        fts.rules().remove(rule);
        removeNodeFromParent(ruleNode);        
    }
    
    private void remove(DefaultMutableTreeNode parentNode, Symbolizer symbol){
        Rule rule = (Rule) parentNode.getUserObject();
        
        DefaultMutableTreeNode symbolNode = (DefaultMutableTreeNode) parentNode.getChildAt(indexof(rule, symbol));
        
        Symbolizer[] symbols = rule.getSymbolizers();
        Symbolizer[] nsymbols = new Symbolizer[symbols.length-1];
        
        for(int i=0,j=0,n=symbols.length;i<n;i++,j++){
            if(symbols[i] == symbol){
                j--;
            }else{
                nsymbols[j] = symbols[i];
            }
        }
                
        rule.setSymbolizers(nsymbols);
        removeNodeFromParent(symbolNode);        
    }
    
    private DefaultMutableTreeNode parse(Style style) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(style);
        FeatureTypeStyle[] ftss = style.getFeatureTypeStyles();

        for (FeatureTypeStyle fts : ftss) {
            node.add(parse(fts));
        }
        return node;
    }

    private DefaultMutableTreeNode parse(FeatureTypeStyle fts) {
        DefaultMutableTreeNode ftsnode = new DefaultMutableTreeNode(fts);

        Rule[] rules = fts.getRules();
        for (Rule rule : rules) {
            ftsnode.add(parse(rule));
        }
        return ftsnode;
    }

    private DefaultMutableTreeNode parse(Rule rule) {
        DefaultMutableTreeNode rulenode = new DefaultMutableTreeNode(rule);
        Symbolizer[] symbs = rule.getSymbolizers();

        for (Symbolizer symb : symbs) {
            DefaultMutableTreeNode symbnode = new DefaultMutableTreeNode(symb);
            rulenode.add(symbnode);
        }
        return rulenode;
    }

    //--------------------override----------------------------------------------
    @Override
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) super.getRoot();
    }
    
}
