/**
 * Implements generic management of a database.
 * @author shrapnelnet
 */
package com.shr4pnel.db;

/**
 * Base class for all database accessors.
 *
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 */
public class DBAccess {
    private final String connURL = "jdbc:derby:derby";

    /**
     * Returns hard-coded link to Derby database
     *
     * @return "jdbc:derby:derby"
     */
    public String urlOfDatabase() {
        return connURL;
    }
}
