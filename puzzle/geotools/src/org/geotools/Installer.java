/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import javax.sql.DataSource;
import org.geotoolkit.factory.AuthorityFactoryFinder;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.referencing.factory.epsg.ThreadedEpsgFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.openide.filesystems.FileUtil;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        //register the EPSG database

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:"+System.getProperty("user.home").replace(File.separatorChar, '/') +"/.puzzle/derbyepsg";
        String user = "puzzle";
        String password = "puzzlepass";

        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        try{
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println("DB is valid, use it");
        }catch(Exception ex){
            System.out.println("Create EPSG DB");
            try {
//                FileUtil.getArchiveFile(arg0)
                Class.forName(driver).newInstance();
                InputStream scriptpath0 = Installer.class.getResourceAsStream("/org/geotools/EPSG_v6_18.mdb_Data_PostgreSQL.sql");
                InputStream scriptpath1 = Installer.class.getResourceAsStream("/org/geotools/EPSG_v6_18.mdb_FKeys_PostgreSQL.sql");
                InputStream scriptpath2 = Installer.class.getResourceAsStream("/org/geotools/EPSG_v6_18.mdb_Tables_PostgreSQL.sql");

                String temppath = System.getProperty("java.io.tmpdir") + File.separator + "scriptSQL";
                File tempFolder = new File(temppath);
                tempFolder.mkdirs();

                copyFile(tempFolder, scriptpath0,"EPSG_v6_18.mdb_Data_PostgreSQL.sql");
                copyFile(tempFolder, scriptpath1,"EPSG_v6_18.mdb_FKeys_PostgreSQL.sql");
                copyFile(tempFolder, scriptpath2,"EPSG_v6_18.mdb_Tables_PostgreSQL.sql");

                EpsgScriptRunner.createDerby(url, tempFolder.getPath(), user, password);
            } catch (Exception ex1) {
                Exceptions.printStackTrace(ex1);
            }
        }finally {
            System.out.println("registerDB");
            DataSource source = new DefaultDataSource(url,props);
            ThreadedEpsgFactory factory = new ThreadedEpsgFactory(source);
            AuthorityFactoryFinder.addAuthorityFactory(factory);
        }

    }

    private static void copyFile(File folder, InputStream in, String name){

        try {
            File f2 = new File(folder.getPath() + "/" + name);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
