package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentDAO {
    public static ObservableList<Appointment> getAppointments() throws SQLException {
        ObservableList<Appointment> maintainAppointments = FXCollections.observableArrayList();
        String sqlCommand = "SELECT * from appointments";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet result = prepare.executeQuery();
        while (result.next()) {
            int appointID = result.getInt("Appointment_ID");
            String appointTitle = result.getString("Title");
            String appointDescription = result.getString("Description");
            String appointLocation = result.getString("Location");
            int contactID = result.getInt("Contact_ID");
            String appointType = result.getString("Type");
            LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
            int appointCustomerID = result.getInt("Customer_ID");
            int userID = result.getInt("User_ID");
            Appointment newAppoint = new Appointment(appointID, appointTitle, appointDescription, appointLocation, contactID,
                    appointType, start, end, appointCustomerID, userID);
            maintainAppointments.add(newAppoint);
        }
        return maintainAppointments;
    }

    public static int deleteAppoint(int customer, Connection connection) throws SQLException {
        String sqlCommand = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement prepare = connection.prepareStatement(sqlCommand);
        prepare.setInt(1, customer);
        int outcome = prepare.executeUpdate();
        prepare.close();
        return outcome;
    }

    public static Boolean deleteAppointAndCustomer(Integer customerID) throws SQLException {
        PreparedStatement sql = DBConnect.getConnection().prepareStatement("DELETE FROM appointments WHERE Customer_ID = ?");
        sql.setInt(1, customerID);
        try {
            sql.executeUpdate();
            sql.close();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}