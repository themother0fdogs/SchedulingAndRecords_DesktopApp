package com.company;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** This class creates an the Customer Records and Scheduling Application. */
public class Main extends Application {
    /** This is the start method that will appropriately display the login screen based on the set language of the user's desktop. */
    @Override
    public void start(Stage stage) {
        try{
            ResourceBundle rb = ResourceBundle.getBundle("com.company/Language", Locale.getDefault());
            Parent main = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UserLoginForm.fxml"));
            loader.setResources(rb);
            main = loader.load();
            Scene scene = new Scene(main);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
/** This is the main method. This will pull open a connection between the application and database and close it once the application is closed.
    @param args Launches the application. */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
