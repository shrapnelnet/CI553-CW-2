package com.shr4pnel.remote;

import com.shr4pnel.catalogue.Product;
import com.shr4pnel.middleware.StockException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.*;

/**
 * Defines the RMI interface for read access to the stock object.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteStockR_I extends Remote {
    boolean exists(String number) throws RemoteException, StockException;

    Product getDetails(String number) throws RemoteException, StockException;

    ImageIcon getImage(String number) throws RemoteException, StockException;
}
