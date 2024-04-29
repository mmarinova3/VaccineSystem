package com.vaccine.Controller;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Model.Entity.User;
import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Service.PersonService;
import com.vaccine.Service.PersonVaccineService;
import com.vaccine.Service.VaccineService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;

import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonVaccineController {

    @FXML
    private AnchorPane listAnchorPane;
    @FXML
    private ComboBox<String> personComboBox;
    @FXML
    private ComboBox<String> filtersComboBox;

    private final PersonService personService;
    private final VaccineService vaccineService;
    private final PersonVaccineService personVaccineService;
    private final Session session = Session.getInstance();
    private static final Logger log = LogManager.getLogger(LoginController.class);
    public PersonVaccineController() {
        this.personVaccineService = PersonVaccineService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.personService = PersonService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
    }

    @FXML
    public void initialize() {
        populatePersonComboBox();

        ObservableList<String> yesNoOptions = FXCollections.observableArrayList("All", "Made", "UnMade");
        filtersComboBox.setItems(yesNoOptions);

        setCheckListView("All");
        personComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateVaccineListView());
        filtersComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateVaccineListView());

        updateVaccineListView();
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
        if (selectedName != null && personIdMap.containsKey(selectedName)) {
            return personIdMap.get(selectedName);
        } else {
            log.error("Selected person's name not found in the map: " + selectedName);
            return -1;
        }
    }


    private void updateVaccineListView() {
        setCheckListView(filtersComboBox.getValue());
    }

    private void setCheckListView(String filter) {
        int selectedPersonId = getSelectedPersonId();

        if (selectedPersonId == -1) {
            log.error("No person selected.");
            return;
        }

        List<Vaccine> filteredVaccines = getFiltered(filter);

        ObservableList<String> vaccineNames = FXCollections.observableArrayList();
        for (Vaccine vaccine : filteredVaccines) {
            vaccineNames.add(vaccine.getVaccineName());
        }
        vaccineNames.sort(String::compareTo);

        CheckListView<String> checkListView = new CheckListView<>(vaccineNames);

        List<PersonVaccine> personVaccines = personVaccineService.getByPersonId(selectedPersonId);
        for (PersonVaccine personVaccine : personVaccines) {
            Vaccine vaccine = personVaccine.getVaccine();
            if (vaccineNames.contains(vaccine.getVaccineName())) {
                checkListView.getCheckModel().check(vaccine.getVaccineName());
            }
        }

        checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (String newValue : change.getAddedSubList()) {
                        int vaccineId = findVaccineIdByName(filteredVaccines, newValue);
                        addPersonVaccine(vaccineId, selectedPersonId);
                    }
                } else if (change.wasRemoved()) {
                    for (String removedValue : change.getRemoved()) {
                        int vaccineId = findVaccineIdByName(filteredVaccines, removedValue);
                        deletePersonVaccine(vaccineId, selectedPersonId);
                    }
                }
            }
        });

        checkListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = checkListView.getSelectionModel().getSelectedItem();
                int vacId =findVaccineIdByName(filteredVaccines,selectedItem);
                openDetailsPane(vaccineService.getById(vacId));
                log.info(selectedItem);
            }
        });

        listAnchorPane.getChildren().clear();
        AnchorPane.setTopAnchor(checkListView, 0.0);
        AnchorPane.setBottomAnchor(checkListView, 0.0);
        AnchorPane.setLeftAnchor(checkListView, 0.0);
        AnchorPane.setRightAnchor(checkListView, 0.0);
        listAnchorPane.getChildren().add(checkListView);
    }

    private List<Vaccine> getFiltered(String filter) {
        int selectedPersonId = getSelectedPersonId();
        List<Vaccine> allVaccines = vaccineService.getAll();
        List<PersonVaccine> personVaccines = personVaccineService.getByPersonId(selectedPersonId);

        List<Vaccine> filtered = new ArrayList<>();

        if ("Made".equals(filter)) {
            for (Vaccine vaccine : allVaccines) {
                for (PersonVaccine personVaccine : personVaccines) {
                    if (personVaccine.getVaccine().getVaccineName().equals(vaccine.getVaccineName())) {
                        filtered.add(vaccine);
                        break;
                    }
                }
            }
        } else if ("UnMade".equals(filter)) {
            filtered = vaccineService.getUnassignedVaccinesForPerson(selectedPersonId);
        } else {
            filtered.addAll(allVaccines);
        }

        return filtered;
    }

    private void addPersonVaccine(int vaccineId, int personId) {
        Vaccine vaccine = vaccineService.getById(vaccineId);
        if (vaccine != null) {
            PersonVaccine personVaccine = new PersonVaccine();
            personVaccine.setVaccine(vaccine);
            personVaccine.setPerson(personService.getById(personId));
            personVaccine.setMade(true);
            personVaccine.setVaccinationDate(Date.valueOf(LocalDate.now()));
            personVaccineService.save(personVaccine);
        } else {
            log.error("Vaccine not found: " + vaccineId);
        }
    }

    private void deletePersonVaccine(int vaccineId, int personId) {
        Vaccine vaccine = vaccineService.getById(vaccineId);
        if (vaccine != null) {
            PersonVaccine personVaccine = personVaccineService.getByPersonAndVaccineId(personId, vaccineId);
            if (personVaccine != null) {
                personVaccineService.delete(personVaccine);
            } else {
                log.error("Person vaccine not found for person ID " + personId + " and vaccine ID " + vaccine.getVaccineName());
            }
        } else {
            log.error("Vaccine not found: " + vaccineId);
        }
    }


    private int findVaccineIdByName(List<Vaccine> vaccines, String name) {
        for (Vaccine vaccine : vaccines) {
            if (vaccine.getVaccineName().equals(name)) {
                return vaccine.getId();
            }
        }
        return -1;
    }

    private void openDetailsPane(Vaccine vaccine) {
        PersonVaccine personVaccine = personVaccineService.getByPersonAndVaccineId(getSelectedPersonId(), vaccine.getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vaccine/fxml/details-PV-Pane.fxml"));
            Parent root = loader.load();

           PersonVaccineDetailsController controller = loader.getController();

            controller.setPersonVaccine(personVaccine);

            Stage stage = new Stage();
            stage.setTitle(personVaccine.getPerson().getName()+ "'s "+ personVaccine.getVaccine().getVaccineName() +" details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            log.error("Error loading details pane: " + e.getMessage());
        }
    }

    private void notifyUser(Person person, Vaccine vaccine, PersonVaccine pv){
        if (pv.isMade()){
            long daysBetween = ChronoUnit.DAYS.between(pv.getVaccinationDate().toLocalDate(),  LocalDate.now());

            if (!vaccine.isOneTime()|| daysBetween > vaccine.getEffectivenessPeriod()){
                pv.setMade(false);
                return;
            }

            Period age = Period.between(Date.valueOf(String.valueOf(person.getDateOfBirth())).toLocalDate(), LocalDate.now());
            int personAge = age.getYears();

            if (vaccine.getAgeOfUse() >= personAge ||!pv.isMade()){
               //notify
            }

        }


    }


}
