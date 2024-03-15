module com.vaccine.vaccinesystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.vaccine.vaccinesystem to javafx.fxml;
    exports com.vaccine.vaccinesystem;
}