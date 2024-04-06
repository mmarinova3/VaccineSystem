module com.vaccine.vaccinesystem {

    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;


    opens com.vaccine to javafx.fxml;
    exports com.vaccine.VaccineSystem;
    opens com.vaccine.VaccineSystem to javafx.fxml;
    exports com.vaccine.Controller;
    opens com.vaccine.Controller to javafx.fxml;
}