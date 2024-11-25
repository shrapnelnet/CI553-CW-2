/**
 * Implements Read access to the stock list The stock list is held in a relational DataBase
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
package com.shr4pnel.db;

import com.google.gson.Gson;
import com.shr4pnel.catalogue.Product;
import com.shr4pnel.gsonhelpers.GetAllStockHelper;
import com.shr4pnel.middleware.StockException;
import com.shr4pnel.middleware.StockReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/** Implements read only access to the stock database. */
public class StockR implements StockReader {
    private static final Logger StockRLogger = LogManager.getLogger(StockR.class);
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
            dbDriver.loadDriver();
            conn = DriverManager.getConnection(dbDriver.urlOfDatabase());
            theStmt = conn.createStatement();
        } catch (SQLException e) {
            StockRLogger.error("Error during SQL execution", e);
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

    /**
     * Checks if the product exits in the stock list
     *
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String pNum) throws StockException {

        try {
            ResultSet rs =
                    getStatementObject()
                            .executeQuery(
                                    "select price from ProductTable "
                                            + "  where  ProductTable.productNo = '"
                                            + pNum
                                            + "'");
            boolean res = rs.next();
            StockRLogger.trace("Db StockR: exists({}) -> {}", pNum, res);
            return res;
        } catch (SQLException e) {
            throw new StockException("SQL exists: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list. Assumed to exist in database.
     *
     * @param pNum The product number
     * @return Details in an instance of a Product
     */
    public synchronized Product getDetails(String pNum) throws StockException {
        try {
            Product dt = new Product("0", "", 0.00, 0);
            ResultSet rs =
                    getStatementObject()
                            .executeQuery(
                                    "select description, price, stockLevel "
                                            + "  from ProductTable, StockTable "
                                            + "  where  ProductTable.productNo = '"
                                            + pNum
                                            + "' "
                                            + "  and    StockTable.productNo   = '"
                                            + pNum
                                            + "'");
            if (rs.next()) {
                dt.setProductNum(pNum);
                dt.setDescription(rs.getString("description"));
                dt.setPrice(rs.getDouble("price"));
                dt.setQuantity(rs.getInt("stockLevel"));
            }
            rs.close();
            return dt;
        } catch (SQLException e) {
            throw new StockException("SQL getDetails: " + e.getMessage());
        }
    }

    /**
     * Returns 'image' of the product
     *
     * @param pNum The product number Assumed to exist in database.
     * @return ImageIcon representing the image
     */
    public synchronized ImageIcon getImage(String pNum) throws StockException {
        String filename = "default.jpg";
        try {
            ResultSet rs =
                    getStatementObject()
                            .executeQuery(
                                    "select picture from ProductTable "
                                            + "  where  ProductTable.productNo = '"
                                            + pNum
                                            + "'");

            boolean res = rs.next();
            if (res) filename = rs.getString("picture");
            rs.close();
        } catch (SQLException e) {
            StockRLogger.fatal("Failed to fetch product image", e);
            throw new StockException("Failed to fetch product image");
        }

        // DEBUG.trace( "DB StockR: getImage -> %s", filename );
        return new ImageIcon(filename);
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
            StockRLogger.error("Failed to fetch results. Is the database up?");
        }
        return json;
    }
}
