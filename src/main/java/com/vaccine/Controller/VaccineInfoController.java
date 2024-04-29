package com.vaccine.Controller;

import com.vaccine.Model.Entity.Vaccine;
import com.vaccine.Service.VaccineService;
import com.vaccine.Utils.Connection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class VaccineInfoController {

    @FXML
    private Label infoLabel;
    @FXML
    private TextField keywordField;
    @FXML
    private TextArea moreInfoArea;
    @FXML
    private TableView<Vaccine> vaccineTableView;

    private final VaccineService vaccineService;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public VaccineInfoController() {
        this.vaccineService = VaccineService.getInstance(Connection.getEntityManager());
    }
    @FXML
    public void initialize() {
        moreInfoArea.setDisable(false);
        populateVaccineTable();
        vaccineTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showMoreInfo();
            }
        });
    }

    @FXML
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
                effectivenessPeriodColumn, isMandatoryColumn, isOneTimeColumn);
    }

    @FXML
    private void searchButtonOnClick() {
        String keyword = keywordField.getText().trim().toLowerCase();

        ObservableList<Vaccine> filteredVaccines = FXCollections.observableArrayList();
        boolean matchFound = false;

        for (Vaccine vaccine : vaccineTableView.getItems()) {
            if (vaccineContainsKeyword(vaccine, keyword)) {
                filteredVaccines.add(vaccine);
                log.info(vaccine.getInfo());
                matchFound = true;
            }
        }

        if (matchFound) {
            vaccineTableView.setItems(filteredVaccines);
            infoLabel.setText("");
        } else {
            infoLabel.setText("No vaccines matching the keyword found.");
        }
    }

    private boolean vaccineContainsKeyword(Vaccine vaccine, String keyword) {
        return vaccine.getVaccineName().toLowerCase().contains(keyword) ||
                String.valueOf(vaccine.getAgeOfUse()).contains(keyword) ||
                vaccine.getApplicationMethod().toLowerCase().contains(keyword) ||
                String.valueOf(vaccine.getEffectivenessPeriod()).contains(keyword) ||
                String.valueOf(vaccine.isMandatory()).toLowerCase().contains(keyword) ||
                String.valueOf(vaccine.isOneTime()).toLowerCase().contains(keyword) ||
                vaccine.getInfo().toLowerCase().contains(keyword);
    }

    private void showMoreInfo() {
        Vaccine selectedVaccine = vaccineTableView.getSelectionModel().getSelectedItem();
        if (selectedVaccine != null) {
            moreInfoArea.setText("More info for "+selectedVaccine.getVaccineName()+":\n"+selectedVaccine.getInfo());
        }
    }
}
