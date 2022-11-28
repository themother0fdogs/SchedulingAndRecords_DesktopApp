package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/** This is the SelectOptionForm class that works with the SelectOptionForm FXML to display the two options that users can choose between. */
public class SelectOptionForm {
    Stage stage;
    Parent scene;

    /** When Customer Appointments button is pressed, the Customer Appointment Form will pop up.
     * @param event Opens Appointments table */
    @FXML
    void onActionAppts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerApptForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** When the Customer Records button is pressed, the Customer Records Form will pop up.
     * @param event Opens Customers table */
    @FXML
    void onActionRecords(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerRecordsForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** When the Exit button is pressed, it will close the application. */
    @FXML
    void onActionExit(ActionEvent event) throws IOException {
        System.exit(0);
    }
}
