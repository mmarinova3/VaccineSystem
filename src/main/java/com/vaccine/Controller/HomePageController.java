package com.vaccine.Controller;

import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Service.PersonService;
import com.vaccine.Service.PersonVaccineService;
import com.vaccine.Service.VaccineService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.RelationshipEnum;
import com.vaccine.Utils.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import com.vaccine.Utils.Session;
import com.vaccine.Model.Entity.Person;
import com.vaccine.VaccineSystem.Main;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class HomePageController  {
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private VBox personVBox;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private BorderPane homePane;
    @FXML
    private Button addVaccineButton;
    @FXML
    private Label helloLabel;

    private final PersonService personService;
    private final VaccineService vaccineService;
    private final PersonVaccineService personVaccineService;
    private final Session session = Session.getInstance();

    private static final Logger log = LogManager.getLogger(LoginController.class);
     public  HomePageController(){
     this.personService = PersonService.getInstance(Connection.getEntityManager());
     this.personVaccineService = PersonVaccineService.getInstance(Connection.getEntityManager());
     this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
     }

    @FXML
    public void initialize()  {
        uncheckVaccine();
        showVaccineNotifications();
        showCalendar();


        List<Person> people = personService.getPersonsList(session.getUser().getId());
        for (Person p : people) {
            if (RelationshipEnum.ME.toString().equals(p.getRelationWithUser().toUpperCase())) {
                String[] names = p.getName().split("\\s+");
                String firstName = names[0];
                helloLabel.setText("Hello, " + firstName + "!");
               break;
            } else {
                helloLabel.setText("Welcome!");
            }
        }

        if(!Objects.equals(session.getUser().getUsername(), "admin")){
          addVaccineButton.setVisible(false);
        }
    }

    @FXML
    public void LogOut() throws IOException {
        Main app= new Main();
        app.changeScene("/com/vaccine/fxml/vac-login-view.fxml",747, 438,false);
    }


    @FXML
    private void openUserEditScene() {
        rightPane.setVisible(true);
        SceneNavigator.navigateTo("/com/vaccine/fxml/edit-user.fxml", mainAnchor);
    }

    @FXML
    private void openVaccineManagement() {
        rightPane.setVisible(false);
        SceneNavigator.navigateTo("/com/vaccine/fxml/vaccine-management.fxml", mainAnchor);
    }

    @FXML
    private void openVaccineInfo() {
        rightPane.setVisible(false);
        SceneNavigator.navigateTo("/com/vaccine/fxml/vac-info-user.fxml", mainAnchor);
    }

    @FXML
    private void openVaccinePersonView() {
        rightPane.setVisible(true);
        SceneNavigator.navigateTo("/com/vaccine/fxml/vaccine-person.fxml", mainAnchor);
    }

    @FXML
    private void openCalendarView() {
        rightPane.setVisible(false);
        SceneNavigator.navigateTo("/com/vaccine/fxml/calendar-view.fxml", mainAnchor);
    }

    @FXML
    private void openHomeView() throws IOException {
         Main app = new Main();
        app.changeScene("/com/vaccine/fxml/vac-main.fxml",1000,600,true);
    }

    private List<PersonVaccine> checkNotificatonList() {
        List<Person> personList = personService.getPersonsList(session.getUser().getId());
        List<PersonVaccine> personVaccineToDo = new ArrayList<>();

        for (Person person : personList) {
            Period age = Period.between(Date.valueOf(String.valueOf(person.getDateOfBirth())).toLocalDate(), LocalDate.now());
            int personAge = age.getYears();
            List<Vaccine> unmadeVaccines = vaccineService.getUnassignedVaccinesForPerson(person.getId());
            if(!unmadeVaccines.isEmpty()){
                for (Vaccine vaccine : unmadeVaccines) {
                    if (vaccine.getAgeOfUse() <= personAge) {
                        PersonVaccine newPV = createNewPersonVaccine(personService.getById(person.getId()),vaccineService.getById(vaccine.getId()));
                        personVaccineToDo.add(newPV);
                    }
                }
            }
        }
        log.info("Size hole list: " + personVaccineToDo.size());
        return personVaccineToDo;
    }

    private void uncheckVaccine(){
        List<Person> personList = personService.getPersonsList(session.getUser().getId());
        for (Person person : personList) {
            List<Vaccine> madeVaccines = vaccineService.getAssignedVaccinesForPerson(person.getId());
            if(!madeVaccines.isEmpty()) {
                for (Vaccine vaccine : madeVaccines) {
                    if (!vaccine.isOneTime()) {
                        PersonVaccine pv = personVaccineService.getByPersonAndVaccineId(person.getId(),vaccine.getId());
                        long daysBetween = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), pv.getVaccinationDate().toLocalDate()));
                        if (daysBetween > vaccine.getEffectivenessPeriod()) {
                            pv.setMade(false);
                            personVaccineService.delete(pv);
                        }
                    }
                }
            }
        }
    }

    private PersonVaccine createNewPersonVaccine(Person person, Vaccine vaccine) {
        PersonVaccine newPV = new PersonVaccine();
        newPV.setPerson(person);
        newPV.setVaccine(vaccine);
        newPV.setMade(false);
        newPV.setVaccinationDate(Date.valueOf(LocalDate.now()));
        return newPV;
    }
    @FXML
    private void showVaccineNotifications(){
        try {
            List<PersonVaccine> pvList = checkNotificatonList();
            if (!pvList.isEmpty()) {
                for (PersonVaccine pv : pvList) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/vaccine/fxml/person-pane.fxml"));
                    Pane anchorPane = fxmlLoader.load();
                    PersonPaneController personPaneController = fxmlLoader.getController();

                    personPaneController.setPersonPaneData(
                            pv.getPerson(),
                            pv.getVaccine(),
                            pv.getVaccinationDate().toString()
                    );
                    personVBox.getChildren().add(anchorPane);
                }

            } else {
                log.info("No notifications");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showCalendar() {
         SceneNavigator.navigateTo("/com/vaccine/fxml/small-calendar-view.fxml",calendarPane);
    }

    @FXML
    private void minimizeApp() {
        Stage stage = (Stage) homePane.getScene().getWindow();
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
