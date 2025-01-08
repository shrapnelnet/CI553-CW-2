package com.shr4pnel.db;

/**
 * Implements Read /Write access to the stock list The stock list is held in a relational DataBase
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
import com.shr4pnel.middleware.StockException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


/**
 * Implements read/write access to the stock database.
 *
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 * @see com.shr4pnel.web.WebApplication
 */
public class StockRW extends StockR {
    private static final Logger stockRWLogger = LogManager.getLogger(StockRW.class);

    /*
     * Connects to database
     */
    /**
     * <p>Constructor for StockRW.</p>
     *
     * @throws com.shr4pnel.middleware.StockException if any.
     */
    public StockRW() throws StockException {
        super(); // Connection done in StockR's constructor
    }

    /**
     * Adds stock (Re-stocks) to the store. Assumed to exist in database.
     *
     * @param pNum Product number
     * @param amount Amount of stock to add
     */
    public synchronized void addStock(String pNum, int amount) {
        stockRWLogger.debug("UPDATE STOCKTABLE SET STOCKLEVEL = STOCKLEVEL + {} WHERE PRODUCTNO = {}", amount, pNum);
        try (PreparedStatement ps = getConnectionObject().prepareStatement("UPDATE STOCKTABLE SET STOCKLEVEL = STOCKLEVEL + ? WHERE PRODUCTNO = ?")) {
            ps.setInt(1, amount);
            ps.setString(2, pNum);
            ps.executeUpdate();
            stockRWLogger.trace("StockRW: addStock({}, {})", pNum, amount);
        } catch (SQLException e) {
            stockRWLogger.error("Failed to add new stock", e);
        }
    }

    /**
     * Adds a new order to the OrderTable.
     *
     * @param uuid A UUID, which is used as a primary key, as well as a foreign key in BasketTable
     */
    public synchronized void addOrder(UUID uuid) {
        Date date = new Date(System.currentTimeMillis());
        stockRWLogger.debug("INSERT INTO ORDERTABLE VALUES({},{})", uuid, date);
        try (PreparedStatement ps = getConnectionObject().prepareStatement("INSERT INTO ORDERTABLE VALUES (?,?)")) {
            ps.setString(1, String.valueOf(uuid));
            ps.setDate(2, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            stockRWLogger.error("Failed to add order to OrderTable", e);
        }
    }

    /**
     * Add an item to the BasketTable
     *
     * @param orderID The ID of the order that the item belongs to
     * @param pNum The Product Number of the item
     * @param quantity The amount of the item to add
     */
    public synchronized void addBasket(UUID orderID, String pNum, int quantity) {
        try (PreparedStatement ps = getConnectionObject().prepareStatement("INSERT INTO BASKETTABLE VALUES (?,?,?,?)")) {
            UUID uuid = UUID.randomUUID();
            stockRWLogger.debug("INSERT INTO BASKETTABLE VALUES({},{},{},{})", uuid, orderID, pNum, quantity);
            ps.setString(1, String.valueOf(uuid));
            ps.setString(2, String.valueOf(orderID));
            ps.setString(3, pNum);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            stockRWLogger.error("Failed to add basket item", e);
        }
    }

    /**
     * Deletes an order, and all associated basket items from the database to simulate the order being packed.
     *
     * @param orderID The order ID to be removed
     * @return true, if successful
     */
    public synchronized boolean packOrder(String orderID) {
        try (
                PreparedStatement deleteBasket = getConnectionObject().prepareStatement("DELETE FROM BASKETTABLE WHERE orderid=?");
                PreparedStatement deleteOrder = getConnectionObject().prepareStatement("DELETE FROM ORDERTABLE WHERE orderid=?")
            ) {
            // basket has to be deleted first due to foreign key constraints. always !!
            deleteBasket.setString(1, orderID);
            deleteOrder.setString(1, orderID);
            deleteBasket.executeUpdate();
            stockRWLogger.debug("DELETE FROM BASKETTABLE WHERE orderid={}", orderID);
            deleteOrder.executeUpdate();
            stockRWLogger.debug("DELETE FROM ORDERTABLE WHERE orderid={}", orderID);
            return true;
        } catch (IllegalArgumentException e) {
            stockRWLogger.error("Order does not exist", e);
            return false;
        } catch (SQLException e) {
            stockRWLogger.error("Failed to delete packed orders.", e);
            return false;
        }
    }
}

