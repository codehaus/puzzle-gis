/*
 * ENV.java
 *
 * Created on 24 janvier 2007, 15:15
 *
 *
 * Copyright 2007 Johann Sorel, Romain Govaere
 *
 * AlterSIG-Convert is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * AlterSIG-Convert is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.puzzle.shell.gdal;

import java.io.File;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Romain
 */
public class ENV {

    public static final String GDAL_PATH_KEY = "path_gdal";
    
    
//    public static ArrayList<ToolListDetail> TOOLS = new ArrayList<ToolListDetail>();
//variables d'environnement
    /**
     * Chemin d'acces au dossier contenant les executable FWTools
//     */
//    private static String FWTOOLSFOLDER;
//    private static String FWOGR2OGR;
//    private static String FWGDAL_TRANSLATE;
//    private static String FWOGRINFO;
//    private static String[] FWENVDATA;
//    private static String FWEXTENSION;
//    /**
//     * pour les cas particuliers
//     */
//    public static String OS;
    
    private static File CHEMIN = null;
    

    public static String getOGR2OGRPath(){
        return getGDALPath() + File.separator + "ogr2ogr";
    }
    
    public static String getOGRINFOPath(){        
        return getGDALPath() + File.separator + "ogrinfo";
    }
    
    public static String getGDALINFOPath(){        
        return getGDALPath() + File.separator + "gdalinfo";
    }
    
    public static String getGDAL_TRANSLATEPath(){        
        return getGDALPath() + File.separator + "gdal_translate";
    }
    
    public static String getGDALPath(){
        Preferences pref = NbPreferences.forModule(ENV.class);
        String path = pref.get(GDAL_PATH_KEY, "");
        return path;
    }
    
    
    /**
     * mise a jour du chemin
     */
    public static void updatePath(File newpath) {

        if (newpath != null) {
            CHEMIN = newpath;
        }
    }

    public static File getPath() {

        if (CHEMIN == null) {
            File[] listeRacine = File.listRoots();
            return listeRacine[0];
        } else {
            return CHEMIN;
        }
    }

//    private static void testOS() {
//
//        OS = System.getProperty("os.name");
//
//        if (OS.toLowerCase().indexOf("win") != -1) {
//            String fullpath = "";
//
//            try {
//                File f = new File("");
//                fullpath = f.getAbsolutePath() + File.separator;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            FWTOOLSFOLDER = "nb6asig" + File.separator  + "gdal" + File.separator + "win" + File.separator + "bin" + File.separator;
//            FWOGR2OGR = FWTOOLSFOLDER + "ogr2ogr";
//            FWGDAL_TRANSLATE = FWTOOLSFOLDER + "gdal_translate";
//            FWOGRINFO = FWTOOLSFOLDER + "ogrinfo";
//            FWEXTENSION = ".exe";
//
//            FWENVDATA = new String[1];
//            FWENVDATA[0] = "XXX=" + fullpath + FWTOOLSFOLDER + ";";
//            System.out.println(FWENVDATA[0]);
//        } else {
//            FWTOOLSFOLDER = "nb6asig" + File.separator  + "gdal" + File.separator + "linux" + File.separator + "bin" + File.separator;
//            FWOGR2OGR = FWTOOLSFOLDER + "ogr2ogr";
//            FWGDAL_TRANSLATE = FWTOOLSFOLDER + "gdal_translate";
//            FWOGRINFO = FWTOOLSFOLDER + "ogrinfo";
//            FWEXTENSION = "";
//
//            FWENVDATA = new String[1];
//            FWENVDATA[0] = FWTOOLSFOLDER;
//        }
//    }
    
       


}