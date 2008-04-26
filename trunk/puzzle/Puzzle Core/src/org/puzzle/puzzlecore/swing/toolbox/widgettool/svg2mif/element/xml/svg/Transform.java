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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.element.xml.svg;
import java.awt.geom.AffineTransform;
import java.util.StringTokenizer;

import org.geotools.gui.swing.toolbox.widgettool.svg2mif.element.xml.Utility;
/**
 *
 * @author  Default
 */
public class Transform extends AffineTransform {
    

    
    /** Creates a new instance of Transform */
    public Transform() {
    }
    
    void setTransform(java.lang.String transformString) {
        int transformOffset = -1;
        int endParenOffset = -1;
        int numTransformTokens;
        StringTokenizer transformTokens = null;
        double transformValue[] = null;


        String transformData;
        transformString = transformString.replace('\n', ' ');  //suppress newline
        transformString = transformString.replace('\t', ' ');  // suppress tab
        transformOffset = transformString.indexOf("translate");  
        if ( transformOffset >= 0){
             endParenOffset = transformString.indexOf(")", transformOffset);
             if ( endParenOffset > 0){
                   transformData = transformString.substring(transformOffset + 9, endParenOffset+1);
                   transformValue   = new double[2];  //trusting that SVG will match required fields
                   transformValue = Utility.getDoubles(transformData);
                   this.translate(transformValue[0], transformValue[1]);
                }
        }
        transformOffset = transformString.indexOf("scale");  
        if ( transformOffset >= 0){
             endParenOffset = transformString.indexOf(")", transformOffset);
             if ( endParenOffset > 0){
                   transformData = transformString.substring(transformOffset+5, endParenOffset+1);
                   transformTokens = new StringTokenizer(transformData,",() \t\n");
                    numTransformTokens = transformTokens.countTokens();
                   transformValue = new double[numTransformTokens];
                   transformValue = Utility.getDoubles(transformData);
                   if (numTransformTokens == 2){
                       this.scale(transformValue[0], transformValue[1]);
                   }else{
                       this.scale(transformValue[0], transformValue[0]);
                   }
                }
        }

        transformOffset = transformString.indexOf("rotate");  
        if ( transformOffset >= 0){
             endParenOffset = transformString.indexOf(")", transformOffset);
             if ( endParenOffset > 0){
                   transformData = transformString.substring(transformOffset+6, endParenOffset+1);
                   transformTokens = new StringTokenizer(transformData,",() \t\n");
                    numTransformTokens = transformTokens.countTokens();
                   transformValue = new double[numTransformTokens];
                   transformValue = Utility.getDoubles(transformData);
                   if (numTransformTokens == 1){
                       this.rotate(transformValue[0]* (Math.PI/180));
                   }
                   if (numTransformTokens == 3){                   
                       this.rotate(transformValue[0]*(Math.PI/180), transformValue[1], transformValue[2]);
                   }
                }
        }
        
        transformOffset = transformString.indexOf("skewX");  
        if ( transformOffset >= 0){
             endParenOffset = transformString.indexOf(")", transformOffset);
             if ( endParenOffset > 0){
                   transformData = transformString.substring(transformOffset + 5, endParenOffset+1);
                   transformValue   = new double[1];  //trusting that SVG will match required fields
                   transformValue = Utility.getDoubles(transformData);
                   this.shear(Math.tan(Math.PI * transformValue[0]/180), 0.0);
                   // notes on w3c and clarifed by Batik code
                }
        }

        transformOffset = transformString.indexOf("skewY");  
        if ( transformOffset >= 0){
             endParenOffset = transformString.indexOf(")", transformOffset);
             if ( endParenOffset > 0){
                   transformData = transformString.substring(transformOffset + 5, endParenOffset+1);
                   transformValue   = new double[1];  //trusting that SVG will match required fields
                   transformValue = Utility.getDoubles(transformData);
                   this.shear(0.0, Math.tan( Math.PI * transformValue[0]/180) );
                   // notes on w3c and clarifed by Batik code
                }
        }        
       
    }
}
