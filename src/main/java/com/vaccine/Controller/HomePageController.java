package com.vaccine.Controller;

import com.vaccine.Service.PersonService;
import com.vaccine.Service.UserService;
import com.vaccine.Utils.Connection;
import com.vaccine.Utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import com.vaccine.Utils.Session;
import com.vaccine.Model.Entity.User;
import com.vaccine.Model.Entity.Person;
import com.vaccine.VaccineSystem.Main;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class HomePageController implements Initializable {
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private VBox personVBox;
    @FXML
    private ScrollPane rightPane;

    private final UserService userService;
    private final PersonService personService;

    private final Session session = Session.getInstance();

    private static final Logger log = LogManager.getLogger(LoginController.class);
     public  HomePageController(){

     this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.personService = PersonService.getInstance(Connection.getEntityManager(),Session.getInstance());
     }

    @FXML
    public void initialize() {

    }

    @FXML
    public void LogOut() throws IOException {
        Main app= new Main();
        app.changeScene("/com/vaccine/fxml/vac-login-view.fxml",747, 438,false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Person> persons = persons();

        for (Person person : persons) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/vaccine/fxml/person-pane.fxml"));

            try {
                Pane anchorPane = fxmlLoader.load();
                PersonPaneController personPaneController = fxmlLoader.getController();
                personPaneController.setPersonPaneData(person);
                personVBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Person> persons(){
        User currentUser = session.getUser();
        int currentUserId = currentUser.getId();
        return  personService.getPersonsList(currentUserId);
    }

    @FXML
    private void openUserEditScene() {
        SceneNavigator.navigateTo("/com/vaccine/fxml/edit-user.fxml", mainAnchor);
    }

    @FXML
    private void openVaccineManagement() {
        rightPane.setVisible(false);
        SceneNavigator.navigateTo("/com/vaccine/fxml/vaccine-management.fxml", mainAnchor);
    }
}
