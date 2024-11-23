package com.shr4pnel.middleware;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */

import com.shr4pnel.catalogue.Product;
import com.shr4pnel.logging.Logger;
import com.shr4pnel.remote.RemoteStockR_I;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Setup connection to the middle tier
 */

public class F_StockR implements StockReader {
    private RemoteStockR_I aR_StockR = null;
    private String theStockURL = null;

    public F_StockR(String url) {
        Logger.trace("F_StockR: %s", url);
        theStockURL = url;
    }

    private void connect() throws StockException {
        try                                             // Setup
        {                                               //  connection
            aR_StockR =                                   //  Connect to
                    (RemoteStockR_I) Naming.lookup(theStockURL);// Stub returned
        } catch (Exception e)                           // Failure to
        {                                               //  attach to the
            aR_StockR = null;
            throw new StockException("Com: " +
                    e.getMessage());  //  object

        }
    }

    /**
     * Checks if the product exits in the stock list
     * @return true if exists otherwise false
     */

    public synchronized boolean exists(String number)
            throws StockException {
        Logger.trace("F_StockR:exists()");
        try {
            if (aR_StockR == null) connect();
            return aR_StockR.exists(number);
        } catch (RemoteException e) {
            aR_StockR = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */

    public synchronized Product getDetails(String number)
            throws StockException {
        Logger.trace("F_StockR:getDetails()");
        try {
            if (aR_StockR == null) connect();
            return aR_StockR.getDetails(number);
        } catch (RemoteException e) {
            aR_StockR = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }


    public synchronized ImageIcon getImage(String number)
            throws StockException {
        Logger.trace("F_StockR:getImage()");
        try {
            if (aR_StockR == null) connect();
            return aR_StockR.getImage(number);
        } catch (RemoteException e) {
            aR_StockR = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

}
