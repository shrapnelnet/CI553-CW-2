package com.shr4pnel.middleware;

/**
 * Exception throw if there is an error in accessing the stock list
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class StockException extends Exception {
    private static final long serialVersionUID = 1;

    /**
     * <p>Constructor for StockException.</p>
     *
     * @param s a {@link java.lang.String} object
     */
    public StockException(String s) {
        super(s);
    }
}
