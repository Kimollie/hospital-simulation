package com.simulator.hospital.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultView extends Application {
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/simulator/hospital/result2.fxml")); //load main Menu first

            //Set up stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Hospital Simulation Result");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
