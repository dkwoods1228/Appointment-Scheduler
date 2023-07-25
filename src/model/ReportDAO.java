package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
/**Class used to obtain data from multiple different tables within the database.*/
public class ReportDAO extends Appointment {
    public ReportDAO(int appointID, String appointTitle, String appointDescription, String appointLocation, int contactID, String appointType, LocalDateTime start, LocalDateTime end, int appointCustomerID, int userID) {
        super(appointID, appointTitle, appointDescription, appointLocation, contactID, appointType, start, end, appointCustomerID, userID);
    }

    /**
     *Observable List used to pull/manipulate countries data from the database.
     * @return maintainCountries
     * @throws SQLException
     */
    public static ObservableList<Report> getCountries() throws SQLException {
        ObservableList<Report> maintainCountries = FXCollections.observableArrayList();
        String sqlCommand = "SELECT countries.Country, count(*) as countryTotal from customers inner join first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID inner join countries on countries.Country_ID = first_level_divisions.Country_ID where customers.Division_ID = first_level_divisions.Division_ID group by first_level_divisions.Country_ID order by count(*) desc";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            String country = results.getString("Country");
            int countryTotal = results.getInt("countryTotal");
            Report newReport = new Report(country, countryTotal);
            maintainCountries.add(newReport);
        }
        return maintainCountries;
    }
}
