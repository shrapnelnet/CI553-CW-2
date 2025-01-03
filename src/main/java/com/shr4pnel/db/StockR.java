/**
 * Implements Read access to the stock list The stock list is held in a relational DataBase
 *
 * @author shrapnelnet
 */
package com.shr4pnel.db;

import com.google.gson.Gson;
import com.shr4pnel.middleware.StockException;
import com.shr4pnel.schemas.GetAllStockHelper;
import com.shr4pnel.schemas.PackingHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implements read only access to the stock database.
 */
public class StockR {
    private static final Logger stockRLogger = LogManager.getLogger(StockR.class);
    private Connection conn = null; // Connection to database
    private Statement theStmt = null; // Statement object
    private PreparedStatement preparedStatement;

    /**
     * Connects to database Uses a factory method to help setup the connection
     *
     * @throws StockException if problem
     */
    public StockR() throws StockException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            conn = DriverManager.getConnection(dbDriver.urlOfDatabase());
            theStmt = conn.createStatement();
        } catch (SQLException e) {
            stockRLogger.error("Error during SQL execution", e);
        } catch (Exception e) {
            throw new StockException("Cannot load database driver.");
        }
    }

    /**
     * Returns a statement object that is used to process SQL statements
     *
     * @return A statement object used to access the database
     */
    protected Statement getStatementObject() {
        return theStmt;
    }

    /**
     * Returns a connection object that is used to process requests to the DataBase
     *
     * @return a connection object
     */
    protected Connection getConnectionObject() {
        return conn;
    }

    public synchronized String getAllStock() {
        ResultSet res;
        String json = null;
        try (Statement stmt = conn.createStatement()) {
            res = stmt.executeQuery("SELECT STOCKLEVEL, P.PRODUCTNO, P.DESCRIPTION, P.PRICE FROM STOCKTABLE INNER JOIN APP.PRODUCTTABLE P on STOCKTABLE.PRODUCTNO = P.PRODUCTNO");
            ArrayList<GetAllStockHelper> jsonArray = new ArrayList<>();
            while (res.next()) {
                long stockLevel = res.getLong("STOCKLEVEL");
                long price = res.getLong("PRICE");
                String name = res.getString("DESCRIPTION");
                String pNum = res.getString("PRODUCTNO");
                GetAllStockHelper currentRow = new GetAllStockHelper(name, price, stockLevel, pNum);
                jsonArray.add(currentRow);
            }

            Object[] jsonArrayComplete = jsonArray.toArray();
            Gson gson = new Gson();
            json = gson.toJson(jsonArrayComplete);
        } catch (SQLException e) {
            stockRLogger.error("Failed to fetch results. Is the database up?");
        }
        return json;
    }

    public synchronized String getAllOrdersToPack() {
        Gson gson = new Gson();
        String json = null;
        ResultSet rs;
        ArrayList<PackingHelper> packingHelperArrayList = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery("SELECT O.DATEORDERED, BASKETTABLE.PRODUCTNO, BASKETTABLE.QUANTITY, O.ORDERID FROM BASKETTABLE INNER JOIN ORDERTABLE O ON BASKETTABLE.ORDERID = O.ORDERID");
            while (rs.next()) {
                Date orderDate = rs.getDate(1);
                String pNum = rs.getString(2);
                int quantity = rs.getInt(3);
                String uuid = rs.getString(4);
                packingHelperArrayList.add(new PackingHelper(orderDate, pNum, quantity, uuid));
            }
        } catch (SQLException e) {
            stockRLogger.error("Failed to fetch all orders to pack.", e);
        }
        Object[] packingHelperArray = packingHelperArrayList.toArray();
        return gson.toJson(packingHelperArray);
    }
}
