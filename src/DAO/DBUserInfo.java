package DAO;

import Model.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This is the DBuserInfo class that retrieves all user information in MySQL. */

public class DBUserInfo {

    /**Retrieves all user information from MySQl.
     @return gets all of the users IDs, names, and passwords. */
    public static ObservableList<UserInfo> getAllUserInfo() {

        ObservableList <UserInfo> usersList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");
                UserInfo users = new UserInfo (userId, userName, userPassword);
                usersList.add(users);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usersList;
    }

    /** This method finds all existing user IDs.
     This helps code the AddApptForm and UpdateApptForm controller.
     @return finds all existing user IDs. */
    public static Boolean getExisitingUserId(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }
}
