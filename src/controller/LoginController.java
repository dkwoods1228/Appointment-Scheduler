package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDAO;
import model.User;
import model.UserDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController {
    public TextField usernameLogin;
    public TextField passwordLogin;
    public Button loginButton;
    public Button exitButton;
    public TextField loginTimeZone;

    public void loginButtonClicked(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            LocalDateTime subtract15 = LocalDateTime.now().minusMinutes(15);
            LocalDateTime add15 = LocalDateTime.now().plusMinutes(15);
            LocalDateTime enterProgram;
            boolean notifyAppoint15 = false;
            LocalDateTime showTime = null;
            int maintainAppointID = 0;

            ResourceBundle resource = ResourceBundle.getBundle("language/login", Locale.getDefault());
            String user = usernameLogin.getText();
            String pass = passwordLogin.getText();
            int userID = UserDAO.confirmUserLogin(user, pass);

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

                printFile.print("There was a login by " + user + " at " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                for (Appointment appoint: maintainAppointments) {
                    enterProgram = appoint.getStart();
                    if ((enterProgram.isAfter(subtract15))
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
}
