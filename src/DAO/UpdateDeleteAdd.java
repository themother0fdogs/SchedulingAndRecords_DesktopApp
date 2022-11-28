package DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/** This is the UpdateDeleteAdd class that assists in updating, deleting, and adding information in MySQL. */
public abstract class UpdateDeleteAdd {

        /** This method updates appointments in MySQL. */
    public static void updateAppt(String apptTitle, String apptDesc, String apptLocation, String apptType,
                              String apptStart, String apptEnd, int apptCustomerId, int apptUserId, int apptContact, int apptId) throws SQLException {
            String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, apptTitle);
            ps.setString(2, apptDesc);
            ps.setString(3, apptLocation);
            ps.setString(4, apptType);
            ps.setString(5, apptStart);
            ps.setString(6, apptEnd);
            ps.setInt(7, apptCustomerId);
            ps.setInt(8, apptUserId);
            ps.setInt(9, apptContact);
            ps.setInt(10, apptId);
            ps.executeUpdate();
    }

        /** This method adds appointments in MySQL. */
    public static void addAppt(String apptTitle, String apptDesc, String apptLocation, String apptType,
                               String apptStart, String apptEnd, int apptCustomerId, int apptUserId, int apptContact) throws SQLException {
            String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, apptTitle);
            ps.setString(2, apptDesc);
            ps.setString(3, apptLocation);
            ps.setString(4, apptType);
            ps.setString(5, apptStart);
            ps.setString(6, apptEnd);
            ps.setInt(7, apptCustomerId);
            ps.setInt(8, apptUserId);
            ps.setInt(9, apptContact);
            ps.executeUpdate();
    }

        /** This method deletes appointments in MySQL. */
    public static void deleteAppt(int apptId) throws SQLException {
            String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, apptId);
            ps.executeUpdate();
        }

        /** This method updates customer records in MySQL. */
    public static void updateCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhone,  int customerDivisionId, int customerId) throws SQLException {
            String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setInt(5, customerDivisionId);
            ps.setInt(6, customerId);
            ps.executeUpdate();
    }

        /** This method adds customer records in MySQL. */
    public static void addCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhone,  int customerDivisionId) throws SQLException {
            String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setInt(5, customerDivisionId);
            ps.executeUpdate();
    }

        /** This method deletes customer records in MySQL. */
    public static void deleteCustomer(int customerId) throws SQLException {
            String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.executeUpdate();
    }
}
