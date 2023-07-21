package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDAO;
import model.UserDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField usernameLogin;
    public TextField passwordLogin;
    public Button loginButton;
    public Button exitButton;
    public TextField loginTimeZone;
    public Label loginLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label timezoneLabel;

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException, IOException, Exception {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            LocalDateTime subtract15 = LocalDateTime.now().minusMinutes(15);
            LocalDateTime add15 = LocalDateTime.now().plusMinutes(15);
            LocalDateTime enterProgram;
            boolean notifyAppoint15 = false;
            LocalDateTime showTime = null;
            int maintainAppointID = 0;

            ResourceBundle resource = ResourceBundle.getBundle("loglang/login", Locale.getDefault());
            String userName = usernameLogin.getText();
            String passWord = passwordLogin.getText();
            int userID = UserDAO.confirmUserLogin(userName, passWord);

            FileWriter writeFile = new FileWriter("login_activity.txt", true);
            PrintWriter printFile = new PrintWriter(writeFile);

            if (userID > 0) {
                FXMLLoader loads = new FXMLLoader();
                loads.setLocation(getClass().getResource("/view/Main.fxml"));
                Parent login = loads.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene newScene = new Scene(login);
                stage.setScene(newScene);
                stage.show();

                printFile.print("There was a successful login by " + userName + " at " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                for (Appointment appoint : maintainAppointments) {
                    enterProgram = appoint.getStart();
                    if ((enterProgram.isAfter(subtract15) || enterProgram.isEqual(subtract15)) && (enterProgram.isBefore(add15) || (enterProgram.isEqual(add15)))) {
                        maintainAppointID = appoint.getAppointID();
                        showTime = enterProgram;
                        notifyAppoint15 = true;
                    }
                }
                if (notifyAppoint15 != false) {
                    Alert appointSoon = new Alert(Alert.AlertType.WARNING);
                    appointSoon.setTitle("Appointment Reminder");
                    appointSoon.setHeaderText("Upcoming Appointment");
                    appointSoon.setContentText("The following appointment(s) take place within the next 15 minutes.    Appointment ID:" + maintainAppointID + "Appointment Start Time: " + showTime);
                } else {
                    Alert noAppointsSoon = new Alert(Alert.AlertType.WARNING);
                    noAppointsSoon.setTitle("No Appointments");
                    noAppointsSoon.setContentText("There are no upcoming appointments.");
                }
            } else if (userID < 0) {
                Alert invalidCred = new Alert(Alert.AlertType.ERROR);
                invalidCred.setTitle(resource.getString("Error"));
                invalidCred.setTitle(resource.getString("Invalid"));
                invalidCred.show();

                printFile.print("user: " + userName + " failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }
            printFile.close();

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void exitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit this program?");
        Optional<ButtonType> validate = alert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            exitProgram.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        try {
            ZoneId getZone = ZoneId.systemDefault();
            Locale region = Locale.getDefault();
            Locale.setDefault(region);

            loginTimeZone.setText(String.valueOf(getZone));

            resource = ResourceBundle.getBundle("loglang/login", Locale.getDefault());
            loginLabel.setText(resource.getString("Login"));
            usernameLabel.setText(resource.getString("Username"));
            passwordLabel.setText(resource.getString("Password"));
            loginButton.setText(resource.getString("Login"));
            exitButton.setText(resource.getString("Exit"));
            timezoneLabel.setText(resource.getString("TimeZone"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
