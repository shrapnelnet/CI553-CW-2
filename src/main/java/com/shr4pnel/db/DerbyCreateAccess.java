package com.shr4pnel.db;

import java.sql.DriverManager;

class DerbyCreateAccess extends DBAccess {
    private static final String URLdb = "jdbc:derby:derby;create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * JDBC should automatically register the driver
     * TODO: is this really needed?
     * pps. original way this was done was last used during java 3. what the hell?
     * @throws Exception
     */
    public void loadDriver() throws Exception {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
    }

    public String urlOfDatabase() {
        return URLdb;
    }
}
