package com.vaccine.Controller;

import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;


@Component
public class LoginController {


    @FXML
    private Label welcomeText;

    private final UserService userService;

    public LoginController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        Session.getInstance();
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        // Check if the database is working by performing a simple operation
        try {

            System.out.print(userService.getAll());
            System.out.println("Database is working!");
        } catch (Exception e) {
            System.err.println("Error while accessing the database: " + e.getMessage());
        }
    }
}
