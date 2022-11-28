package Controller;

import DAO.DBCountries;
import DAO.DBDivisions;
import DAO.UpdateDeleteAdd;
import Model.Countries;
import Model.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the AddCustomerForm class that works with the AddCustomerForm FXML and allows users to add new customers. */
public class AddCustomerForm implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TextField addAddressTxt;
    @FXML
    private TextField addIdTxt;
    @FXML
    private TextField addNameTxt;
    @FXML
    private TextField addPhoneTxt;
    @FXML
    private TextField addPostalTxt;
    @FXML
    private ComboBox<Countries> addCountryCombo;
    @FXML
    private ComboBox<String> addDivisionCombo;

    /** When a user clicks the Cancel button, it will return the user back to the Customer Records Form.
      @param event returns the user back to the Customer Appointments screen. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerRecordsForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** When a user clicks the Save button, it will save the user's input into the Customer Records Form.
     While the Customer Records Form is updated, MySQL will be updated.
     I created a code that will alert the user if there is information missing.
     @param event saves data into MySQL database */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {
        try {
            String customerName = addNameTxt.getText();
            String customerAddress = addAddressTxt.getText();
            String customerPostalCode = addPostalTxt.getText();
            String customerPhone = addPhoneTxt.getText();

            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
                alert.showAndWait();
                return;
            }

            for (Divisions divs : DBDivisions.getAllDivisions()) {
                String stringDivision = addDivisionCombo.getValue();
                if (stringDivision.equals(divs.getDivisionName())) {
                    int customerDivisionId = divs.getDivisionId();
                    UpdateDeleteAdd.addCustomer(customerName, customerAddress, customerPostalCode, customerPhone, customerDivisionId);
                }
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/CustomerRecordsForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
            alert.showAndWait();
        }
    }
    /** This initializes the countries with its divisions. Separate ObservableLists are created for each country
     so that each Division with the matching Country ID will be added and separated into that list.

     LAMBDA EXPRESSION #1(same comment is written write above the Lambda coding below): I chose to use the lambda expression
     here because when a user chooses one of the three countries, the Division's ComboBox will update to only show
     that country's division. The Country IDs in the Countries table will match
     up with the Country IDs of the Divisions table.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Countries> allCountries = DBCountries.getAllCountries();
            addCountryCombo.setItems(allCountries);

            ObservableList<Divisions> allDivisions = DBDivisions.getAllDivisions();
            ObservableList<String> usDivisions = FXCollections.observableArrayList();
            ObservableList<String> ukDivisions = FXCollections.observableArrayList();
            ObservableList<String> caDivisions = FXCollections.observableArrayList();

            for (Divisions divisionResult : allDivisions) {
                if (divisionResult.getCountryId() == 1) {
                    usDivisions.add(divisionResult.getDivisionName());

                } else if (divisionResult.getCountryId() == 2) {
                    ukDivisions.add(divisionResult.getDivisionName());

                } else if (divisionResult.getCountryId() == 3) {
                    caDivisions.add(divisionResult.getDivisionName());
                }
            }
/*LAMBDA EXPRESSION #1: I chose to use the lambda expression here because when a user chooses one of the three countries,
the Division's ComboBox will update to only show that country's division. The Country IDs in the Countries table will match
up with the Country IDs of the Divisions table. */
            addCountryCombo.valueProperty().addListener((observableValue, countries, t1) -> {
                for (Countries selCountry : addCountryCombo.getItems()) {
                    Countries selectedCountry = addCountryCombo.getSelectionModel().getSelectedItem();

                    if (selCountry.getCountryId() == 1 || selectedCountry.getCountryId() == 1) {
                        addDivisionCombo.setItems(usDivisions);
                        addDivisionCombo.setVisibleRowCount(10);

                    } else if (selCountry.getCountryId() == 2 || selectedCountry.getCountryId() == 2) {
                        addDivisionCombo.setItems(ukDivisions);
                        addDivisionCombo.setVisibleRowCount(4);

                    } else if (selCountry.getCountryId() == 3 || selectedCountry.getCountryId() == 3) {
                        addDivisionCombo.setItems(caDivisions);
                        addDivisionCombo.setVisibleRowCount(10);
                    }
                }
            });
        }
        catch(NullPointerException e){
            System.out.println("broken");
        }
    }
}
