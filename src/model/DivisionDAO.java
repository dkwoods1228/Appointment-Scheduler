package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**Class used to manipulate/obtain data from First_Level_Divisions table within the database.*/
public class DivisionDAO extends Division {
    public DivisionDAO(int division_ID, String division, int country_ID) {
        super(division_ID, division, country_ID);
    }

    /**
     * Observable List used to pull/manipulate division data from the database.
     * @return maintainDivisions
     * @throws SQLException
     */
    public static ObservableList<DivisionDAO> getDivisions() throws SQLException {
        ObservableList<DivisionDAO> maintainDivisions = FXCollections.observableArrayList();
        String sqlCommand = "SELECT * from first_level_divisions";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            int divisionID = results.getInt("Division_ID");
            String division = results.getString("Division");
            int countryID = results.getInt("Country_ID");
            DivisionDAO newDivision = new DivisionDAO(divisionID, division, countryID);
            maintainDivisions.add(newDivision);
        }
        return maintainDivisions;
    }
}
