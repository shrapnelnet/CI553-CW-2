package com.shr4pnel.middleware;


import javax.swing.*;

/**
 * Interface for read access to the stock list.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface StockReader {
    /**
     * Returns details about the product in the stock list
     *
     * @param pNum Product nymber
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if issue
     */
    void getDetails(String pNum) throws StockException;

    /**
     * Returns an image of the product in the stock list
     *
     * @param pNum Product nymber
     * @return Image
     * @throws StockException if issue
     */
    ImageIcon getImage(String pNum) throws StockException;
}
