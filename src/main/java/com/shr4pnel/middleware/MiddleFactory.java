/**
 * @author Mike Smith University of Brighton Interface Middle factory
 * @version 2.0
 */
package com.shr4pnel.middleware;

import com.shr4pnel.db.StockR;

/** Provide access to middle tier components. */

// Pattern: Abstract Factory

public interface MiddleFactory {

    /**
     * Return an object to access the database for read only access
     *
     * @return instance of StockReader
     * @throws StockException if issue
     */
     StockR makeStockReader() throws StockException;

    /**
     * Return an object to access the database for read/write access
     *
     * @return instance of StockReadWriter
     * @throws StockException if issue
     */
    StockReadWriter makeStockReadWriter() throws StockException;

    /**
     * Return an object to access the order processing system
     *
     * @return instance of OrderProcessing
     * @throws OrderException if issue
     */
//    OrderProcessing makeOrderProcessing() throws OrderException;
}
