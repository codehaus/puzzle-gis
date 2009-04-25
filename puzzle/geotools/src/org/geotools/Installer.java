/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools;

import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //I made several tryes to avoid having the log4j.xml classpath error,
        //looks like this logging api doesn't provide any other way to configure itself
        //without config file. that's really annoying.

        Properties properties = new Properties();

        // Set root logger level to DEBUG and its only appender to A1.
        // log4j.rootLogger=DEBUG, A1
        properties.put("log4j.rootLogger", "DEBUG, A1");


        // A1 is set to be a ConsoleAppender.
        // log4j.appender.A1=org.apache.log4j.ConsoleAppender
        properties.put("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");

        // A1 uses PatternLayout.
        // log4j.appender.A1.layout=org.apache.log4j.PatternLayout
        // log4j.appender.A1.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
        properties.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
        properties.put("log4j.appender.A1.layout.ConversionPattern", "%-4r [%t] %-5p %c %x - %m%n");

        PropertyConfigurator.configure(properties);
    }
}
