/**
 * @author Mike Smith University of Brighton
 * @version 3.0
 */

package com.shr4pnel.db;

import com.shr4pnel.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manages the starting up of the database.
 * The database may be Access, mySQL etc.
 */

// Pattern: Abstract Factory
//          Fix to be 

public class DBAccessFactory {
    private static String action;
    private static String database;
    private static String os;

    public static void setAction(String name) {
        action = name;
    }

    private static String setEnvironment() {
        database = fileToString("Database") + action;
        String os = System.getProperties().getProperty("os.name");
        String arch = System.getProperties().getProperty("os.arch");
        String osVer = System.getProperties().getProperty("os.version");
        os = String.format("%s %s %s", os, osVer, arch);
        System.out.println(os);
        return os;
    }

    /**
     * return as a string the contents of a file
     * stripping out newline and carriage returns from contents
     * @param file File name
     * @return contents of a file as a string
     */
    private static String fileToString(String file) {
        byte[] vec = fileToBytes(file);
        return new String(vec).replaceAll("\n", "").replaceAll("\r", "");
    }

    /**
     * Return contents of file as a byte vector
     * @param file File name
     * @return contents as byte array
     */
    private static byte[] fileToBytes(String file) {
        byte[] vec = new byte[0];
        try {
            final int len = (int) length(file);
            if (len < 1000) {
                vec = new byte[len];
                FileInputStream istream = new FileInputStream(file);
                final int read = istream.read(vec, 0, len);
                istream.close();
                return vec;
            } else {
                Logger.error("File %s length %d bytes too long",
                        file, len);
            }
        } catch (FileNotFoundException err) {
            Logger.error("File does not exist: fileToBytes [%s]\n", file);
            System.exit(0);
        } catch (IOException err) {
            Logger.error("IO error: fileToBytes [%s]\n", file);
            System.exit(0);
        }
        return vec;
    }

    /**
     * return number of characters in file
     * @param path File name
     * @return Number of characters in file
     */
    private static long length(String path) {
        try {
            File in = new File(path);
            return in.length();
        } catch (SecurityException err) {
            Logger.error("Security error: length of file [%s]\n", path);
            System.exit(0);
        }
        return -1;
    }

    /**
     * Return an object to implement system level access to the database.
     * @return An object to provide system level access to the database
     */
    public DBAccess getNewDBAccess() {
        setEnvironment();
        Logger.traceA("Using [%s] as database type\n", database);
        switch (database) {
            case "Derby":
                return new DerbyAccess();       // Derby

            case "DerbyCreate":
                return new DerbyCreateAccess(); // Derby & create database

            case "Access":
            case "AccessCreate":
                return new WindowsAccess();     // Access Windows

            case "mySQL":
            case "mySQLCreate":
                return new LinuxAccess();       // MySQL Linux

            default:
                Logger.error("DataBase [%s] not known\n", database);
                System.exit(0);
        }
        return new DBAccess();               // Unknown
    }

}

