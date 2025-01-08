/**
 * Main module of project
 * @author <a href="https://github.com/shrapnelnet">shrapnelnet</a>
 * @since v0.1.0
 * @uses com.shr4pnel.db
 * @uses com.shr4pnel.web
 */
module CI {
    requires com.google.common;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.web;
    requires org.apache.derby.tools;
    requires java.sql;
    requires org.apache.commons.csv;
    requires io.swagger.v3.oas.annotations;

    exports com.shr4pnel.web;
    exports com.shr4pnel.db;
}
