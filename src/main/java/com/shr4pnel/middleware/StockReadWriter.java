package com.shr4pnel.middleware;

import com.shr4pnel.catalogue.Product;

/**
 * Interface for read/write access to the stock list.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface StockReadWriter extends StockReader {
    /**
     * Adds stock (Restocks) to store.
     *
     * @param pNum Product number
     * @param amount Quantity of product
     * @throws com.shr4pnel.middleware.StockException if issue
     */
    void addStock(String pNum, int amount) throws StockException;
}
