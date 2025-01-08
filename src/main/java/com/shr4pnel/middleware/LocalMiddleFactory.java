/**
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
package com.shr4pnel.middleware;

import com.shr4pnel.db.StockR;
import com.shr4pnel.db.StockRW;

// Pattern: Abstract Factory

/**
 * A local middlefactory, used to create accesses to the stock readers and writers.
 *
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @see StockR
 * @see StockRW
 */
public class LocalMiddleFactory {
    private static StockR aStockR = null;
    private static StockRW aStockRW = null;

    /**
     * Return an object to access the database for read only access. All users share this same
     * object.
     *
     * @return a {@link com.shr4pnel.db.StockR} object
     * @throws com.shr4pnel.middleware.StockException if any.
     */
    public StockR makeStockReader() throws StockException {
        if (aStockR == null)
            aStockR = new StockR();
        return aStockR;
    }

    /**
     * Return an object to access the database for read/write access. All users share this same
     * object.
     *
     * @return a {@link com.shr4pnel.db.StockRW} object
     * @throws com.shr4pnel.middleware.StockException if any.
     */
    public StockRW makeStockReadWriter() throws StockException {
        if (aStockRW == null) aStockRW = new StockRW();
        return aStockRW;
    }
}
