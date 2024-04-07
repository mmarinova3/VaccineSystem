package com.vaccine.Controller;

import com.vaccine.VaccineSystem.Main;
import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterController {
    @FXML
    protected void onGoBackButtonClick() throws IOException {
        Main app= new Main();
        app.changeScene("/com/vaccine/vac-login-view.fxml",747, 438,false);
    }
}
