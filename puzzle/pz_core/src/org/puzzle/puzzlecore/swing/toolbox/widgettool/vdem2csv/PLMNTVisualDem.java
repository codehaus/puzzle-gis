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

package org.puzzle.puzzlecore.swing.toolbox.widgettool.vdem2csv;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;


/**
 *
 * @author Johann
 */
public class PLMNTVisualDem {
    
    
    private int	longueur = 0;
    private int	n = 0;
    private int	val = 0;
    private int coordX = 0;
    private int coordY = 0;
    private boolean coordrec = false;
    private ArrayList<ArrayList<Integer>> bloc_alti = new ArrayList<ArrayList<Integer>>();
    private float[][][] vertex = new float[258][258][4];
    private int pas = 75;
    
    private float minalti = 0;
    private float maxalti = 0;
    
    
    /** Creates a new instance of PLMNTVisualDem */
    public PLMNTVisualDem() {
    }
    
    
    private void reset(){
        longueur = 0;
        n = 0;
        val = 0;
        coordX = 0;
        coordY = 0;
        coordrec = false;
        bloc_alti = new ArrayList<ArrayList<Integer>>();
        vertex = new float[258][258][4];
        pas = 75;
        
        minalti = 0;
        maxalti = 0;
    }
    
    
    
    public int[] recoverGeoref(String adresse){
        int[] ref = new int[2];
        
        try{
            String coord = adresse.substring(adresse.length()-8,adresse.length()-4);
            coordY = Integer.valueOf( coord.substring(coord.length()-2,coord.length()) ).intValue() ;
            
            char l1 = coord.charAt(0);
            l1 = Character.toUpperCase(l1);
            char l2 = coord.charAt(1);
            l2 = Character.toUpperCase(l2);
            
            switch (l1) {
                case 'A' :coordX = 0; break;
                case 'B' :coordX = 26; break;
                case 'C' :coordX = 52; break;
            }
            
            switch (l2) {
                case 'A' :coordX += 0; break;
                case 'B' :coordX += 1; break;
                case 'C' :coordX += 2; break;
                case 'D' :coordX += 3; break;
                case 'E' :coordX += 4; break;
                case 'F' :coordX += 5; break;
                case 'G' :coordX += 6; break;
                case 'H' :coordX += 7; break;
                case 'I' :coordX += 8; break;
                case 'J' :coordX += 9; break;
                case 'K' :coordX += 10; break;
                case 'L' :coordX += 11; break;
                case 'M' :coordX += 12; break;
                case 'N' :coordX += 13; break;
                case 'O' :coordX += 14; break;
                case 'P' :coordX += 15; break;
                case 'Q' :coordX += 16; break;
                case 'R' :coordX += 17; break;
                case 'S' :coordX += 18; break;
                case 'T' :coordX += 19; break;
                case 'U' :coordX += 20; break;
                case 'V' :coordX += 21; break;
                case 'W' :coordX += 22; break;
                case 'X' :coordX += 23; break;
                case 'Y' :coordX += 24; break;
                case 'Z' :coordX += 25; break;
            }
            
            coordX = coordX*pas*256 ;
            coordY = (coordY-1)*pas*256  ;
            //System.out.println( coord +" X=" + coordX + " Y=" + coordY );
            coordrec = true;
            
            ref[0] = coordX;
            ref[1] = coordY;
            
        }catch(Exception e){
            coordrec = false;
        }
        
        return ref;
    }
    
    
        
    
    /*public JPanel visualiser(String entree){
        reset();
        load(entree);
        updateColor();
        
        Zone3D zone = new Zone3D();
        zone.getGraphicListener().addObjet(new MNTVDemGLObject(vertex));
        
        zone.getCamera().setMoveFactor(3);
        zone.getCamera().setRoteFactor(2);
        zone.getCamera().setPosition( (258f/75f)/2f ,maxalti/20f+10f, 0);
        zone.getCamera().setDirection( (258f/75f)/2f , minalti, (258f/75f)/2f );
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(BorderLayout.NORTH,zone.getGLControlPanel());
        p.add(BorderLayout.CENTER,zone.getGLPanel());
                 
        return p;
                
    }*/
    
    
    public boolean load(String enter){
        
        reset();
        
        try{
            InputStream ips = new FileInputStream(enter);
            
            //OK on passe l'entete, 2048 octets
            ips.skip(2048);
            //System.out.println("taille entete : 2048");
            
            ArrayList<Integer> bloc_se = new ArrayList<Integer>();
            //OK on stock notre bloc de donnees
            while ( (val = ips.read()) != -1 ){
                bloc_se.add(val);
            }
            ips.close();
            //System.out.println("taille bloc sans entete : " + bloc_se.size());
            
            
            //OK on divise en ligne notre bloc
            ArrayList<ArrayList<Integer>> bloc_pl = new ArrayList<ArrayList<Integer>>();
            long lng_diviser = 0;
            for( int X = 0 ; X < 258 ; X++ ){
                longueur = bloc_se.get(n) * 256;
                n++;
                longueur = longueur + bloc_se.get(n);
                n++;
                lng_diviser += longueur;
                
                ArrayList<Integer> rec = new ArrayList<Integer>();
                for( int i = 0 ; i < longueur ; i++ ){
                    rec.add(bloc_se.get(n));
                    n++;
                }
                
                bloc_pl.add(rec);
            }
            
            //System.out.println("taille bloc dans lignes : " + lng_diviser);
            
            ArrayList<ArrayList<Integer>> bloc_dc = decodage(bloc_pl);
            ajustement(bloc_dc);
            
            updateCoords();
            
            return true;
            
        } catch( Exception e ){
            //e.printStackTrace();
            return false;
        }
        
        
        
    }
    
    
    public boolean convert(String enter, String sortie, boolean cover, float referencementX, float referencementY){
        
        try{
            InputStream ips = new FileInputStream(enter);
            
            //OK on passe l'entete, 2048 octets
            ips.skip(2048);
            //System.out.println("taille entete : 2048");
            
            ArrayList<Integer> bloc_se = new ArrayList<Integer>();
            //OK on stock notre bloc de donnees
            while ( (val = ips.read()) != -1 ){
                bloc_se.add(val);
            }
            ips.close();
            //System.out.println("taille bloc sans entete : " + bloc_se.size());
            
            
            //OK on divise en ligne notre bloc
            ArrayList<ArrayList<Integer>> bloc_pl = new ArrayList<ArrayList<Integer>>();
            long lng_diviser = 0;
            for( int X = 0 ; X < 258 ; X++ ){
                longueur = bloc_se.get(n) * 256;
                n++;
                longueur = longueur + bloc_se.get(n);
                n++;
                lng_diviser += longueur;
                
                ArrayList<Integer> rec = new ArrayList<Integer>();
                for( int i = 0 ; i < longueur ; i++ ){
                    rec.add(bloc_se.get(n));
                    n++;
                }
                
                bloc_pl.add(rec);
            }
            
            //System.out.println("taille bloc dans lignes : " + lng_diviser);
            
            ArrayList<ArrayList<Integer>> bloc_dc = decodage(bloc_pl);
            ajustement(bloc_dc);
            
            updateCoords();
            
            //System.out.println("la");
            save(sortie,cover,referencementX,referencementY);
            return true;
            
        } catch( Exception e ){
            //e.printStackTrace();
            return false;
        }
        
        
        
    }
    
    
    private ArrayList<ArrayList<Integer>> decodage( ArrayList<ArrayList<Integer>> bloc_pl){
        ArrayList<ArrayList<Integer>> bloc_dc = new ArrayList<ArrayList<Integer>>();
        
        //OK on decode nos lignes
        int total = 0;
        for( int X = 0 ; X < 258 ; X++ ){
            
            ArrayList<Integer> arr = bloc_pl.get(X);
            ArrayList<Integer> rec = new ArrayList<Integer>();
            
            n = 0;
            total = 0;
            do{
                
                val = arr.get(n);
                //System.out.print(val + ">");
                n++;
                total++;
                
                if( val >= 128 ){
                    int decal = 257 - val;
                    total += decal;
                    //System.out.print(decal + " ");
                    for( int t = 0 ; t < decal ; t++ ){
                        rec.add(arr.get(n));
                    }
                    n++;
                    
                } else{
                    int decal = val + 1;
                    total += decal;
                    //System.out.print(decal + " ");
                    for( int t = 0 ; t < decal ; t++ ){
                        rec.add(arr.get(n));
                        n++;
                    }
                }
            }
            while ( n < arr.size() );
            
            bloc_dc.add(rec);
            
            
        }
        //System.out.println("\n" + total);
        return bloc_dc;
    }
    
