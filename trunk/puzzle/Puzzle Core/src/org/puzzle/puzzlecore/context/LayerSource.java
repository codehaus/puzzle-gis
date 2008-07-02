/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.puzzle.puzzlecore.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johann Sorel
 */
public final class LayerSource {

    private final int sourceId;
    private final Map<String,String> params = new HashMap<String, String>();
    
    public LayerSource(int sourceId, Map<String,String> parameters){
        this.sourceId = sourceId;
        if(parameters != null) this.params.putAll(parameters);
    }
    
    public final int getSourceId(){
        return sourceId;
    }
    
    public final Map<String,String> getParameters(){
        return Collections.unmodifiableMap(params);
    }
    
}
