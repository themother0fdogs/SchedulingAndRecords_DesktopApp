package Controller;

import DAO.DBAppointments;
import DAO.DBCustomers;
import DAO.UpdateDeleteAdd;
import Model.Customers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the CustomerRecordsForm class that works with the CustomerRecordsForm FXML to display all of the customers on a table. */
public class CustomerRecordsForm implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();

    @FXML
    private TableColumn<Customers, Integer> idCol;
    @FXML
    private TableColumn<Customers, String> nameCol;
    @FXML
    private TableColumn<Customers, String> phoneCol;
    @FXML
    private TableColumn<Customers, String> addressCol;
    @FXML
    private TableColumn<Customers, String> countryCol;
    @FXML
    private TableColumn<Customers, String> postalCol;
    @FXML
    private TableColumn<Customers, String> divisionCol;
    @FXML
    private TableView<Customers> customersTableView;

    /**When the Add button is pressed, a new screen will load for the user to add a new customer. */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddCustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** When the Back button is pressed, the screen will return back to the Select Option Form for the user to
     select a different option to view. */
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/SelectOptionForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

/** When the Delete button is pressed, a screen pops up and asks for the user to confirm if they want to delete the selected customer.
 This screen will also show the customer's name. Once deleted, another screen will pop up to show that the customer and their name has been
 deleted. This will also update the Customer Records table and in MySQL.
 An alert is coded if a user tries to delete a customer that has existing appointments. The user must delete the appointments
 before continuing on with deleting.
 LAMBDA EXPRESSION #2 (same comment is written write above the Lambda coding below): I chose to use the lambda expression for an Alert because it provides a clearer way to display an alert.
 The code is stating that if the user were to press the OK button,
 it will continue on with the deletion or else it will not run the code. */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        try {
            Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
            int selectedCustomerId = selectedCustomer.getCustomerId();
            String selectedCustomerName = selectedCustomer.getCustomerName();
            if (DBAppointments.getExistingAppts(selectedCustomerId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Customer has existing appointments! Unable to delete! \nPlease delete the customer's existing appointment to continue.");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer selected: " + selectedCustomerName + "\nAre you sure you want to delete this customer?");
                Alert deleteCompleted = new Alert(Alert.AlertType.INFORMATION);
                deleteCompleted.setContentText("The customer (" + selectedCustomerName + ") was deleted successfully!");
/*LAMBDA EXPRESSION #2: I chose to use the lambda expression for an Alert because it provides a clearer way to display an alert.
The code is stating that if the user were to press the OK button,
it will continue on with the deletion or else it will not run the code. */
                alert.showAndWait().ifPresent((result -> {
                    if(result == ButtonType.OK){
                        deleteCompleted.show();
                        try {
                            allCustomers.remove(selectedCustomer);
                            UpdateDeleteAdd.deleteCustomer(selectedCustomer.getCustomerId()); }
                        catch (SQLException throwables) { throwables.printStackTrace();}
                    }
                }));
            }
        }

        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to delete!");
            alert.showAndWait();
        }
    }

    /** When the Update button is pressed, another screen will pop up with the selected customer's data so that the
     user is able to update the record. If the Update button is pressed without selecting a customer, an alert will
     ask the user to select a customer to update before continuing.
     @param event opens a new page for user to update customer information */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateCustomerForm.fxml"));
            loader.load();

            UpdateCustomerForm UCFController = loader.getController();
            UCFController.selectedCustomer(customersTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a customer to update!");
            alert.showAndWait();
        }
    }

    /** This initializes the table with the correct data for each column. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTableView.setItems(allCustomers);
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        }
    }

