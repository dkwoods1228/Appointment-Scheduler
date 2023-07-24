package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends User {
    private static User loggedIn;
    public UserDAO(int userID, String username, String password) {
        super();

    }

    public static boolean confirmUserLogin(String user, String pass) throws SQLException {
        Connection connection = DBConnect.getConnection();
        PreparedStatement prepare = connection.prepareStatement("SELECT * FROM users WHERE " + "User_Name = ? AND Password = ?");
        prepare.setString(1, user);
        prepare.setString(2, pass);
        ResultSet results = prepare.executeQuery();
        if (!results.next()) {
            prepare.close();
            return false;
        } else {
            loggedIn = new UserDAO(results.getInt("User_ID"), results.getString("User_Name"), results.getString("Password"));
            prepare.close();
            return true;
        }
    }



    public static ObservableList<UserDAO> getUsers() throws SQLException {
        ObservableList<UserDAO> maintainUsers = FXCollections.observableArrayList();
        String sqlCommand = "SELECT * from users";
        PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
        ResultSet results = prepare.executeQuery();
        while (results.next()) {
            int userID = results.getInt("User_ID");
            String username = results.getString("User_Name");
            String password = results.getString("Password");
            UserDAO newUser = new UserDAO(userID, username, password);
            maintainUsers.add(newUser);
        }
        return maintainUsers;
    }

    public static ObservableList<Integer> getEveryUserID() throws SQLException {
        ObservableList<Integer> everyUserID = FXCollections.observableArrayList();
        PreparedStatement sql = DBConnect.getConnection().prepareStatement("SELECT DISTINCT User_ID FROM users;");
        ResultSet result = sql.executeQuery();

        while (result.next()) {
            everyUserID.add(result.getInt("User_ID"));
        }
        sql.close();
        return everyUserID;
    }
}
