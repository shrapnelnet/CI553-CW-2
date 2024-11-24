module CI {
    exports com.shr4pnel.catalogue;
    exports com.shr4pnel.clients;
    exports com.shr4pnel.clients.backDoor;
    exports com.shr4pnel.clients.cashier;
    exports com.shr4pnel.clients.customer;
    exports com.shr4pnel.clients.packing;
    exports com.shr4pnel.db;
    exports com.shr4pnel.middleware;
    exports com.shr4pnel.orders;
    exports com.shr4pnel.remote;

    requires org.apache.logging.log4j.core;
    requires java.sql;
    requires org.apache.derby.client;
    requires org.apache.derby.tools;
    requires java.desktop;
    requires java.rmi;

    opens com.shr4pnel.catalogue;
    opens com.shr4pnel.db;
}