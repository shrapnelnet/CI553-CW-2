package com.shr4pnel.clients;

import com.shr4pnel.clients.backDoor.BackDoorController;
import com.shr4pnel.clients.backDoor.BackDoorModel;
import com.shr4pnel.clients.backDoor.BackDoorView;
import com.shr4pnel.clients.cashier.CashierController;
import com.shr4pnel.clients.cashier.CashierModel;
import com.shr4pnel.clients.cashier.CashierView;
import com.shr4pnel.clients.customer.CustomerController;
import com.shr4pnel.clients.customer.CustomerModel;
import com.shr4pnel.clients.customer.CustomerView;
import com.shr4pnel.clients.packing.PackingController;
import com.shr4pnel.clients.packing.PackingModel;
import com.shr4pnel.clients.packing.PackingView;
import com.shr4pnel.logging.Logger;
import com.shr4pnel.middleware.LocalMiddleFactory;
import com.shr4pnel.middleware.MiddleFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 *
 * @author Mike Smith University of Brighton
 * @author Shine University of Brighton
 * @version year-2024
 */

class Main {
    public static void main(String[] args) {
        new Main().begin();
    }

    /**
     * Starts the system (Non-distributed)
     */
    public void begin() {
        Logger.enable(); /* Lots of debug info */
        MiddleFactory mlf = new LocalMiddleFactory();  // Direct access
        startCustomerGUI_MVC(mlf);
        startCashierGUI_MVC(mlf);
        startCashierGUI_MVC(mlf); // you can create multiple clients
        startPackingGUI_MVC(mlf);
        startBackDoorGUI_MVC(mlf);
    }

    /**
     * start the Customer client, -search product
     *
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCustomerGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        CustomerModel model = new CustomerModel(mlf);
        CustomerView view = new CustomerView(window, mlf, pos.width, pos.height);
        CustomerController cont = new CustomerController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model, ---view is observer, model is Observable
        window.setVisible(true);         // start Screen
    }

    /**
     * start the cashier client - customer check stock, buy product
     *
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCashierGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("Cashier Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        CashierModel model = new CashierModel(mlf);
        CashierView view = new CashierView(window, mlf, pos.width, pos.height);
        CashierController cont = new CashierController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model
        window.setVisible(true);         // Make window visible
        model.askForUpdate();            // Initial display
    }

    /**
     * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
     *
     * @param mlf A factory to create objects to access the stock list
     */
    public void startPackingGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        PackingModel model = new PackingModel(mlf);
        PackingView view = new PackingView(window, mlf, pos.width, pos.height);
        PackingController cont = new PackingController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model
        window.setVisible(true);         // Make window visible
    }

    /**
     * start the BackDoor client - store staff to check and update stock
     *
     * @param mlf A factory to create objects to access the stock list
     */
    public void startBackDoorGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();

        window.setTitle("BackDoor Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        BackDoorModel model = new BackDoorModel(mlf);
        BackDoorView view = new BackDoorView(window, mlf, pos.width, pos.height);
        BackDoorController cont = new BackDoorController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model
        window.setVisible(true);         // Make window visible
    }

}
