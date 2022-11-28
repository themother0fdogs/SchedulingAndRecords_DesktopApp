package Controller;

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.UpdateDeleteAdd;
import Model.Appointments;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the CustomerApptForm class that works with the CustomerApptForm FXML to display the customer's data in a table. */
public class CustomerApptForm implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<Appointments> allAppts = DBAppointments.getAllAppts();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDate currentDate = LocalDate.now();

    @FXML
    private Label totalContactLbl;
    @FXML
    private Label totalTypeMonthLbl;
    @FXML
    private RadioButton showAllRbtn;
    @FXML
    private RadioButton currentMonthRbtn;
    @FXML
    private RadioButton currentWeekRbtn;
    @FXML
    private ToggleGroup filterToggle;
    @FXML
    private TableColumn<Appointments, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointments, String> titleCol;
    @FXML
    private TableColumn<Appointments, String> descriptionCol;
    @FXML
    private TableColumn<Appointments, String> locationCol;
    @FXML
    private TableColumn<Appointments, String> contactCol;
    @FXML
    private TableColumn<Appointments, String> typeCol;
    @FXML
    private TableColumn<Appointments, String> startCol;
    @FXML
    private TableColumn<Appointments, String> endCol;
    @FXML
    private TableColumn<Appointments, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointments, Integer> userIdCol;
    @FXML
    private TableView<Appointments> apptTableView;
    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private ComboBox<Contacts> selectContactCombo;
    @FXML
    private ComboBox<Contacts> totalContactCombo;
    @FXML
    private ComboBox<String> typeCombo;

    /** When the Add button is pressed, a new page will pop up that will allow users to add new appointments.
     @param event This goes to the AddApptForm. */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddApptForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** When the Back button is pressed, the user will be returned to the Select Option form.
     @param event This goes back to the SelectOptionForm. */
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/SelectOptionForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** When the Generate button is pressed, the code will read the user's selection of Type and Month and generate
     how many appointments return with the matching selections.
     I created an alert if a user were to press the button without choosing an option. It will tell the user to
     choose both a Type and Month before being able to generate the information. */
    @FXML
    void onActionGenerate(ActionEvent event) {
        try {
            String selectedType = typeCombo.getSelectionModel().getSelectedItem();
            String selectedMonth = monthCombo.getSelectionModel().getSelectedItem();
            ObservableList<String> resultString = FXCollections.observableArrayList();

            for (Appointments totalTypeMonth : allAppts) {
                DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMMM");
                String selectMonth = totalTypeMonth.getApptStart();
                LocalDateTime startOfAppt = LocalDateTime.parse(selectMonth, format);
                String stringMonth = startOfAppt.format(monthFormat);
                ObservableList<String> monthList = FXCollections.observableArrayList(stringMonth);

                if (totalTypeMonth.getApptType().contains(selectedType) && monthList.contains(selectedMonth)) {
                    resultString.add(String.valueOf(totalTypeMonth));
                }
            }
            int total = resultString.size();
            totalTypeMonthLbl.setText(String.valueOf(total));
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select options for both Type and Month!");
            alert.showAndWait();
        }
    }
/** When the Clear button is pressed, the Type and Month ComboBoxes will be reset with no values. This allows users
    to choose different options to generate. */
    @FXML
    void onActionClear(ActionEvent event) {
        typeCombo.setValue(null);
        monthCombo.setValue(null);
        totalTypeMonthLbl.setText(null);
    }

