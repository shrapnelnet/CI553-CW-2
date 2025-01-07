package com.shr4pnel.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 * Manages creation of databases, as well as instantiation of existing ones.
 */
public class DBAccessFactory {
    private static final Logger DBAccessFactoryLogger = LogManager.getLogger(DBAccessFactory.class);
    private boolean create = false;

    /**
     * No-arg constructor. Use to get access to an instance of Derby without creating a new database.
     */
    public DBAccessFactory() {}

    /**
     * Access a new instance of Derby, with the option to create a new database.
     * @param create Should a new database be created?
     * @see com.shr4pnel.init.Setup
     */
    public DBAccessFactory(boolean create) {
        this.create = create;
    }

    /**
     * Used to create, or get a handle to the current database instance .
     * @return If class constructed with create=true, a database instance that will be created, otherwise a handle to an existing database
     */
    public DBAccess getNewDBAccess() {
        if (create) {
            DBAccessFactoryLogger.trace("Creating new Apache Derby database");
            return new DerbyCreateAccess();
        }
        DBAccessFactoryLogger.trace("Providing new Apache Derby instance");
        return new DerbyAccess();
    }
}
