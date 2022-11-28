package DAO;

import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/** This is the DBAppointments class that retrieves appointments from MySQL. */
public class DBAppointments {

    /** This gets all appointments from MySQL.
     The current database is in UTC time, so the code below is retrieving the appointment start and end times
     and converting them to the user's default time zone.
      @return gets all of the appointment's ID, title, description, location, contact's name,
     type, start and end times, and the customer and user's ID. */
    public static ObservableList<Appointments> getAllAppts() {

        ObservableList<Appointments> apptList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM appointments LEFT JOIN contacts ON appointments.Contact_Id = contacts.Contact_Id";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int apptId = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDesc = rs.getString("Description");
                String apptLocation = rs.getString("Location");
                String contactName = rs.getString("Contact_Name");
                String apptType = rs.getString("Type");
                String apptStartUTC = rs.getString("Start");
                String apptEndUTC = rs.getString("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                ZoneId utcZoneID = ZoneId.of("UTC");
                ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(utcZoneID);
                LocalDateTime startLZ = LocalDateTime.parse(apptStartUTC, formatter);
                ZonedDateTime startDefaultZDT = ZonedDateTime.of(startLZ ,utcZoneID);
                DateTimeFormatter utcFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(localZoneID);
                String apptStart = utcFormat.format(startDefaultZDT);

                LocalDateTime endLZ = LocalDateTime.parse(apptEndUTC, formatter);
                ZonedDateTime endDefaultZDT = ZonedDateTime.of(endLZ, utcZoneID);
                String apptEnd = utcFormat.format(endDefaultZDT);

                Appointments appts = new Appointments(apptId, apptTitle, apptDesc, apptLocation, contactName, apptType,
                        apptStart, apptEnd, customerId, userId);
                apptList.add(appts);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return apptList;
    }

    /** Retrieves all existing appointments based on the customer's ID.
     This helps code the CustomerRecordsForm controller.
     @return goes through all appointments to find the all the ones matching the customer's ID. */
    public static Boolean getExistingAppts(int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
