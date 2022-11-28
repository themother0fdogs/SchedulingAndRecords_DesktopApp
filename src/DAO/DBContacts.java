package DAO;

import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This is the DBContacts class that retrieves contacts from MySQL. */
public class DBContacts {

    /**Retrieves all contacts from MySQl.
     @return retrieves all contact's IDs, names, and emails. */
    public static ObservableList<Contacts> getAllContacts() {

        ObservableList <Contacts> contactList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contacts contact = new Contacts (contactId, contactName, contactEmail);
                contactList.add(contact);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactList;
    }
}
