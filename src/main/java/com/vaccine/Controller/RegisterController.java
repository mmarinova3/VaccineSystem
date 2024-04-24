package com.vaccine.Controller;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.User;
import com.vaccine.Service.PersonService;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import com.vaccine.VaccineSystem.Main;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Date;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker birthday;
    @FXML
    private Label infoLabel;

    private final UserService userService;
    private final PersonService personService;

    public RegisterController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.personService = PersonService.getInstance(Connection.getEntityManager(),Session.getInstance());
    }

    @FXML
    protected void onGoRegisterButtonClick() {

        Person newPerson = new Person(1332,new User(1,"a","b","c"), "enteredName", Date.valueOf("2002-12-12"));
        personService.save(newPerson);
/*
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();
        String enteredEmail = emailField.getText();
        String enteredName = nameField.getText();
        Date enteredBirthday = birthday.getValue() != null ? Date.valueOf(birthday.getValue()) : null;

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || enteredEmail.isEmpty() || enteredName.isEmpty()){
            infoLabel.setText("Please fill all fields.");
            return;
        }
        else if (userService.checkUsernameExists(enteredUsername)){
            infoLabel.setText("Username already taken.");
            return;
        }else if (userService.checkEmailExists(enteredEmail)){
            infoLabel.setText("Email already used.");
            return;
        }

        User newUser = new User(enteredUsername, enteredPassword, enteredEmail);
        userService.save(newUser);

        Person newPerson = new Person(1,newUser, enteredName, enteredBirthday);

        System.out.print("\nName:"+enteredName+"\nDate:"+enteredBirthday+"\nStatus:"+ newPerson.getRelationWithUser());
        personService.save(newPerson);
        System.out.print("\n"+newPerson.getId());

 */
    }


    @FXML
    protected void onGoBackButtonClick() throws IOException {
        Main app= new Main();
        app.changeScene("/com/vaccine/fxml/vac-login-view.fxml",747, 438,false);


    }
}
