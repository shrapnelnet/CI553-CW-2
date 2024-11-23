package com.shr4pnel.middleware;

import com.shr4pnel.catalogue.Basket;

import java.util.List;
import java.util.Map;

/**
 * Defines the interface for accessing the order processing system.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */

public interface OrderProcessing {
    // Used by
    void newOrder(Basket bought)              // Cashier
            throws OrderException;

    int uniqueNumber()                       // Cashier
            throws OrderException;

    Basket getOrderToPack()                   // Packer
            throws OrderException;

    boolean informOrderPacked(int orderNum)   // Packer
            throws OrderException;

    // not being used in this version
    boolean informOrderCollected(int orderNum) // Collection
            throws OrderException;

    // not being used in this version
    Map<String, List<Integer>> getOrderState() // Display
            throws OrderException;
}
