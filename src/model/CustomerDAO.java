package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    public static ObservableList<Customer> getCustomers(Connection connection) throws SQLException {
        String sqlCommand = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division from customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        ObservableList<Customer> maintainCustomers = FXCollections.observableArrayList();
        while (results.next()) {
            int customerID = results.getInt("Customer_ID");
            String customerName = results.getString("Customer_Name");
            String customerAddress = results.getString("Address");
            String customerPostal = results.getString("Postal_Code");
            String customerPhoneNumber = results.getString("Phone");
            int divisionID = results.getInt("Division_ID");
            String division = results.getString("Division");
            Customer newCustomer = new Customer(customerID, customerName, customerAddress, customerPostal, customerPhoneNumber, divisionID, division);
            maintainCustomers.add(newCustomer);
        }
        return maintainCustomers;
    }

    public static Boolean deleteCustomer(Integer customerID) throws SQLException {
        PreparedStatement sql = DBConnect.openConnection().prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
        sql.setInt(1, customerID);
        try {
            sql.executeUpdate();
            sql.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ObservableList<Integer> getEveryCustomerID() throws SQLException {
        ObservableList<Integer> everyCustomerID = FXCollections.observableArrayList();
        PreparedStatement sql = DBConnect.getConnection().prepareStatement("SELECT DISTINCT Customer_ID FROM customers ORDER BY Customer_ID ASC;");
        ResultSet result = sql.executeQuery();

        while (result.next()) {
            everyCustomerID.add(result.getInt("Customer_ID"));
        }
        sql.close();
        return everyCustomerID;
    }

}
