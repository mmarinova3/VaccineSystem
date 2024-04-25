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
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPersonController {
    @FXML
    private DatePicker birthday;

    @FXML
    private AnchorPane editPersonAnchor;

    @FXML
    private Label infoLabel;

    @FXML
    private ComboBox<String> personComboBox;

    @FXML
    private ComboBox<RelationshipEnum> relationComboBox;

    @FXML
    private TextField nameField;
    private boolean nameEnabled = false;
    private boolean birthdayEnabled = false;
    private boolean relationEnabled = false;


    private final PersonService personService;

    private final Session session = Session.getInstance();

    private static final Logger log = LogManager.getLogger(LoginController.class);
    public EditPersonController() {
        this.personService = PersonService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }


    private boolean isPersonAddFXML = true;

    private void determineFXMLFile() {
        if (editPersonAnchor.getScene() != null) {
            String fxmlFile = editPersonAnchor.getScene().getRoot().getId();
            if (fxmlFile != null) {
                System.out.println("FXML file: " + fxmlFile);
            } else {
                System.out.println("FXML file not found");
            }
        } else {
            System.out.println("AnchorPane is not attached to a scene");
        }
    }


    @FXML
    public void initialize() {
        determineFXMLFile();

        if (isPersonAddFXML) {
            initializeForPersonAddFXML();
        } else {
            initializeForEditUserFXML();
        }
    }


    public void initializeForPersonAddFXML() {
        populateRelationComboBox();
    }

    public void initializeForEditUserFXML() {
        populatePersonComboBox();
        populateRelationComboBox();
        disableFields();

        personComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields();
            }
        });

    }


    private void populatePersonComboBox() {
        User currentUser = session.getUser();
        int userId = currentUser.getId();

        List<Person> persons = personService.getPersonsList(userId);

        ObservableList<String> personNames = FXCollections.observableArrayList();
        Map<String, Integer> personIdMap = new HashMap<>();

        for (Person person : persons) {
            String name = person.getName();
            int id = person.getId();
            personNames.add(name);
            personIdMap.put(name, id);
        }

        personComboBox.setItems(personNames);
        personComboBox.setUserData(personIdMap);
    }

    @SuppressWarnings("unchecked")
    private int getSelectedPersonId() {
        Map<String, Integer> personIdMap = (Map<String, Integer>) personComboBox.getUserData();
        String selectedName = personComboBox.getValue();
        return personIdMap.get(selectedName);
    }


    private void populateRelationComboBox() {
        ObservableList<RelationshipEnum> relationshipEnums = FXCollections.observableArrayList(RelationshipEnum.values());
        relationComboBox.setItems(relationshipEnums);
    }

    private void disableFields() {
        nameField.setDisable(true);
        relationComboBox.setDisable(true);
        birthday.setDisable(true);
    }

    @FXML
    private void enableDateClicked() {
         birthdayEnabled = !birthdayEnabled;
         birthday.setDisable(!birthdayEnabled);
    }

    @FXML
    private void  enableNameFieldClicked() {
        nameEnabled = !nameEnabled;
        nameField.setDisable(!nameEnabled);
    }

    @FXML
    private void enableRelationBoxClicked() {
        relationEnabled = !relationEnabled;
        relationComboBox.setDisable(!relationEnabled);
    }

    @FXML
    private void addButtonClicked () {
        Person person = new Person();

        String newName = nameField.getText();
        java.sql.Date newDateOfBirth = Date.valueOf(birthday.getValue());
        String relation = String.valueOf(relationComboBox.getValue());

        if (newName == null || relation == null){
            infoLabel.setText("Please, fill all the fields");
            return;
        }

        if (relation.equals("ME") || relation.equals("HUSBAND")) {
            List<Person> existingPersons = personService.getPersonsList(session.getUser().getId());
            for (Person existingPerson : existingPersons) {
                if (existingPerson.getRelationWithUser() != null && existingPerson.getRelationWithUser().equals(relation)) {
                    infoLabel.setText("There is already a person with the relation: " + relation);
                    return;
                }
            }
        }
        User currentUser = session.getUser();

        person.setName(newName);
        person.setDateOfBirth(newDateOfBirth);
        person.setRelationWithUser(relation);
        person.setUser(currentUser);

        personService.save(person);
        disableFields();
        infoLabel.setText("Person added successfully");
    }

    @FXML
    private void saveChangesClicked() {
        if (getSelectedPersonId() == 0){
            infoLabel.setText("Please, select person");
            return;
        }

        Person person = personService.getById(getSelectedPersonId());

        if (nameEnabled) {
            String newName = nameField.getText();
            person.setName(newName);
        }

        if (birthdayEnabled) {
            java.sql.Date newDateOfBirth = Date.valueOf(birthday.getValue());
            person.setDateOfBirth(newDateOfBirth);
        }

        if (relationEnabled) {
            String relation = String.valueOf(relationComboBox.getValue());
            if (relation.equals("ME") || relation.equals("HUSBAND")) {
                List<Person> existingPersons = personService.getPersonsList(session.getUser().getId());
                for (Person existingPerson : existingPersons) {
                    if (existingPerson.getRelationWithUser() != null && existingPerson.getRelationWithUser().equals(relation)) {
                        infoLabel.setText("There is already a person with the relation: " + relation);
                        return;
                    }
                }
            }
            person.setRelationWithUser(relation);
        }

        personService.save(person);
        disableFields();
        infoLabel.setText("Changes saved successfully");

    }

    @FXML
    private void deleteButtonClicked() {
        if (getSelectedPersonId() == 0) {
            infoLabel.setText("Please, select a person");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Person");
        alert.setContentText("Are you sure you want to delete this person?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Person person = personService.getById(getSelectedPersonId());
                personService.delete(person);
                disableFields();
                infoLabel.setText("Person deleted successfully");
            } else {
                infoLabel.setText("Deletion canceled");
            }
        });
    }



    private void fillFields(){
        Person person = personService.getById(getSelectedPersonId());

         log.info(person.getName());


        nameField.setText(person.getName());
        if(person.getRelationWithUser()!=null) {
            relationComboBox.setValue(RelationshipEnum.valueOf(person.getRelationWithUser()));
        } else {
            relationComboBox.setValue(null);
        }

        java.sql.Date dateOfBirth = (java.sql.Date) person.getDateOfBirth();
        if (dateOfBirth != null) {
            LocalDate localDate = dateOfBirth.toLocalDate();
            birthday.setValue(localDate);
        } else {
            birthday.setValue(null);
        }
    }
    @FXML
    private void openUserManagement() {
        SceneNavigator.navigateTo("/com/vaccine/fxml/edit-user.fxml", editPersonAnchor);
    }
}
