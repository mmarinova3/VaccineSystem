package com.vaccine.Controller;

import com.vaccine.Model.Entity.PersonVaccine;
import com.vaccine.Service.PersonVaccineService;
import com.vaccine.Utils.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;

public class PersonVaccineDetailsController {

    @FXML
    private Label infoLabel;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker vaccinationDate;

    private PersonVaccine personVaccine;
    private final PersonVaccineService personVaccineService;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public PersonVaccineDetailsController() {
        this.personVaccineService = PersonVaccineService.getInstance(Connection.getEntityManager());
    }

    public void setPersonVaccine(PersonVaccine personVaccine) {
        this.personVaccine = personVaccine;
        updateUI();
    }

    private void updateUI() {
        noteTextArea.setText(personVaccine.getNote());
        vaccinationDate.setValue(personVaccine.getVaccinationDate().toLocalDate());
    }

    @FXML
    protected void saveChangesButtonOnClick() {
        if (personVaccine != null) {
            String note = noteTextArea.getText();
            java.sql.Date vaccDate = Date.valueOf(vaccinationDate.getValue());

            personVaccine.setNote(note);
            personVaccine.setVaccinationDate(vaccDate);

            personVaccineService.update(personVaccine,new String[]{personVaccine.getNote(), String.valueOf(personVaccine.getVaccinationDate())});
            infoLabel.setText("Changes saved successfully");
        } else {
            infoLabel.setText("Changes aren't saved");
            log.error("No PersonVaccine object set in PersonVaccineDetailsController.");
        }
    }
}
