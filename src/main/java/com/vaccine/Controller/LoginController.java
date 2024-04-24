package com.vaccine.Controller;

import com.vaccine.Model.DAO.PersonDAO;
import com.vaccine.Model.Entity.User;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.SceneNavigator;
import com.vaccine.Utils.Session;
import com.vaccine.VaccineSystem.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class LoginController {


    @FXML
    private Label infoLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane loginAnchor;

    private final UserService userService;

    private static final Logger log = LogManager.getLogger(LoginController.class);
    public LoginController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        Session.getInstance();
    }


    @FXML
    protected void onLoginButtonClick() throws IOException {
        Main app = new Main();

        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {
            User currentUser = userService.validateLogin(enteredUsername, enteredPassword);

            if (currentUser != null) {
                infoLabel.setText("Successful");
                app.changeScene("/com/vaccine/fxml/vac-main.fxml",1000,600,true);
            } else {
                infoLabel.setText("Invalid username/password");
            }
        } else {
            infoLabel.setText("Please enter a username/password");
        }

    }

    @FXML
    protected void onRegisterButtonClick(){
        SceneNavigator.navigateTo("/com/vaccine/fxml/vac-register.view.fxml", loginAnchor);

    }

    @FXML
    protected void onForgetPasswordButtonClick(){
        SceneNavigator.navigateTo("/com/vaccine/fxml/vac-change-password.fxml", loginAnchor);

    }
    @FXML
    private void minimizeApp() {
        Stage stage = (Stage) loginAnchor.getScene().getWindow();
        if (stage != null) {
            stage.setIconified(true);
        }
    }


    @FXML
    private void closeApp() {
        log.info("Exit app...");
        Platform.exit();
    }
}
