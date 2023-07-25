package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**Class used to manipulate/obtain data from Contacts table within the database.*/
public class ContactDAO {
    /**
     * Observable List used to pull/manipulate contact data from the database.
     * @return maintainContacts
     * @throws SQLException
     */
    public static ObservableList<Contact> getContacts() throws SQLException {
        ObservableList<Contact> maintainContacts = FXCollections.observableArrayList();
        String sqlCommand = "SELECT * from contacts";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            int contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");
            String contactEM = results.getString("Email");
            Contact newContact = new Contact(contactID, contactName, contactEM);
            maintainContacts.add(newContact);
        }
        return maintainContacts;
    }

    /**
     * Used to fill contact ComboBox and match contact IDs to contact names.
     * @param contactID
     * @return contactID
     * @throws SQLException
     */
    public static String tryContactID(String contactID) throws SQLException {
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement("SELECT * FROM contacts WHERE Contact_Name = ?");
        prepare.setString(1, contactID);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            contactID = results.getString("Contact_ID");
        }
        return contactID;
    }
}
