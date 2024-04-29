package com.vaccine.Controller;

import com.vaccine.Model.Entity.Person;
import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Service.PersonService;
import com.vaccine.Service.PersonVaccineService;
import com.vaccine.Service.VaccineService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.SceneNavigator;
import com.vaccine.Utils.Session;
import com.vaccine.VaccineSystem.Main;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;


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
    private Label vaccineLabel;
    @FXML
    private Pane personPane;


    private final PersonVaccineService personVaccineService;
    private final PersonService personService;
    private final VaccineService vaccineService;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    private int personId, vaccineId;
    public PersonPaneController() {
        this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
        this.personVaccineService = PersonVaccineService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.personService = PersonService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    public void setPersonPaneData(Person p, Vaccine v , String period) {
        if ( !v.getVaccineName().isEmpty() && !p.getName().isEmpty()) {
            String firstTwoChars = p.getName().substring(0, Math.min(p.getName().length(), 2));
            letterLabel.setText(firstTwoChars.toUpperCase());
            nameLabel.setText(p.getName() +" ("+p.getRelationWithUser()+")");
            vaccineLabel.setText(v.getVaccineName());
            periodLabel.setText(period);
            setPersonIdAndVaccineId(p.getId(),v.getId());

        } else {
            letterLabel.setText("No notifications");
        }
    }

    @FXML
    public void handleCheckBox() throws IOException {
        if (isVaccineCheckBox.isSelected()) {
            PersonVaccine personVaccine = new PersonVaccine();
            personVaccine.setPerson(personService.getById(personId));
            personVaccine.setVaccine(vaccineService.getById(vaccineId));
            personVaccine.setMade(true);
            personVaccine.setVaccinationDate(Date.valueOf(LocalDate.now()));
            personVaccineService.save(personVaccine);
            personPane.setVisible(false);

            Main app = new Main();
            app.changeScene("/com/vaccine/fxml/vac-main.fxml",1000,600,true);

        } else {
            log.info("Checkbox is not checked");
        }
    }

    public void setPersonIdAndVaccineId(int personId, int vaccineId) {
        this.personId = personId;
        this.vaccineId = vaccineId;
    }


}
