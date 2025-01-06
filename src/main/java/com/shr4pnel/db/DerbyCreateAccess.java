package com.shr4pnel.db;

class DerbyCreateAccess extends DBAccess {
    private static final String URLdb = "jdbc:derby:derby;create=true";
    public String urlOfDatabase() {
        return URLdb;
    }
}
