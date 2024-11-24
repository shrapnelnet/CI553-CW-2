package com.shr4pnel.orders;

import com.shr4pnel.catalogue.Basket;
import com.shr4pnel.catalogue.Product;
import com.shr4pnel.middleware.OrderException;
import com.shr4pnel.middleware.OrderProcessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The order processing system.<br>
 * Manages the progression of customer orders, instances of a Basket as they are progressed through
 * the system. These stages are: <br>
 * <B>Waiting to be processed<br>
 * Currently being packed<br>
 * Waiting to be collected<br>
 * </B>
 *
 * @author Mike Smith University of Brighton
 * @version 3.0
 */
public class Order implements OrderProcessing {
  private static final Logger orderLogger = LogManager.getLogger(Order.class);
  private static int theNextNumber = 1; // Start at order 1

  // Active orders in the Catshop system
  private final ArrayList<Folder> folders = new ArrayList<>();

  /**
   * Used to generate debug information
   *
   * @param basket an instance of a basket
   * @return Description of contents
   */
  private String asString(Basket basket) {
    StringBuilder sb = new StringBuilder(1024);
    Formatter fr = new Formatter(sb);
    fr.format("#%d (", basket.getOrderNum());
    for (Product pr : basket) {
      fr.format("%-15.15s: %3d ", pr.getDescription(), pr.getQuantity());
    }
    fr.format(")");
    fr.close();
    return sb.toString();
  }

  /**
   * Generates a unique order number would be good to recycle numbers after 999
   *
   * @return A unique order number
   */
  public synchronized int uniqueNumber() throws OrderException {
    return theNextNumber++;
  }

  /**
   * Add a new order to the order processing system
   *
   * @param bought A new order that is to be processed
   */
  public synchronized void newOrder(Basket bought) throws OrderException {
    orderLogger.trace("Order received");
    folders.add(new Folder(bought));
    for (Folder bws : folders) {
      orderLogger.trace("Order: {}", asString(bws.getBasket()));
    }
  }

  /**
   * Returns an order to pack from the warehouse.
   *
   * @return An order to pack or null if no order
   */
  public synchronized Basket getOrderToPack() throws OrderException {
    orderLogger.debug("Order packing");
    Basket foundWaiting = null;
    for (Folder bws : folders) {
      if (bws.getState() == State.Waiting) {
        foundWaiting = bws.getBasket();
        bws.newState(State.BeingPacked);
        break;
      }
    }
    return foundWaiting;
  }

  /**
   * Informs the order processing system that the order has been packed and the products are now
   * being delivered to the collection desk
   *
   * @param orderNum The order that has been packed
   * @return true Order in system, false no such order
   */
  public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
    orderLogger.debug("Order #{} packed", orderNum);
    for (Folder folder : folders) {
      if (folder.getBasket().getOrderNum() == orderNum && folder.getState() == State.BeingPacked) {
        folder.newState(State.ToBeCollected);
        return true;
      }
    }
    return false;
  }

  /**
   * Informs the order processing system that the order has been collected by the customer
   *
   * @return true If order is in the system, otherwise false
   */
  public synchronized boolean informOrderCollected(int orderNum) throws OrderException {
    orderLogger.debug("Order #{} collected", orderNum);
    for (int i = 0; i < folders.size(); i++) {
      if (folders.get(i).getBasket().getOrderNum() == orderNum
          && folders.get(i).getState() == State.ToBeCollected) {
        folders.remove(i);
        return true;
      }
    }
    return false;
  }

  /**
   * Returns information about all the orders (there order number) in the order processing system
   * This consists of a map with the following keys:
   *
   * <PRE>
   * Key "Waiting"        a list of orders waiting to be processed
   * Key "BeingPacked"    a list of orders that are currently being packed
   * Key "ToBeCollected"  a list of orders that can now be collected
   * Associated with each key is a List&lt;Integer&gt; of order numbers.
   * Note: Each order number will be unique number.
   * </PRE>
   *
   * @return a Map with the keys: "Waiting", "BeingPacked", "ToBeCollected"
   */
  public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
    // DEBUG.trace( "DEBUG: get state of order system" );
    orderLogger.debug("Fetching state of order");
    Map<String, List<Integer>> res = new HashMap<>();

    res.put("Waiting", orderNums(State.Waiting));
    res.put("BeingPacked", orderNums(State.BeingPacked));
    res.put("ToBeCollected", orderNums(State.ToBeCollected));

    return res;
  }

  /**
   * Return the list of order numbers in selected state
   *
   * @param inState The state to find order numbers in
   * @return A list of order numbers
   */
  private List<Integer> orderNumsOldWay(State inState) {
    List<Integer> res = new ArrayList<>();
    for (Folder folder : folders) {
      if (folder.getState() == inState) {
        res.add(folder.getBasket().getOrderNum());
      }
    }
    return res;
  }

  /**
   * Return the list of order numbers in selected state
   *
   * @param inState The state to find order numbers in
   * @return A list of order numbers
   */
  private List<Integer> orderNums(State inState) {
    return folders.stream()
        .filter(folder -> folder.getState() == inState)
        .map(folder -> folder.getBasket().getOrderNum())
        .collect(Collectors.toList());
  }

  private enum State {
    Waiting,
    BeingPacked,
    ToBeCollected
  }

  /** Wraps a Basket and it state into a folder */
  private class Folder {
    private final Basket basket; // For this basket
    private State stateIs; // Order state

    public Folder(Basket anOrder) {
      stateIs = State.Waiting;
      basket = anOrder;
    }

    public State getState() {
      return this.stateIs;
    }

    public Basket getBasket() {
      return this.basket;
    }

    public void newState(State newState) {
      stateIs = newState;
    }
  }
}
