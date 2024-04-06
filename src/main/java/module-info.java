module com.vaccine.vaccinesystem {

    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;


    opens com.vaccine.vaccinesystem to javafx.fxml;
    exports com.vaccine.vaccinesystem;
}