package model;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends User {
    public UserDAO(int userID, String username, String password) {
        super();
    }

    public static int confirmUserLogin(String userName, String passWord) throws SQLException
    {
        try
        {
            String sqlCommand = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + passWord +"'";
            PreparedStatement prepare = DBConnect.getConnection().prepareStatement(sqlCommand);
            ResultSet results = prepare.executeQuery();
            results.next();
            if (results.getString("User_Name").equals(userName)) {
                if (results.getString("Password").equals(passWord)) {
                    return results.getInt("User_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
}
