package com.vaccine.Controller;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.User;
import com.vaccine.Service.PersonService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.RelationshipEnum;
import com.vaccine.Utils.SceneNavigator;
import com.vaccine.Utils.Session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Date;
import java.util.List;

public class AddPersonController {

    @FXML
    private DatePicker birthday;
    @FXML
    private AnchorPane editPersonAnchor;
    @FXML
    private Label infoLabel;
    @FXML
    private ComboBox<RelationshipEnum> relationComboBox;
    @FXML
    private TextField nameField;
    private final PersonService personService;

    private final Session session = Session.getInstance();
    public AddPersonController() {
        this.personService = PersonService.getInstance(Connection.getEntityManager());
    }
    @FXML
    public void initialize() {
        populateRelationComboBox();
    }

    private void populateRelationComboBox() {
        ObservableList<RelationshipEnum> relationshipEnums = FXCollections.observableArrayList(RelationshipEnum.values());
        relationComboBox.setItems(relationshipEnums);
    }

    @FXML
    private void addButtonClicked () {
        User currentUser = session.getUser();

        Person person = new Person();

        String newName = nameField.getText();
        java.sql.Date newDateOfBirth = Date.valueOf(birthday.getValue());
        String relation = String.valueOf(relationComboBox.getValue());

        if (newName == null || relation == null){
            infoLabel.setText("Please, fill all the fields");
            return;
        }

        if (relation.equals("ME") || relation.equals("PARTNER")) {
            List<Person> existingPersons = personService.getPersonsList(session.getUser().getId());
            for (Person existingPerson : existingPersons) {
                if (existingPerson.getRelationWithUser() != null && existingPerson.getRelationWithUser().equals(relation)) {
                    infoLabel.setText("There is already a person with the relation: " + relation);
                    return;
                }
            }
        }


        person.setName(newName);
        person.setDateOfBirth(newDateOfBirth);
        person.setRelationWithUser(relation);
        person.setUser(currentUser);

        personService.save(person);

        infoLabel.setText("Person added successfully");
    }

    @FXML
    private void openUserManagement() {
        SceneNavigator.navigateTo("/com/vaccine/fxml/edit-user.fxml", editPersonAnchor);
    }
}


