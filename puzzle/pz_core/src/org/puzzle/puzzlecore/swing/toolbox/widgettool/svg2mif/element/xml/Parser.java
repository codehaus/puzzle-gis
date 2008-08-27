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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;


public class Parser {
    private Vector<Element> elements = new Vector<Element>(); 
    private int nestLevel=-1;   // incremented to Zero for root
    
    
    private int elementCount=0;
    private String tagName = null;
    private Stack<String> elementStack = new Stack<String>();
    private Element e = null;
 
    public Parser() {
    }

    public Parser(String filename) {
        String bufSize = "1024";
        internalParser(filename, bufSize);
    }
    
    public Parser(String filename, String bufSize) {
        internalParser(filename, bufSize);
    }
    
    
    private void internalParser(String filename, String bufSize) 
    {
                int bufferSize =  Integer.parseInt(bufSize);
                FileReader source = null;
                BufferedReader buf;
                char cbuf[] = new char[bufferSize];  //large               
                String xmlBuffer=null;
                String xmlFragment=null;
                int bytesRead = 0;
                try{
                    source  = new FileReader(filename);
                } catch (IOException e){
                System.out.println("Error reading file" );
                System.out.println(e);
                }
                buf = new BufferedReader(source);
                try{
                    while (bytesRead >=0 && buf.ready()){
                        cbuf = new char[bufferSize];
                        bytesRead = buf.read(cbuf); // returns -1 at end of stream
                        xmlBuffer = null;
                        xmlBuffer = new String(cbuf);
                        xmlBuffer=xmlFragment+xmlBuffer; // take fragment and add what is read from buffer
                        xmlFragment = parseElementInfo(xmlBuffer);
                    }
                    parseElements("<" +xmlFragment);
                }catch (IOException e){
                       System.out.println(e);
                }

        }
               
  
        private String parseElementInfo(String xmlString )
        {
                String beginTagToSearch = "<";
		int index = xmlString.indexOf(beginTagToSearch);
                int lastIndex = xmlString.indexOf( beginTagToSearch, index+1);                
		while(lastIndex >  index ){
                    String subs = xmlString.substring(index, lastIndex) ;                    
                    parseElements(subs);
		    try {
			xmlString = xmlString.substring(lastIndex);
                    }
		    catch(Exception e) {
			System.out.println("string Error");
                        xmlString = "";
		    }
        	    index = xmlString.indexOf(beginTagToSearch);
	            lastIndex = xmlString.indexOf(beginTagToSearch, index+1);                            
		}	
                return (xmlString);
        }    

        private void parseElements(String xmlString){
            int numTokens;
            int nest_level;
            int i;
            int offset;
            String endString = null;
            String[] tokens;
            //System.out.println(xmlString);

            if (xmlString.startsWith("<?") || xmlString.startsWith("<!") ||  xmlString.startsWith("<</")  );  //ignore PI and final end tags
            else{
                 if (xmlString.startsWith("</")  ){  // byproduct of last tag
                     offset = xmlString.indexOf(">");  
                     if ( offset > 0){
                       endString = xmlString.substring(2, offset);
                     }
                     String testString = elementStack.pop();
                     if ( !endString.equals(testString)){
                         System.out.println("Parsing error near: "+ xmlString + "  element number " + elementCount );
                         System.exit(0);
                     }
                     nestLevel--;
                 }else{
                      offset = xmlString.indexOf("/>"); // is this empty string
                      if ( offset > 0) {                 
                          nestLevel++;  
                          xmlString= xmlString.replace('\n', ' ');  //suppress newline
                          xmlString = xmlString.replace('\t', ' ');  // suppress tab
                          xmlString= xmlString.replace('\r', ' '); //suppression des retours chariot
                          xmlString = Utility.trim(xmlString); // added March 8, 2003                          
                          offset = xmlString.indexOf(" ");  // the get tag name based on SPACE
                          if ( offset > 0){
                              tagName = xmlString.substring(1, offset);
                              e = new Element(tagName, xmlString.substring(offset), nestLevel);
                              elements.add(e);
                              nestLevel--;
                              elementCount++;
                          }
                       }else{  //not an empty string
                           xmlString= xmlString.replace('\n', ' ');  //suppress newline
                           xmlString = xmlString.replace('\t', ' ');  // suppress tab
                           xmlString = Utility.trim(xmlString);
                           StringTokenizer t = new StringTokenizer(xmlString, " >", true);
                           String firstToken = t.nextToken();
                           offset = firstToken.length();
                           if ( offset > 0){
                               nestLevel++;                       
                               tagName = xmlString.substring(1, offset);
                               elementStack.push(tagName);                        
                               e = new Element(tagName, xmlString.substring(offset+1), nestLevel);
                               elements.add(e);
                               elementCount++;                        
                          }
                       } // end else
                      }
                 }
            }
        
        public Enumeration getElements(){
            Enumeration e = elements.elements();
            return(e);
        }
        
     public static void main(String[] args) {
           Parser test = new Parser(args[0]);
           Enumeration e = test.getElements();
           while (e.hasMoreElements()){
              Element elem = (Element) e.nextElement();
              System.out.println (elem.toString());
          }
 
           System.exit(0);
        } // main

}
    
    

