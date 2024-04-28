package com.vaccine.Controller;

import com.vaccine.Model.Entity.User;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import com.vaccine.VaccineSystem.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ForgetPasswordController {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label infoLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;


    private final UserService userService;

    private static final Logger log = LogManager.getLogger(LoginController.class);
    public ForgetPasswordController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        Session.getInstance();
    }

    @FXML
    protected void onChangePasswordButtonClick(){
        String enteredUsername = usernameField.getText();
        String enteredNewPassword = passwordField.getText();
        String enteredConfirmPassword = confirmPasswordField.getText();

        if (!enteredUsername.isEmpty() && !enteredNewPassword.isEmpty() && !enteredConfirmPassword.isEmpty()) {
            if (userService.checkUsernameExists(enteredUsername)) {
                if(enteredNewPassword.equals(enteredConfirmPassword)) {

                    User user = userService.getById(userService.getIdByUsername(enteredUsername));
                    user.setPassword(enteredNewPassword);
                    userService.update(user, new String[]{user.getUsername(),user.getEmail(),user.getPassword()});

                    infoLabel.setText("Password is changed.");
                }else{
                    infoLabel.setText("Passwords don't match.");
                }
            }else{
                infoLabel.setText("User doesn't exist.");
            }
        } else {
            infoLabel.setText("Please enter a username/password");
        }
    }

    @FXML
    protected void onGoBackButtonClick() throws IOException {
        Main app= new Main();
        app.changeScene("/com/vaccine/fxml/vac-login-view.fxml",747, 438,false);
    }
}
