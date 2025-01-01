/**
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
package com.shr4pnel.middleware;

import com.shr4pnel.db.StockR;
import com.shr4pnel.db.StockRW;

/**
 * Provide access to middle tier components. Now only one instance of each middle tier object is
 * created
 */

// Pattern: Abstract Factory

public class LocalMiddleFactory {
    private static StockR aStockR = null;
    private static StockRW aStockRW = null;

    /**
     * Return an object to access the database for read only access. All users share this same
     * object.
     */
    public StockR makeStockReader() throws StockException {
        if (aStockR == null)
            aStockR = new StockR();
        return aStockR;
    }

    /**
     * Return an object to access the database for read/write access. All users share this same
     * object.
     */
    public StockRW makeStockReadWriter() throws StockException {
        if (aStockRW == null) aStockRW = new StockRW();
        return aStockRW;
    }
}
