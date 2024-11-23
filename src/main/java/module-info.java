module com.shr4pnel {
    requires java.datatransfer;
    requires java.desktop;
    requires java.rmi;
    requires java.sql;
    requires org.apache.logging.log4j;
    opens com.shr4pnel.clients;
    opens com.shr4pnel.logging;
}