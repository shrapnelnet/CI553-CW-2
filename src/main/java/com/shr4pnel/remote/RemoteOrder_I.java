package com.shr4pnel.remote;

import com.shr4pnel.catalogue.Basket;
import com.shr4pnel.middleware.OrderException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Defines the RMI interface for the Order object.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteOrder_I extends Remote {
    void newOrder(Basket order) throws RemoteException, OrderException;

    int uniqueNumber() throws RemoteException, OrderException;

    Basket getOrderToPack() throws RemoteException, OrderException;

    boolean informOrderPacked(int orderNum) throws RemoteException, OrderException;

    boolean informOrderCollected(int orderNum) throws RemoteException, OrderException;

    Map<String, List<Integer>> getOrderState() throws RemoteException, OrderException;
}
