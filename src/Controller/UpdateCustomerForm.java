package Controller;

import DAO.DBCountries;
import DAO.DBDivisions;
import DAO.UpdateDeleteAdd;
import Model.Countries;
import Model.Customers;
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

/** This is the UpdateCustomerForm class that works with the UpdateCustomerForm FXML. It allows the user to update a customer's record. */
public class UpdateCustomerForm implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private ComboBox<String> updateDivisionCombo;
    @FXML
    private ComboBox<Countries> updateCountryCombo;
    @FXML
    private TextField updateAddressTxt;
    @FXML
    private TextField updatePostalTxt;
    @FXML
    private TextField updateIdTxt;
    @FXML
    private TextField updateNameTxt;
    @FXML
    private TextField updatePhoneTxt;

    /** This retrieves the customer's country.
     @param customerCountry searches for the customer's country by name.
     @return returns the customer's country. */
    private Countries getCustomerCountry(String customerCountry) {
        ObservableList<Countries> allCountries = DBCountries.getAllCountries();
        Countries returnCountries = null;
        for (int i = 0; i < allCountries.size(); i++) {
            Countries count = allCountries.get(i);
            if (!count.getCountryName().equals(customerCountry)) {
                continue;
            } else {
                returnCountries = count;
                break;
            }
        }
        return returnCountries;
    }
/** This retrieves the customer's division.
 @param customerDivision searches for the customer's division by name.
 @return returns the customer's division. */
    private Divisions getCustomerDivision(String customerDivision) {
        ObservableList<Divisions> allDivisions = DBDivisions.getAllDivisions();
        Divisions returnDivisions = null;

        for (int i = 0; i < allDivisions.size(); i++) {
            Divisions div = allDivisions.get(i);
            if (!div.getDivisionName().equals(customerDivision)) {
                continue;
            } else {
                returnDivisions = div;
                break;
            }
        }
        return returnDivisions;
    }

    /** This populate the form with the selected Customer's data.
     @param customers finds the selected customer's records.  */
    public void selectedCustomer(Customers customers) {
        updateIdTxt.setText(String.valueOf(customers.getCustomerId()));
        updateNameTxt.setText(customers.getCustomerName());
        updatePhoneTxt.setText(customers.getCustomerPhone());
        updateAddressTxt.setText(customers.getCustomerAddress());
        updatePostalTxt.setText(customers.getCustomerPostalCode());

        Countries customerCountry = getCustomerCountry(customers.getCustomerCountry());
        updateCountryCombo.setValue(customerCountry);

        Divisions customerDivision = getCustomerDivision(customers.getCustomerDivision());
        updateDivisionCombo.setValue(customerDivision.toString());
    }

    /** When the Cancel button is pressed, the user will be returned to the Customer Appointments Form and no data is saved. */
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
            String customerName = updateNameTxt.getText();
            String customerAddress = updateAddressTxt.getText();
            String customerPostalCode = updatePostalTxt.getText();
            String customerPhone = updatePhoneTxt.getText();
            int customerId = Integer.parseInt(updateIdTxt.getText());

            if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
                alert.showAndWait();
                return;
            }

            for (Divisions divs : DBDivisions.getAllDivisions()) {
                String stringDivision = updateDivisionCombo.getValue();
                if (stringDivision.equals(divs.getDivisionName())) {
                    int customerDivisionId = divs.getDivisionId();
                    UpdateDeleteAdd.updateCustomer(customerName, customerAddress, customerPostalCode, customerPhone, customerDivisionId, customerId);
                }
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/CustomerRecordsForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
            alert.showAndWait();
        }
    }

    /** This initializes the countries with its divisions. Separate ObservableLists are created for each country
     so that each Division with the matching Country ID will be added and separated into that list.

     LAMBDA EXPRESSION #3(same comment is written write above the Lambda coding below): I chose to use the lambda expression
     here because when a user chooses one of the three countries, the Division's ComboBox will update to only show
     that country's division. The Country IDs in the Countries table will match
     up with the Country IDs of the Divisions table.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Countries> allCountries = DBCountries.getAllCountries();
            updateCountryCombo.setItems(allCountries);

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
/*LAMBDA EXPRESSION #3: I chose to use the lambda expression here because when a user chooses one of the three countries,
the Division's ComboBox will update to only show that country's division. The Country IDs in the Countries table will match
up with the Country IDs of the Divisions table. */
            updateCountryCombo.valueProperty().addListener((observableValue, countries, t1) -> {

                for (Countries selCountry : updateCountryCombo.getItems()) {
                    Countries selectedCountry = updateCountryCombo.getSelectionModel().getSelectedItem();

                    if (selCountry.getCountryId() == 1 || selectedCountry.getCountryId() == 1) {
                        updateDivisionCombo.setItems(usDivisions);
                        updateDivisionCombo.setVisibleRowCount(10);

                    } else if (selCountry.getCountryId() == 2 || selectedCountry.getCountryId() == 2) {
                        updateDivisionCombo.setItems(ukDivisions);
                        updateDivisionCombo.setVisibleRowCount(4);

                    } else if (selCountry.getCountryId() == 3 || selectedCountry.getCountryId() == 3) {
                        updateDivisionCombo.setItems(caDivisions);
                        updateDivisionCombo.setVisibleRowCount(10);
                    }

                }
            });
        }
        catch(NullPointerException e){
            System.out.println("broken");
        }
    }
}

