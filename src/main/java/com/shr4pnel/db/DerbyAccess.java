package com.shr4pnel.db;

import java.sql.DriverManager;

/**
 * Apache Derby database access
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */

class DerbyAccess extends DBAccess {
    private static final String URLdb = "jdbc:derby:derby";

    /**
     * Load the Apache Derby database driver
     */
    public void loadDriver() throws Exception {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
    }

    /**
     * Return the url to access the database
     *
     * @return url to database
     */
    public String urlOfDatabase() {
        return URLdb;
    }
}

