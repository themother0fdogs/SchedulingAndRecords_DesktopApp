package DAO;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This is the DBCountries class that retrieves all countries in MySQL. */
public class DBCountries {

    /**Retrieves all countries from MySQl.
      @return gets all of the country ID and country name. */
    public static ObservableList <Countries> getAllCountries() {

        ObservableList <Countries> countryList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM countries";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                Countries Country = new Countries(countryId, countryName);
                countryList.add(Country);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryList;
    }
}
