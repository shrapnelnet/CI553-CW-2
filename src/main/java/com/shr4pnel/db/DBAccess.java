/**
 * Implements generic management of a database.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
package com.shr4pnel.db;

import java.sql.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Base class that defines the access to the database driver */
public class DBAccess {
    private static final Logger dbAccessLogger = LogManager.getLogger(DBAccess.class);
    private final String connURL = "jdbc:derby:derby";

    public void loadDriver() throws Exception {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
    }

    public String urlOfDatabase() {
        return connURL;
    }
}
