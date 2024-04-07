module com.vaccine.vaccinesystem {

    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;
    requires spring.boot;
    requires persistence.api;
    requires spring.beans;
    requires spring.context;
    requires spring.tx;

    exports  com.vaccine.Model.Entity;
    opens com.vaccine.Model.Entity to org.hibernate.orm.core;
    opens com.vaccine to javafx.fxml;
    exports com.vaccine.VaccineSystem;
    opens com.vaccine.VaccineSystem to javafx.fxml;
    exports com.vaccine.Controller;
    opens com.vaccine.Controller to javafx.fxml,org.hibernate.orm.core;
    exports com.vaccine.Service;
    opens com.vaccine.Service to javafx.fxml,org.hibernate.orm.core;
}