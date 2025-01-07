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
 * @since v0.1.0
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 */
public class StockR {
    private static final Logger stockRLogger = LogManager.getLogger(StockR.class);
    private Connection conn = null; // Connection to database

    /**
     * Connects to database Uses a factory method to help set up the connection
     *
     * @throws StockException if problem
     */
    public StockR() throws StockException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            conn = DriverManager.getConnection(dbDriver.urlOfDatabase());
        } catch (SQLException e) {
            stockRLogger.error("Error during SQL execution", e);
        } catch (Exception e) {
            throw new StockException("Cannot load database driver.");
        }
    }

    /**
     * Used to access the current connection to the database
     * @return The current connection to the database
     */
    protected Connection getConnectionObject() {
        return conn;
    }

    /**
     * Used to access the contents of the StockTable
     * @return Stringified JSON array containing current stock-list
     */
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

    /**
     * Get all orders in the OrderTable
     * @return A stringified JSON array containing the contents of the OrderTable, as well as any items in the BasketTable referenced by an OrderTable foreign key
     */
    public synchronized String getAllOrdersToPack() {
        Gson gson = new Gson();
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
