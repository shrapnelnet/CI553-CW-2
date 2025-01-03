/**
 * Creates a valid database in an environment without one.
 * Without this method, the REST API will fail to load
 *
 * @see com.shr4pnel.web.WebApplication
 */

package com.shr4pnel.clients;

import com.shr4pnel.db.DBAccess;
import com.shr4pnel.db.DBAccessFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

/**
 * Populates the database
 *
 * @author shrapnelnet
 */
public class Setup {
    private static final Logger setupLogger = LogManager.getLogger(Setup.class);

    public static void main(String[] args) {
        Connection conn = null;
        DBAccess db = new DBAccess();
        try {
            db = (new DBAccessFactory(true)).getNewDBAccess();

            conn = DriverManager.getConnection(db.urlOfDatabase());
        } catch (SQLException e) {
            setupLogger.fatal(
                    "Problem connecting to {}.\nSQLState: {}\nErrorCode: {}",
                    db.urlOfDatabase(),
                    e.getSQLState(),
                    e.getErrorCode(),
                    e);
            System.exit(-1);
        } catch (Exception e) {
            setupLogger.fatal("Failed to connect to Apache Derby server", e);
            System.exit(-1);
        }
        boolean dbExists = true;
        try {
            ResultSet rs = conn.getMetaData().getCatalogs();
            if (!rs.next()) {
                throw new SQLException("Database does not exist");
            }
            setupLogger.debug("Database already exists. Skipping...");
        } catch (SQLException e) {
            setupLogger.debug("Database does not exist. Reinitializing database.");
            dbExists = false;
        }
        if (dbExists) {
            return;
        }
        String[] queries = getSQLInitializationScript();
        for (String statement : queries) {
            try (Statement stmt = conn.createStatement()) {
                setupLogger.trace("Processing {}", statement);
                // todo write about autoclosables
                if (statement.contains("CREATE") || statement.contains("INSERT")) {
                    stmt.executeUpdate(statement);
                } else {
                    stmt.executeQuery(statement);
                }
            } catch (SQLException e) {
                setupLogger.error("Error occured while processing statement during setup", e);
            }
        }
    }

    /**
     * Loads the initialization script
     *
     * @return A string array containing each line of the initialization script at resources/com/shr4pnel/config/init.sql
     */
    private static String[] getSQLInitializationScript() {
        String[] SQL = null;
        try (InputStream is = Setup.class.getResourceAsStream("/com/shr4pnel/config/init.sql")) {
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> SQLStatements = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                SQLStatements.add(line);
            }
            // setupLogger.trace("SQL PROCESSED AS: {}", sb.toString());
            SQL = SQLStatements.toArray(String[]::new);
        } catch (IOException e) {
            setupLogger.fatal("Can't find /config/init.sql. Is it on the classpath?", e);
            System.exit(1);
        }
        // setupLogger.debug("SQL processed as: {}", SQL);
        return SQL;
    }
}