    private void ajustement( ArrayList<ArrayList<Integer>> bloc_dc){
        
        // on ajuste l'altitude
        for( int X = 0 ; X < 258 ; X++ ){
            ArrayList<Integer> arr = bloc_dc.get(X);
            ArrayList<Integer> rec = new ArrayList<Integer>();
            for( int i = 0 ; i < 258 ; i++ ){
                rec.add(0);
            }
            
            int raise = 0;
            n = 0;
            
            int altitude = arr.get(n) * 256 + arr.get(n + 1);
            n += 2;
            rec.set(raise, altitude);
            raise++;
            
            
            do{
                val = arr.get(n);
                n++;
                
                if( val == 128 ){
                    altitude = arr.get(n) * 256 + arr.get(n + 1);
                    n += 2;
                } else if( val < 128 )
                    altitude += val;
                else if( val > 128 )
                    altitude -= (256 - val);
                
                rec.set(raise, altitude);
                raise++;
            }
            while ( n < arr.size() );
            
            bloc_alti.add(rec);
        }
        
    }
    
    private void updateCoords(){
        
        for( int X=0; X<258; X++ ){
            for( int Y=0; Y<258; Y++ ){
                vertex[X][Y][0] = (X*pas);
                vertex[X][Y][1] = (Y*pas);
                vertex[X][Y][2] = bloc_alti.get(Y).get(X);
            }
        }
    }
    
    
    public void updateColor(){
        
        
        try{
            for( int X=0; X<258; X++ ){
                for( int Y=0; Y<258; Y++ ){
                    if( bloc_alti.get(Y).get(X) >= maxalti )
                        maxalti = bloc_alti.get(Y).get(X);
                    if( bloc_alti.get(Y).get(X) <= minalti )
                        minalti = bloc_alti.get(Y).get(X);
                }
            }
            
            for( int X=0; X<258; X++ ){
                for( int Y=0; Y<258; Y++ ){
                    vertex[X][Y][3] = (bloc_alti.get(Y).get(X) - minalti) / (maxalti-minalti);
                }
            }
        }catch(Exception e){}
    }
    
    
    public void save( String adr, boolean recouvrement, float referencementX, float referencementY ) throws IOException{
        
        if( adr == null ){
            
            JFileChooser fc = new JFileChooser();
            fc.setApproveButtonText("Enregistrer");
            fc.setDialogTitle("Enregistrer sous");
            adr = "";
            
            int returnVal = fc.showOpenDialog(null);
            if( returnVal == JFileChooser.APPROVE_OPTION ){
                adr = fc.getSelectedFile().getAbsolutePath();
            }
            
        }
        
        if( adr != "" ){
            
            Vector<String> v = new Vector<String>();
            
            String str = "";
            str = "X";
            str += "\t";
            str += "Y";
            str += "\t";
            str += "Alti";
            str += "\n";
            v.add(str);
            
            
            //referencement france
            //referencementX = 40000f
            //referencementY = 1690000f
            
            
            //si on garde le recouvrement
            if( recouvrement ){
                for( int X=1; X<257; X++ ){
                    for( int Y=1; Y<257; Y++ ){
                        str = String.valueOf( Math.round(referencementX + coordX + vertex[X][Y][0] ) );
                        str += "\t";
                        str += String.valueOf( Math.round(referencementY + coordY + vertex[X][Y][1] ) );
                        str += "\t";
                        str += String.valueOf( Math.round( vertex[X][Y][2]) );
                        str += "\n";
                        v.add(str);
                    }
                }
                
            } else{
                for( int X=0; X<258; X++ ){
                    for( int Y=0; Y<258; Y++ ){
                        str = String.valueOf( Math.round(referencementX + coordX + vertex[X][Y][0] ) );
                        str += "\t";
                        str += String.valueOf( Math.round(referencementY + coordY + vertex[X][Y][1] ) );
                        str += "\t";
                        str += String.valueOf( Math.round( vertex[X][Y][2]) );
                        str += "\n";
                        v.add(str);
                    }
                }
            }
                                    
            write(adr, v);
            
            
        }
        
    }
    
    
    private void write(String adress, List<String> val) throws IOException {        
        FileWriter fw = new FileWriter(adress, false);
        BufferedWriter output = new BufferedWriter(fw);

        int size = val.size();
        for (int i = 0; i < size; i++) {
            output.write(val.get(i));
            output.flush();
        }

        fw.close();
        output.close();
    }
    
    
}
