package com.vaccine.Controller;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.User;
import com.vaccine.Service.PersonService;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PersonPaneController {

    @FXML
    private CheckBox isVaccineCheckBox;
    @FXML
    private Label letterLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label periodLabel;
    @FXML
    private Pane personPane;
    @FXML
    private Label vaccineLabel;

    private final UserService userService;
    private final PersonService personService;

    private final Session session = Session.getInstance();

    private static final Logger log = LogManager.getLogger(LoginController.class);
    public PersonPaneController() {
        this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.personService = PersonService.getInstance(Connection.getEntityManager(),Session.getInstance());
    }

    @FXML
    public void initialize() {

    }

    public void setPersonPaneData(Person p) {
        if (p != null && p.getName() != null && !p.getName().isEmpty()) {
            String firstTwoChars = p.getName().substring(0, Math.min(p.getName().length(), 2));
            letterLabel.setText(firstTwoChars.toUpperCase());
            nameLabel.setText(p.getName());
        } else {
            letterLabel.setText("");
            nameLabel.setText("");
        }
    }

}
