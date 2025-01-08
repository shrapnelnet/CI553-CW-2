package com.shr4pnel.db;

/**
 * Create and then access a new database,
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 */
class DerbyCreateAccess extends DBAccess {
    private static final String URLdb = "jdbc:derby:derby;create=true";
    /**
     * <p>urlOfDatabase.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String urlOfDatabase() {
        return URLdb;
    }
}
