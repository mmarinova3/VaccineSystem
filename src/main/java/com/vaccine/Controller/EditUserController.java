package com.vaccine.Controller;

import com.vaccine.Model.Entity.User;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.SceneNavigator;
import com.vaccine.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class EditUserController {

    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private TextField emailField;
    @FXML
    private Label infoLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    private boolean emailEnabled = false;
    private boolean passwordEnabled = false;
    private boolean usernameEnabled = false;

    private final UserService userService;

    private final Session session = Session.getInstance();

    public EditUserController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }


    @FXML
    public void initialize() {
        User currentUser = session.getUser();

        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        passwordField.setText(currentUser.getPassword());
        confirmPasswordField.setText(currentUser.getPassword());

        disableFields();
    }

    private void disableFields() {
        emailField.setDisable(true);
        passwordField.setDisable(true);
        confirmPasswordField.setDisable(true);
        usernameField.setDisable(true);
    }

    @FXML
    private void enableEmailFieldClicked() {
        emailEnabled = !emailEnabled;
        emailField.setDisable(!emailEnabled);
    }

    @FXML
    private void enablePasswordFieldClicked() {
        passwordEnabled = !passwordEnabled;
        passwordField.setDisable(!passwordEnabled);
        confirmPasswordField.setDisable(!passwordEnabled);
    }

    @FXML
    private void enableUsernameFieldClicked() {
        usernameEnabled = !usernameEnabled;
        usernameField.setDisable(!usernameEnabled);
    }

    @FXML
    private void saveChangesClicked() {
        User currentUser = session.getUser();

        if (emailEnabled) {
            String newEmail = emailField.getText();
            if (userService.checkUsernameExists(newEmail)){
                infoLabel.setText("Email already used.");
                return;
            }
            currentUser.setEmail(newEmail);
        }

        if (passwordEnabled) {
            String newPassword = passwordField.getText();
            String confirmedPassword = confirmPasswordField.getText();

            if (newPassword.equals(confirmedPassword)) {
                currentUser.setPassword(newPassword);
            } else {
                infoLabel.setText("Passwords don't match");
                return;
            }
        }

        if (usernameEnabled) {
            String newUsername = usernameField.getText();
            if (userService.checkUsernameExists(newUsername)) {
                infoLabel.setText("Username already taken.");
                return;
            }
            currentUser.setUsername(newUsername);
        }

        userService.save(currentUser);
        disableFields();
        infoLabel.setText("Changes saved successfully");
    }

    @FXML
    private void openPersonManagement() {
        SceneNavigator.navigateTo("/com/vaccine/fxml/person-management.fxml", mainAnchor);
    }

    @FXML
    private void openAddPerson() {
        SceneNavigator.navigateTo("/com/vaccine/fxml/person-add.fxml", mainAnchor);
    }
}
