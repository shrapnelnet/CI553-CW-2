package com.shr4pnel.clients.customer;

import com.shr4pnel.catalogue.Basket;
import com.shr4pnel.catalogue.Product;
import com.shr4pnel.middleware.MiddleFactory;
import com.shr4pnel.middleware.OrderProcessing;
import com.shr4pnel.middleware.StockException;
import com.shr4pnel.middleware.StockReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
    private static final Logger customerModelLogger = LogManager.getLogger(CustomerModel.class);

    private final Product theProduct = null;          // Current product
    private Basket theBasket = null;          // Bought items

    private String pn = "";                    // Product being processed

    private StockReader theStock = null;
    private final OrderProcessing theOrder = null;
    private ImageIcon thePic = null;

    /*
     * Construct the model of the Customer
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try                                          //
        {
            theStock = mf.makeStockReader();           // Database access
        } catch (Exception e) {
            customerModelLogger.error("CustomerModel.constructor, Database not created?", e);
        }
        theBasket = makeBasket();                    // Initial Basket
    }

    /**
     * return the Basket of products
     *
     * @return the basket of products
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     *
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        theBasket.clear();                          // Clear s. list
        String theAction = "";
        pn = productNum.trim();                    // Product no.
        int amount = 1;                         //  & quantity
        try {
            if (theStock.exists(pn))              // Stock Exists?
            {                                         // T
                Product pr = theStock.getDetails(pn); //  Product
                if (pr.getQuantity() >= amount)       //  In stock?
                {
                    theAction =                           //   Display
                            String.format("%s : %7.2f (%2d) ", //
                                    pr.getDescription(),              //    description
                                    pr.getPrice(),                    //    price
                                    pr.getQuantity());               //    quantity
                    pr.setQuantity(amount);             //   Require 1
                    theBasket.add(pr);                  //   Add to basket
                    thePic = theStock.getImage(pn);     //    product
                } else {                                //  F
                    theAction =                           //   Inform
                            pr.getDescription() +               //    product not
                                    " not in stock";                   //    in stock
                }
            } else {                                  // F
                theAction =                             //  Inform Unknown
                        "Unknown product number " + pn;       //  product number
            }
        } catch (StockException e) {
            customerModelLogger.error("CustomerClient.doCheck()", e);
        }
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Clear the products from the basket
     */
    public void doClear() {
        String theAction = "";
        theBasket.clear();                        // Clear s. list
        theAction = "Enter Product Number";       // Set display
        thePic = null;                            // No picture
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Return a picture of the product
     *
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return thePic;
    }

    /**
     * ask for update of view callled at start
     */
    private void askForUpdate() {
        setChanged();
        notifyObservers("START only"); // Notify
    }

    /**
     * Make a new Basket
     *
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}

