module CI {
    exports com.shr4pnel.catalogue;
    exports com.shr4pnel.clients;
    exports com.shr4pnel.db;
    exports com.shr4pnel.middleware;
    exports com.shr4pnel.web;

    requires org.apache.logging.log4j.core;
    requires java.sql;
    requires org.apache.derby.client;
    requires org.apache.derby.tools;
    requires java.desktop;
    requires java.rmi;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires org.apache.commons.csv;
    requires com.google.gson;
    requires spring.core;
    requires com.google.common;

    opens com.shr4pnel.catalogue;
    opens com.shr4pnel.db;
    opens com.shr4pnel.web;

}
