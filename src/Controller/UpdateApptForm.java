package Controller;

import DAO.*;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** This is the UpdateApptForm class that works with the UpdateApptForm FXML and allows the user to update an appointment */
public class UpdateApptForm implements Initializable {
    Stage stage;
    Parent scene;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private DatePicker endDate;
    @FXML
    private DatePicker startDate;
    @FXML
    private ComboBox<Contacts> updateApptContactCombo;
    @FXML
    private ComboBox<String> updateEndTimeCombo;
    @FXML
    private ComboBox<String> updateStartTimeCombo;
    @FXML
    private TextField updateApptIdTxt;
    @FXML
    private TextField updateApptTitleTxt;
    @FXML
    private TextField updateApptDescTxt;
    @FXML
    private TextField updateApptLocTxt;
    @FXML
    private TextField updateApptTypeTxt;
    @FXML
    private TextField updateCustomerIdTxt;
    @FXML
    private TextField updateUserIdTxt;

/** This retrieves the customer's contact.
 @param customerContact searches for the customer's contact by name.
 @return returns the customer's contact. */
    private Contacts getCustomerContact (String customerContact) {
        ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
        Contacts returnContacts = null;
        for (int i = 0; i < allContacts.size(); i++) {
            Contacts contact = allContacts.get(i);
            if (!contact.getContactName().equals(customerContact)) {
                continue;
            }
            else {
                returnContacts = contact;
                break;
            }
        }
        return returnContacts;
    }

    /** This retrieves the customer's Appointment start time and date.
     @param selectedAppt searches for customer's appointment start time and date.
     @return returns the customer's end appointment time and date.*/
    private Appointments getSelectedStartAppt (String selectedAppt) {
        ObservableList<Appointments> allAppts = DBAppointments.getAllAppts();
        Appointments returnAppts = null;
        for (int i = 0; i < allAppts.size(); i++) {
            Appointments appts = allAppts.get(i);
            if (!appts.getApptStart().equals(selectedAppt)) {
                continue;
            }
            else {
                returnAppts = appts;
                break;
            }
        }
        return returnAppts;
    }

    /** This retrieves the customer's Appointment end time and date.
     @param selectedAppt searches for customer's appointment end time and date.
     @return returns the customer's end appointment time and date.*/
    private Appointments getSelectedEndAppt (String selectedAppt) {
        ObservableList<Appointments> allAppts = DBAppointments.getAllAppts();
        Appointments returnAppts = null;
        for (int i = 0; i < allAppts.size(); i++) {
            Appointments appts = allAppts.get(i);
            if (!appts.getApptEnd().equals(selectedAppt)) {
                continue;
            }
            else {
                returnAppts = appts;
                break;
            }
        }
        return returnAppts;
    }

    /**This populate the form with the selected Customer's data.
     @param appts finds the selected customer's appointment.  */
    public void selectedAppt (Appointments appts) throws ParseException {
        updateApptIdTxt.setText(String.valueOf(appts.getApptId()));
        updateApptTitleTxt.setText(appts.getApptTitle());
        updateApptDescTxt.setText(appts.getApptDesc());
        updateApptLocTxt.setText(appts.getApptLocation());
        updateApptTypeTxt.setText(appts.getApptType());
        updateCustomerIdTxt.setText(String.valueOf(appts.getCustomerId()));
        updateUserIdTxt.setText(String.valueOf(appts.getUserId()));

        Contacts selectedContact = getCustomerContact(appts.getContactName());
        updateApptContactCombo.setValue(selectedContact);

        Appointments selectStart = getSelectedStartAppt(appts.getApptStart());
        String startAppt = selectStart.toString();
        LocalDateTime startOfAppt = LocalDateTime.parse(startAppt, format);
        startDate.setValue(LocalDate.parse(startOfAppt.format(dateFormat)));
        updateStartTimeCombo.setValue(startOfAppt.format(timeFormat));

        Appointments selectEnd = getSelectedEndAppt(appts.getApptEnd());
        String endAppt = selectEnd.toString2();
        LocalDateTime endOfAppt = LocalDateTime.parse(endAppt, format);
        endDate.setValue(LocalDate.parse(endOfAppt.format(dateFormat)));
        updateEndTimeCombo.setValue(endOfAppt.format(timeFormat));
    }

