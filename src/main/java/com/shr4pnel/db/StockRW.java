package com.shr4pnel.db;

/**
 * Implements Read /Write access to the stock list
 * The stock list is held in a relational DataBase
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */

import com.shr4pnel.catalogue.Product;
import com.shr4pnel.logging.Logger;
import com.shr4pnel.middleware.StockException;
import com.shr4pnel.middleware.StockReadWriter;

import java.sql.SQLException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods
// 

/**
 * Implements read/write access to the stock database.
 */
public class StockRW extends StockR implements StockReadWriter {
    /*
     * Connects to database
     */
    public StockRW() throws StockException {
        super();        // Connection done in StockR's constructor
    }

    /**
     * Customer buys stock, quantity decreased if sucessful.
     * @param pNum Product number
     * @param amount Amount of stock bought
     * @return true if succeeds else false
     */
    public synchronized boolean buyStock(String pNum, int amount)
            throws StockException {
        Logger.trace("DB StockRW: buyStock(%s,%d)", pNum, amount);
        int updates = 0;
        try {
            getStatementObject().executeUpdate(
                    "update StockTable set stockLevel = stockLevel-" + amount +
                            "       where productNo = '" + pNum + "' and " +
                            "             stockLevel >= " + amount
            );
            updates = 1; // getStatementObject().getUpdateCount();
        } catch (SQLException e) {
            throw new StockException("SQL buyStock: " + e.getMessage());
        }
        Logger.trace("buyStock() updates -> %n", updates);
        return updates > 0;   // sucess ?
    }

    /**
     * Adds stock (Re-stocks) to the store.
     *  Assumed to exist in database.
     * @param pNum Product number
     * @param amount Amount of stock to add
     */
    public synchronized void addStock(String pNum, int amount)
            throws StockException {
        try {
            getStatementObject().executeUpdate(
                    "update StockTable set stockLevel = stockLevel + " + amount +
                            "         where productNo = '" + pNum + "'"
            );
            //getConnectionObject().commit();
            Logger.trace("DB StockRW: addStock(%s,%d)", pNum, amount);
        } catch (SQLException e) {
            throw new StockException("SQL addStock: " + e.getMessage());
        }
    }


    /**
     * Modifies Stock details for a given product number.
     *  Assumed to exist in database.
     * Information modified: Description, Price
     * @param detail Product details to change stocklist to
     */
    public synchronized void modifyStock(Product detail)
            throws StockException {
        Logger.trace("DB StockRW: modifyStock(%s)",
                detail.getProductNum());
        try {
            if (!exists(detail.getProductNum())) {
                getStatementObject().executeUpdate(
                        "insert into ProductTable values ('" +
                                detail.getProductNum() + "', " +
                                "'" + detail.getDescription() + "', " +
                                "'images/Pic" + detail.getProductNum() + ".jpg', " +
                                "'" + detail.getPrice() + "' " + ")"
                );
                getStatementObject().executeUpdate(
                        "insert into StockTable values ('" +
                                detail.getProductNum() + "', " +
                                "'" + detail.getQuantity() + "' " + ")"
                );
            } else {
                getStatementObject().executeUpdate(
                        "update ProductTable " +
                                "  set description = '" + detail.getDescription() + "' , " +
                                "      price       = " + detail.getPrice() +
                                "  where productNo = '" + detail.getProductNum() + "' "
                );

                getStatementObject().executeUpdate(
                        "update StockTable set stockLevel = " + detail.getQuantity() +
                                "  where productNo = '" + detail.getProductNum() + "'"
                );
            }
            //getConnectionObject().commit();

        } catch (SQLException e) {
            throw new StockException("SQL modifyStock: " + e.getMessage());
        }
    }
}
