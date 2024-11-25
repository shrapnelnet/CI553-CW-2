/**
 * @author Mike Smith University of Brighton
 * @version 3.0
 */
package com.shr4pnel.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Manages the starting up of the database. The database may be Access, mySQL etc. */

// Pattern: Abstract Factory
//          Fix to be

public class DBAccessFactory {
    private static final Logger DBAccessFactoryLogger = LogManager.getLogger(DBAccessFactory.class);
    private static final String database = "Derby";
    private static String action = "";
    private static String os;

    public static void setAction(String name) {
        action = name;
    }

    private static String setEnvironment() {
        os = System.getProperties().getProperty("os.name");
        String arch = System.getProperties().getProperty("os.arch");
        String osVer = System.getProperties().getProperty("os.version");
        String sysInfo = String.format("%s %s %s", os, osVer, arch);
        DBAccessFactoryLogger.debug("System information: {}", sysInfo);
        return os;
    }

    /**
     * Return an object to implement system level access to the database.
     *
     * @return An object to provide system level access to the database
     */
    public DBAccess getNewDBAccess() {
        setEnvironment();
        DBAccessFactoryLogger.trace("Using {} as database type", database);
        switch (database + action) {
            case "Derby":
                return new DerbyAccess(); // Derby

            case "DerbyCreate":
                return new DerbyCreateAccess(); // Derby & create database

            case "Access":
            case "AccessCreate":
                return new WindowsAccess(); // Access Windows

            case "mySQL":
            case "mySQLCreate":
                return new LinuxAccess(); // MySQL Linux

            default:
                DBAccessFactoryLogger.error("Instruction {} not implemented", database);
                System.exit(0);
        }
        return new DBAccess(); // Unknown
    }
}
