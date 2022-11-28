package DAO;

import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This is the DBCustomers class that retrieves the customer's record information. */
public class DBCustomers {

    /** Retrieves all customers from MySQL.
     * @return gets all customers IDs, names, phone numbers, addresses, divisions, postal code, and country. */
    public static ObservableList<Customers> getAllCustomers() {

        ObservableList <Customers> customerList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM customers LEFT JOIN first_level_divisions ON first_level_divisions.Division_ID = customers.Division_ID LEFT JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID ";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerPhone = rs.getString("Phone");
                String customerAddress = rs.getString("Address");
                String customerDivision = rs.getString("Division");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerCountry = rs.getString("Country");

                Customers customer = new Customers (customerId, customerName, customerPhone, customerAddress, customerDivision, customerPostalCode, customerCountry);
                customerList.add(customer);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /** This method finds all existing customer IDs.
      This helps code the AddApptForm and UpdateApptForm controller.
           @return finds all existing customer IDs. */
    public static Boolean getExistingCustomerId(int id) throws SQLException {
            String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
        return rs.next();
    }

}
