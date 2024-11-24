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
 * Repopulate the database with test data
 *
 * @author Mike Smith University of Brighton
 * @version 3.0 Derby
 */
class Setup {
    private static final Logger setupLogger = LogManager.getLogger(Setup.class);

    public static void main(String[] args) {

        Connection conn = null;
        DBAccess db = new DBAccess();
        DBAccessFactory.setAction("Create");
        setupLogger.debug("Setup CatShop database of stock items");

        try {
            db = (new DBAccessFactory()).getNewDBAccess();
            db.loadDriver();
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

    private static void query(Statement stmt, String url, String stm) {
        try {
            ResultSet res = stmt.executeQuery(stm);

            ArrayList<String> names = new ArrayList<>(10);

            ResultSetMetaData md = res.getMetaData();
            int cols = md.getColumnCount();

            for (int j = 1; j <= cols; j++) {
                String name = md.getColumnName(j);
                System.out.printf("%-14.14s ", name);
                names.add(name);
            }
            System.out.println();

            for (int j = 1; j <= cols; j++) {
                System.out.printf("%-14.14s ", md.getColumnTypeName(j));
            }
            System.out.println();

            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    String name = names.get(j);
                    System.out.printf("%-14.14s ", res.getString(name));
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("problems with SQL sent to " + url + "\n" + e.getMessage());
        }
    }

    private static String m(int len, String s) {
        if (s.length() >= len) {
            return s.substring(0, len - 1) + " ";
        } else {
            StringBuilder res = new StringBuilder(len);
            res.append(s);
            for (int i = s.length(); i < len; i++) res.append(' ');
            return res.toString();
        }
    }

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
            // felt pretty cool writing this one ;)
            // setupLogger.trace("SQL AS FOLLOWS: {}", sb.toString());
            SQL = SQLStatements.toArray(String[]::new);
        } catch (IOException e) {
            setupLogger.fatal("Can't find /config/init.sql. Is it on the modulepath?", e);
            System.exit(1);
        }
        // setupLogger.debug("SQL processed as: {}", SQL);
        return SQL;
    }
}
