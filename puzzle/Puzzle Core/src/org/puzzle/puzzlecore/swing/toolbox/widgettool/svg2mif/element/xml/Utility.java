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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.svg2mif.element.xml;
import java.util.StringTokenizer;

/**
 *
 * @author  Default
 */
public final class Utility {
    
    private Utility() {
    }
    
     
   public static String trim (String trimString){
       int lastSpace =trimString.length();
       int spaceIndex = 0;
       while((trimString.charAt(0) == ' ') ){   // remove leading space
         trimString = trimString.substring(1);
         if (trimString.equals(" ")){                      
            break;  // out of while
         }
      }
      int len = trimString.length();   // remove trailing spaces
      while(trimString.charAt(len-1) == ' '){
        trimString= trimString.substring(0,len-1);
        len--;
      }
      return trimString;
    }
   
    public static    double[] getDoubles(String doubleString) {
         double[] doubleData;
         int i = 0;
         StringTokenizer t = new StringTokenizer(doubleString,",() \t\n");
         
         doubleData = new double[t.countTokens()];
         while (t.hasMoreTokens()){
             doubleData[i] = (double)new Double(t.nextToken()).doubleValue();
             i++;
         }
         return doubleData;
   
    }
    
    public static    float[] getFloats(String floatString) {
         float[] floatData;
         int i = 0;
         StringTokenizer t = new StringTokenizer(floatString,",() \t\n");
         floatData = new float[t.countTokens()];
         while (t.hasMoreTokens()){
             floatData[i] = (float)new Float(t.nextToken()).floatValue();
             i++;
         }
         return floatData;
    }

}