    /** When the Cancel button is pressed, the user will be returned to the Customer Appointments Form and no data is saved. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerApptForm.fxml"));
        stage.setScene(new Scene(scene));stage.show();
    }

    /** When a user clicks the Save button, it will save the user's input into the Customer Appointment Form.
    While the Customer Appointment Form is updated, MySQL will be updated using the UTC timezone.
    I have also created error codes if a user inputs a UserID or CustomerID that does not exist,
    appointment time overlaps, if there is missing information, and if a user chooses the a start time after the end time.
    Another thing that I included was to capitalize the first letter of every word in Type even if the user
    were to write in all lowercase. I did this to improve the user experience for the Type ComboBox in the Customer Appointment Form.
    @param event saves data into MySQL database */
    @FXML
    void onActionSaveAppt(ActionEvent event) throws IOException {
        try {
            ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
            ZoneId utcZoneID = ZoneId.of("UTC");
            String start = startDate.getValue() + " " + updateStartTimeCombo.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(localZoneID);
            LocalDateTime startLDT = LocalDateTime.parse(start, formatter);
            ZonedDateTime startZDT = ZonedDateTime.of(startLDT, localZoneID);
            DateTimeFormatter utcFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(utcZoneID);
            Instant startToUTC = startZDT.toInstant();
            String apptStart = utcFormat.format(startToUTC);

            String end = endDate.getValue() + " " + updateEndTimeCombo.getValue();
            LocalDateTime endLDT = LocalDateTime.parse(end, formatter);
            ZonedDateTime endZDT = ZonedDateTime.of(endLDT, localZoneID);
            Instant endToUTC = endZDT.toInstant();
            String apptEnd = utcFormat.format(endToUTC);

            int apptId = Integer.parseInt(updateApptIdTxt.getText());
            String apptTitle = updateApptTitleTxt.getText();
            String apptDesc = updateApptDescTxt.getText();
            String apptLocation = updateApptLocTxt.getText();
            String apptType = updateApptTypeTxt.getText();
            int customerId = Integer.parseInt(updateCustomerIdTxt.getText());
            int userId = Integer.parseInt(updateUserIdTxt.getText());
            int contactId = updateApptContactCombo.getValue().getContactId();

            if (!DBCustomers.getExistingCustomerId(customerId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CustomerID does not exist!\nPlease input a valid CustomerID.");
                alert.showAndWait();
                return;
            }

            if (!DBUserInfo.getExisitingUserId(userId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("UserID does not exist!\nPlease input a valid UserID.");
                alert.showAndWait();
                return;
            }

            if(apptTitle.isEmpty() || apptDesc.isEmpty() || apptLocation.isEmpty() || apptType.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
                alert.showAndWait();
                return;
            }

            for(Appointments allAppts : DBAppointments.getAllAppts()) {
                LocalDateTime startDT = LocalDateTime.parse(start, formatter);
                LocalDateTime endDT = LocalDateTime.parse(apptEnd, formatter);
                String allApptDTString = allAppts.getApptStart();
                String allEndDTString = allAppts.getApptEnd();
                LocalDateTime allStartDT = LocalDateTime.parse(allApptDTString, formatter);
                LocalDateTime allEndDT = LocalDateTime.parse(allEndDTString, formatter);

                if(startDT.isAfter(allStartDT) && startDT.isBefore(allEndDT) || endDT.isAfter(allStartDT) && endDT.isBefore(allEndDT)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Selected times are overlapping existing appointment times. \nPlease select a different appointment time.");
                    alert.showAndWait();
                    return;
                }

                if(startDT.isAfter(endDT)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("The start time selected is after the end time. \nPlease select a start time before the end time.");
                    alert.showAndWait();
                    return;
                }
            }

            String[] splitType = apptType.split(" ");
            String result = " ";
            for(String type: splitType) {
                result += (type.length() > 1? type.substring(0,1).toUpperCase() + type.substring(1).toLowerCase() : type) + " ";
                String finalType = result.trim();

                        UpdateDeleteAdd.updateAppt(apptTitle, apptDesc, apptLocation, finalType, apptStart, apptEnd, customerId, userId, contactId, apptId);
            }
        }
        catch(SQLException | NumberFormatException | NullPointerException e ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Some information is missing!\nPlease fill out the form completely.");
            alert.showAndWait();
            return;
        }

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerApptForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This initializes the Contact ComboBox and the ComboBoxes for Start and End Times.
    The Contact ComboBox pulls the information from all of the contacts from database.
    The Start and End Time ComboBoxes automatically fill with times based on the user's time zone of
    the Eastern Times between 0800 and 2200, 7 days a week. This way, the ComboBox times will always be adjusted correctly within business hours.
    The time ComboBoxes are in 15 minute increments to improve the user experience and to provide more options since
    appointment times can vary based on what type it is. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
        updateApptContactCombo.setItems(allContacts);

        LocalDate currentDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(8, 0, 0);
        LocalTime endTime = LocalTime.of(22, 0, 0);
        ZoneId easternTime = ZoneId.of("US/Eastern");
        ZonedDateTime startEastern = ZonedDateTime.of(currentDate, startTime, easternTime);
        ZonedDateTime endEastern = ZonedDateTime.of(currentDate, endTime, easternTime);

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime startEasternToLocal = startEastern.withZoneSameInstant(localZoneId);
        ZonedDateTime endEasternToLocal = endEastern.withZoneSameInstant(localZoneId);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        ObservableList<String> apptTime = FXCollections.observableArrayList();
        do{
            apptTime.add((startEasternToLocal.format(timeFormat)));
            startEasternToLocal = startEasternToLocal.plusMinutes(15);
        }
        while(startEasternToLocal.isBefore(endEasternToLocal) || startEasternToLocal.isEqual(endEasternToLocal));
        updateStartTimeCombo.setItems(apptTime);
        updateEndTimeCombo.setItems(apptTime);
    }
}
