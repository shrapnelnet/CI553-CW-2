/**
 * Implements generic management of a database.
 * @author shrapnelnet
 */
package com.shr4pnel.db;

/** Base class that defines the access to the database driver */
public class DBAccess {
    private final String connURL = "jdbc:derby:derby";

    public String urlOfDatabase() {
        return connURL;
    }
}
