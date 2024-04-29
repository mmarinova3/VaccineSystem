package com.vaccine.Controller;

import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Service.VaccineService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class VaccineManagementController {
    @FXML
    private TextField ageOfUseField;
    @FXML
    private TextField applicationMethodField;
    @FXML
    private TextField effectivenessPeriodField;
    @FXML
    private Label infoLabel;
    @FXML
    private ComboBox<String> isMandatoryBox;
    @FXML
    private ComboBox<String> isOneTimeBox;
    @FXML
    private TextField noteField;
    @FXML
    private TextField vaccineNameField;
    @FXML
    private TableView<Vaccine> vaccineTableView;

    private final VaccineService vaccineService;
    private static final Logger log = LogManager.getLogger(LoginController.class);
    public VaccineManagementController() {
        this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
    }
    @FXML
    public void initialize() {
        ObservableList<String> yesNoOptions = FXCollections.observableArrayList("Yes", "No");
        isMandatoryBox.setItems(yesNoOptions);
        isOneTimeBox.setItems(yesNoOptions);
        populateVaccineTable();

        vaccineTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsWithSelectedVaccine();
            }
        });
    }

    private void populateVaccineTable() {
        List<Vaccine> vaccineList = vaccineService.getAll();
        if (vaccineList == null) {
            log.info("Vaccine list is null.");
            return;
        }

        ObservableList<Vaccine> vaccines = FXCollections.observableArrayList(vaccineList);
        vaccineTableView.setItems(vaccines);

        TableColumn<Vaccine, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("VaccineName"));

        TableColumn<Vaccine, Integer> ageOfUseColumn = new TableColumn<>("Age of Use");
        ageOfUseColumn.setCellValueFactory(new PropertyValueFactory<>("AgeOfUse"));

        TableColumn<Vaccine, String> applicationMethodColumn = new TableColumn<>("Application Method");
        applicationMethodColumn.setCellValueFactory(new PropertyValueFactory<>("ApplicationMethod"));

        TableColumn<Vaccine, Integer> effectivenessPeriodColumn = new TableColumn<>("Effectiveness Period");
        effectivenessPeriodColumn.setCellValueFactory(new PropertyValueFactory<>("EffectivenessPeriod"));

        TableColumn<Vaccine, String> isMandatoryColumn = new TableColumn<>("Is Mandatory");
        isMandatoryColumn.setCellValueFactory(data -> {
            boolean isMandatoryValue = data.getValue().isMandatory();
            return new SimpleStringProperty(isMandatoryValue ? "Yes" : "No");
        });

        TableColumn<Vaccine, String> isOneTimeColumn = new TableColumn<>("Is One Time");
        isOneTimeColumn.setCellValueFactory(data -> {
            boolean isOneTimeValue = data.getValue().isOneTime();
            return new SimpleStringProperty(isOneTimeValue ? "Yes" : "No");
        });

        TableColumn<Vaccine, String> noteColumn = new TableColumn<>("Info");
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("Info"));

        vaccineTableView.getColumns().addAll(nameColumn, ageOfUseColumn, applicationMethodColumn,
                effectivenessPeriodColumn, isMandatoryColumn, isOneTimeColumn, noteColumn);
    }

    @FXML
    private void addVaccineButtonOnClick() {
        String vaccineName = vaccineNameField.getText();
        int ageOfUse = Integer.parseInt(ageOfUseField.getText());
        String applicationMethod = applicationMethodField.getText();
        int effectivenessPeriod = Integer.parseInt(effectivenessPeriodField.getText());
        String isMandatoryValue = isMandatoryBox.getValue();
        String isOneTimeValue = isOneTimeBox.getValue();
        String note = noteField.getText();

        boolean isMandatory = isMandatoryValue.equals("Yes");
        boolean isOneTime = isOneTimeValue.equals("Yes");

        if (vaccineName.isEmpty() || ageOfUse == 0 || applicationMethod.isEmpty() || effectivenessPeriod == 0) {
            infoLabel.setText("Please fill all the required fields.");
            return;
        }

        Vaccine newVaccine = new Vaccine();
        newVaccine.setVaccineName(vaccineName);
        newVaccine.setAgeOfUse(ageOfUse);
        newVaccine.setApplicationMethod(applicationMethod);
        newVaccine.setEffectivenessPeriod(effectivenessPeriod);
        newVaccine.setMandatory(isMandatory);
        newVaccine.setOneTime(isOneTime);
        newVaccine.setInfo(note);

        vaccineService.save(newVaccine);

        clearInputFields();
        populateVaccineTable();

        infoLabel.setText("Vaccine added successfully.");
    }

    @FXML
    private void updateVaccineButtonOnClick() {
        Vaccine selectedVaccine = vaccineTableView.getSelectionModel().getSelectedItem();
        if (selectedVaccine != null) {
            String vaccineName = vaccineNameField.getText();
            int ageOfUse = Integer.parseInt(ageOfUseField.getText());
            String applicationMethod = applicationMethodField.getText();
            int effectivenessPeriod = Integer.parseInt(effectivenessPeriodField.getText());
            String isMandatoryValue = isMandatoryBox.getValue();
            String isOneTimeValue = isOneTimeBox.getValue();
            String note = noteField.getText();

            boolean isMandatory = isMandatoryValue.equals("Yes");
            boolean isOneTime = isOneTimeValue.equals("Yes");

            selectedVaccine.setVaccineName(vaccineName);
            selectedVaccine.setAgeOfUse(ageOfUse);
            selectedVaccine.setApplicationMethod(applicationMethod);
            selectedVaccine.setEffectivenessPeriod(effectivenessPeriod);
            selectedVaccine.setMandatory(isMandatory);
            selectedVaccine.setOneTime(isOneTime);
            selectedVaccine.setInfo(note);

            vaccineService.update(selectedVaccine,new String[]{selectedVaccine.getVaccineName(), String.valueOf(selectedVaccine.getAgeOfUse()),selectedVaccine.getApplicationMethod(), String.valueOf(selectedVaccine.getEffectivenessPeriod()), String.valueOf(selectedVaccine.isMandatory()), String.valueOf(selectedVaccine.isOneTime()),selectedVaccine.getInfo()});

            populateVaccineTable();

            infoLabel.setText("Vaccine updated successfully.");
        } else {
            infoLabel.setText("Please select a vaccine to update.");
        }
    }

    @FXML
    private void deleteVaccineButtonOnClick() {
        Vaccine selectedVaccine = vaccineTableView.getSelectionModel().getSelectedItem();
        if (selectedVaccine != null) {
            vaccineService.delete(vaccineService.getById(selectedVaccine.getId()));
            populateVaccineTable();
            clearInputFields();
            infoLabel.setText("Vaccine deleted successfully.");
        } else {
            infoLabel.setText("Please select a vaccine to delete.");
        }
    }


    private void clearInputFields() {
        vaccineNameField.clear();
        ageOfUseField.clear();
        applicationMethodField.clear();
        effectivenessPeriodField.clear();
        isMandatoryBox.getSelectionModel().clearSelection();
        isOneTimeBox.getSelectionModel().clearSelection();
        noteField.clear();
    }

    @FXML
    private void fillFieldsWithSelectedVaccine() {
        Vaccine selectedVaccine = vaccineTableView.getSelectionModel().getSelectedItem();
        if (selectedVaccine != null) {
            vaccineNameField.setText(selectedVaccine.getVaccineName());
            ageOfUseField.setText(String.valueOf(selectedVaccine.getAgeOfUse()));
            applicationMethodField.setText(selectedVaccine.getApplicationMethod());
            effectivenessPeriodField.setText(String.valueOf(selectedVaccine.getEffectivenessPeriod()));
            isMandatoryBox.setValue(selectedVaccine.isMandatory() ? "Yes" : "No");
            isOneTimeBox.setValue(selectedVaccine.isOneTime() ? "Yes" : "No");
            noteField.setText(selectedVaccine.getInfo());
            log.info(selectedVaccine.getId());
        }
    }
    @FXML
    private void searchButtonOnClick() {
        String keyword = vaccineNameField.getText().trim().toLowerCase();

        List<Vaccine> filteredVaccines = vaccineService.getAll().stream()
                .filter(vaccine ->
                        vaccine.getVaccineName().toLowerCase().contains(keyword) ||
                                String.valueOf(vaccine.getAgeOfUse()).contains(keyword) ||
                                vaccine.getApplicationMethod().toLowerCase().contains(keyword) ||
                                String.valueOf(vaccine.getEffectivenessPeriod()).contains(keyword) ||
                                vaccine.getInfo().toLowerCase().contains(keyword) ||
                                (vaccine.isMandatory() ? "Yes" : "No").contains(keyword) ||
                                (vaccine.isOneTime() ? "Yes" : "No").contains(keyword) ||
                                ageOfUseField.getText().toLowerCase().contains(keyword) ||
                                applicationMethodField.getText().toLowerCase().contains(keyword) ||
                                effectivenessPeriodField.getText().toLowerCase().contains(keyword) ||
                                noteField.getText().toLowerCase().contains(keyword) ||
                                vaccineNameField.getText().toLowerCase().contains(keyword))
                .toList();

        ObservableList<Vaccine> filteredList = FXCollections.observableArrayList(filteredVaccines);
        vaccineTableView.setItems(filteredList);
    }

}