/** This ComboBox is an additional report of my choice (A.3.f on rubric) where the user can see how many appointments
    each contact has. The user can click on the drop down menu and choose which contact they want to see an
    appointment total for. */
    @FXML
    void onActionContactCombo(ActionEvent event) {
        try {
            Contacts selectedContact = totalContactCombo.getSelectionModel().getSelectedItem();
            ObservableList<String> contactString = FXCollections.observableArrayList();

            for (Appointments appts : allAppts) {
                if (selectedContact.getContactName().equals(appts.getContactName())) {
                    contactString.add(String.valueOf(appts));
                }
            }
            int intAppt = contactString.size();
            totalContactLbl.setText(String.valueOf(intAppt));
        }
        catch(NullPointerException e)
        {totalContactCombo.setValue(null);}
    }
    /** When the Clear button is pressed, the Contact ComboBox will be reset with no values. This allows users
     to choose different options to generate. */
    @FXML
    void onActionClearContact(ActionEvent event) {
            totalContactCombo.setValue(null);
            totalContactLbl.setText(null);
    }

    /** This is a radio button to show all of the appointments on the table when selected. */
    @FXML
    void onActionShowAllRbtn(ActionEvent event) {
            if (showAllRbtn.isSelected()) {
                apptTableView.setItems(allAppts);
                selectContactCombo.setValue(null);
            }
    }

    /** This radio button will show appointments that are for the current month. */
    @FXML
    void onActionCurrentMonthRbtn(ActionEvent event) {
        ObservableList<Appointments> selectedMonth = FXCollections.observableArrayList();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

            for (Appointments byMonth : allAppts) {
                String stringAppt = byMonth.getApptStart();
                LocalDate startOfAppt = LocalDate.parse(stringAppt, format);
                Month monthOfAppt = startOfAppt.getMonth();
                int yearOfAppt = startOfAppt.getYear();

                if (monthOfAppt.toString().contains(currentMonth.toString()) && yearOfAppt == currentYear){
                    selectedMonth.add(byMonth);
                }
            }
            apptTableView.setItems(selectedMonth);
            selectContactCombo.setValue(null);
    }

    /** This radio button will show appointments that are for the current week. */
    @FXML
    void onActionCurrentWeekRbtn(ActionEvent event) {
        ObservableList<Appointments> selectedWeek = FXCollections.observableArrayList();

        for(Appointments byWeek : allAppts) {
            String stringWeek = byWeek.getApptStart();
            LocalDate apptDay = LocalDate.parse(stringWeek, format);

            LocalDate sunday = currentDate;
            while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
            {sunday = sunday.minusDays(1);}
            LocalDate saturday = currentDate;
            while(saturday.getDayOfWeek() != DayOfWeek.SATURDAY)
            {saturday = saturday.plusDays(1);}

            if (apptDay.isAfter(sunday) && apptDay.isBefore(saturday)) {
                selectedWeek.add(byWeek);
            }
        }
        apptTableView.setItems(selectedWeek);
        selectContactCombo.setValue(null);
    }

    /** This ComboBox at the top of the form will show all of the appointments for the selected Contact. */
    @FXML
    void onActionFilterByContact(ActionEvent event) {
        try{
            Contacts filterContact = selectContactCombo.getSelectionModel().getSelectedItem();
            ObservableList<Appointments> contactResult = FXCollections.observableArrayList();

            for (Appointments appts : allAppts) {
                if (filterContact.getContactName().equals(appts.getContactName())) {
                    contactResult.add(appts);
                }
            }
            apptTableView.setItems(contactResult);
            currentMonthRbtn.setSelected(false);
            showAllRbtn.setSelected(false);
            currentWeekRbtn.setSelected(false);
            }
        catch(NullPointerException e)
        {
            selectContactCombo.getSelectionModel().clearSelection();
        }
    }

    /** When the Delete button is pressed, an alert will ask if the user is sure they want to delete the appointment and will
     also include the selected Appointment ID and Appointment Type. If the user confirms to delete, the appointment will
     be deleted with an Information screen showing the Appointment ID and Type deleted. This will also update the data in mySQL.
     If the user presses Delete without selecting an appointment, an alert will pop up to let the user know that they
     need to select an appointment before continuing. */
    @FXML
    void onActionDeleteAppt(ActionEvent event) throws SQLException {
        Appointments selectedAppt = apptTableView.getSelectionModel().getSelectedItem();
        int selectedApptId = selectedAppt.getApptId();
        String selectedApptType = selectedAppt.getApptType();
        if(selectedAppt == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please select an appointment to delete!");
                alert.showAndWait();
            }
        else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?" +
                        "\nAppointment ID :" +selectedApptId +
                        "\nAppointment Type: " + selectedApptType);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    Alert deleteCompleted = new Alert(Alert.AlertType.INFORMATION);
                    deleteCompleted.setContentText("The appointment (" + "ID: " + selectedApptId +
                            " | Type: " + selectedApptType + ") was cancelled successfully!");
                    deleteCompleted.show();
                    allAppts.remove(selectedAppt);
                    UpdateDeleteAdd.deleteAppt(selectedAppt.getApptId());
                }
        }
    }

    /** When the Update button is pressed, the UpdateApptForm will pop up for the user to update the existing customer's information.
     If the user selected Update without pressing a customer, an alert will pop up to let the user know that an
     appointment needs to be selected before continuing. */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateApptForm.fxml"));
            loader.load();

            UpdateApptForm UAFController = loader.getController();
            UAFController.selectedAppt(apptTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException | ParseException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an appointment to update!");
            alert.showAndWait();
        }

    }

/** This initializes the table with the correct data for each column.
    This will also fill the ComboBoxes with its correct data. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptTableView.setItems(allAppts);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        showAllRbtn.setSelected(true);

        ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
        selectContactCombo.setItems(allContacts);
        totalContactCombo.setItems(allContacts);

        for (Appointments apptType : allAppts) {
            ObservableList<String> stringType = apptType.toStringType();
            typeCombo.setItems(stringType);
        }
        for (Appointments apptMonth : allAppts) {
            ObservableList<String> stringMonth = apptMonth.toStringMonths();
            monthCombo.setItems(stringMonth);
        }
    }
}
