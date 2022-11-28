package DAO;


import Model.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This is the DBDivisions class that retrieves the all divisions. */
public class DBDivisions {

    /**This method retrieves all divisions.
     @return gets all division IDs, names, and country IDs. */
    public static ObservableList<Divisions> getAllDivisions() {

        ObservableList <Divisions> divisionsList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM first_level_divisions LEFT JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");

                Divisions division = new Divisions (divisionId, divisionName, countryId);
                divisionsList.add(division);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionsList;
    }
}
