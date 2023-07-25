package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**Class used to manipulate/obtain data from Countries table within the database.*/
public class CountryDAO extends Countries {
    public CountryDAO(int countryID, String country) {
        super(countryID, country);
    }

    /**
     * Observable List used to pull/manipulate country data from the database.
     * @return maintainCountries
     * @throws SQLException
     */
    public static ObservableList<CountryDAO> getCountries() throws SQLException {
        ObservableList<CountryDAO> maintainCountries = FXCollections.observableArrayList();
        String sqlCommand = "SELECT Country_ID, Country from countries";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            int countryID = results.getInt("Country_ID");
            String country = results.getString("Country");
            CountryDAO newCountry = new CountryDAO(countryID, country);
            maintainCountries.add(newCountry);
        }
        return maintainCountries;
    }
}
