package com.shr4pnel.db;

/**
 * Access an existing Derby DB
 *
 * @author shrapnelnet
 * @since v0.1.0
 */
class DerbyAccess extends DBAccess {
    private static final String URLdb = "jdbc:derby:derby";

    /**
     * Return the url to access the database
     *
     * @return url to database
     */
    public String urlOfDatabase() {
        return URLdb;
    }
}
