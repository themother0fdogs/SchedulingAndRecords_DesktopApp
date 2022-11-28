package Controller;

import DAO.*;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** This is the AddApptForm class that works with the AddApptForm FXML and allows users to add new appointments. */
public class AddApptForm implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private DatePicker addEndDate;
    @FXML
    private DatePicker addStartDate;
    @FXML
    private ComboBox<Contacts> addApptContactCombo;
    @FXML
    private ComboBox<String> addEndTimeCombo;
    @FXML
    private ComboBox<String> addStartTimeCombo;
    @FXML
    private TextField addApptDescTxt;
    @FXML
    private TextField addApptIdTxt;
    @FXML
    private TextField addApptLocTxt;
    @FXML
    private TextField addApptTitleTxt;
    @FXML
    private TextField addApptTypeTxt;
    @FXML
    private TextField addCustomerIdTxt;
    @FXML
    private TextField addUserIdTxt;

/** When a user clicks the Cancel button, it will return the user back to the Customer Appointment Form.
    @param event returns the user back to the Customer Appointments screen. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/CustomerApptForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
            String start = addStartDate.getValue() + " " + addStartTimeCombo.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(localZoneID);
            LocalDateTime startLDT = LocalDateTime.parse(start, formatter);
            ZonedDateTime startZDT = ZonedDateTime.of(startLDT, localZoneID);
            DateTimeFormatter utcFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(utcZoneID);
            Instant startToUTC = startZDT.toInstant();
            String apptStart = utcFormat.format(startToUTC);

            String end = addEndDate.getValue() + " " + addEndTimeCombo.getValue();
            LocalDateTime endLDT = LocalDateTime.parse(end, formatter);
            ZonedDateTime endZDT = ZonedDateTime.of(endLDT, localZoneID);
            Instant endToUTC = endZDT.toInstant();
            String apptEnd = utcFormat.format(endToUTC);

            String apptTitle = addApptTitleTxt.getText();
            String apptDesc = addApptDescTxt.getText();
            String apptLocation = addApptLocTxt.getText();
            String apptType = addApptTypeTxt.getText();

            int apptCustomerId = Integer.parseInt(addCustomerIdTxt.getText());
            int apptUserId = Integer.parseInt(addUserIdTxt.getText());
            int apptContact = addApptContactCombo.getValue().getContactId();

            if (!DBCustomers.getExistingCustomerId(apptCustomerId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CustomerID does not exist!\nPlease input a valid CustomerID.");
                alert.showAndWait();
                return;
            }
            if (!DBUserInfo.getExisitingUserId(apptUserId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("UserID does not exist!\nPlease input a valid UserID.");
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

                if (startDT.isAfter(allStartDT) && startDT.isBefore(allEndDT) || endDT.isAfter(allStartDT) && endDT.isBefore(allEndDT)
                        || startDT.isEqual(allStartDT) || startDT.isEqual(allEndDT) || endDT.isEqual(allStartDT) || endDT.isEqual(allEndDT)) {
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
                result += (type.length() > 1 ? type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() : type) + " ";
                String finalType = result.trim();

                UpdateDeleteAdd.addAppt(apptTitle, apptDesc, apptLocation, finalType,
                        apptStart, apptEnd, apptCustomerId, apptUserId, apptContact);
            }
        }
        catch(SQLException | NumberFormatException | NullPointerException e) {
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
        addApptContactCombo.setItems(allContacts);

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
        addStartTimeCombo.setItems(apptTime);
        addEndTimeCombo.setItems(apptTime);
    }
}
