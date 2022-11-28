package Model;

import DAO.DBAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** This is the Appointments class that stores the data for appointments from the mySQL database. */
public class Appointments {
    private int apptId;
    private String apptTitle;
    private String apptDesc;
    private String apptLocation;
    public String contactName;
    private String apptType;
    private String apptStart;
    private String apptEnd;
    public int customerId;
    public int userId;

    public Appointments(int apptId, String apptTitle, String apptDesc, String apptLocation, String contactName, String apptType,
                        String apptStart, String apptEnd, int customerId, int userId) {
        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.contactName = contactName;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.customerId = customerId;
        this.userId = userId;
    }

    /**@return appointment ID */
    public int getApptId() {
        return apptId;
    }

    /**@param apptId appointment ID to set */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**@return appointment Title */
    public String getApptTitle() {
        return apptTitle;
    }

    /**@param apptTitle appointment title to set */
    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    /**@return appointment description */
    public String getApptDesc() {
        return apptDesc;
    }

    /** @param apptDesc appointment description to set */
    public void setApptDesc(String apptDesc) {
        this.apptDesc = apptDesc;
    }

    /** @return appointment location */
    public String getApptLocation() {
        return apptLocation;
    }

    /**@param apptLocation appointment location to set */
    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    /**@return appointment's contact name*/
    public String getContactName() {
        return contactName;
    }

    /**@param contactId contactID to set */
    public void setContactName(int contactId) {
        this.contactName = contactName;
    }

    /**@return appointment type*/
    public String getApptType() {
        return apptType;
    }

    /**@param apptType appointment type to set */
    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    /**@return appointment start time and date */
    public String getApptStart() {
        return apptStart;
    }

    /**@param apptStart appointment start time and date to set */
    public void setApptStart(String apptStart) {
        this.apptStart = apptStart;
    }

    /**@return appointment end time and date */
    public String getApptEnd() {
        return apptEnd;
    }
    /**@param apptEnd appointment end time and date to set */
    public void setApptEnd(String apptEnd) {
        this.apptEnd = apptEnd;
    }

    /**@return customerID*/
    public int getCustomerId() {
        return customerId;
    }
    /**@param customerId customerId to set*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**@return userId*/
    public int getUserId() {
        return userId;
    }

    /**@param userId userId to set*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**@return apartment start time and date into a string. */
    public String toString() {
        return apptStart;
    }

    /**@return apartment end time and date into a string. */
    public String toString2() {
        return apptEnd;
    }

/** This method provides an ObservableList of all types of appointments. This code is created to ensure that
 there are no duplicate type names.
 @return all appointment types that are not duplicated. */
    public ObservableList<String> toStringType () {
        ObservableList<Appointments> allAppts = DBAppointments.getAllAppts();
        ObservableList<String> allApptType = FXCollections.observableArrayList();
        for(Appointments appt : allAppts) {
            String apptType = appt.getApptType();
            if(!allApptType.contains(apptType)) {
                allApptType.add(apptType);
            }
        }
        return allApptType;
    }

    /** This method provides an ObservableList of all of the appointments months. This code is created to ensure that
     there are no duplicate month names.
     @return all months that are not duplicated. */
    public ObservableList<String> toStringMonths () {
        ObservableList<Appointments> allAppts = DBAppointments.getAllAppts();
        ObservableList<String> allApptMonths = FXCollections.observableArrayList();
        for (Appointments appt : allAppts) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMMM");
            String selectMonth = appt.getApptStart();
            LocalDateTime startOfAppt = LocalDateTime.parse(selectMonth, format);
            String stringMonth = startOfAppt.format(monthFormat);

            if(!allApptMonths.contains(stringMonth)){
                allApptMonths.add(stringMonth);
            }
        }
        return allApptMonths;
    }
}
