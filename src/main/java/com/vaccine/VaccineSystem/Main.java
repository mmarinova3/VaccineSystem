package com.vaccine.VaccineSystem;

import com.vaccine.Model.DAO.UserDAO;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;


public class Main extends Application {

    private static final Logger log = LogManager.getLogger(Main.class);
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vaccine/hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 506, 312);
        primaryStage.setTitle("Vaccine System");
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}