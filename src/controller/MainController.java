package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Welcome to the Client Scheduler! Hope you have a great experience!");
    }

    public void mainViewAppointmentsButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Scene newScene = new Scene(root);
        Stage goToAppointments = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        goToAppointments.setScene(newScene);
        goToAppointments.show();
        goToAppointments.centerOnScreen();
    }

    public void mainViewCustomerButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
        Scene newScene = new Scene(root);
        Stage goToCustomer = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        goToCustomer.setScene(newScene);
        goToCustomer.show();
        goToCustomer.centerOnScreen();
    }

    public void mainViewReportsButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Scene newScene = new Scene(root);
        Stage goToReports = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        goToReports.setScene(newScene);
        goToReports.show();
        goToReports.centerOnScreen();
    }

    public void mainExitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit this program?");
        Optional<ButtonType> validate = alert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            exitProgram.close();
        }
    }

    public void logOutButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Proceed to Logout");
        alert.setContentText("Log out of this program?");
        Optional<ButtonType> validate = alert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene newScene = new Scene(root);
            Stage goToLogin = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            goToLogin.setScene(newScene);
            goToLogin.show();
            goToLogin.centerOnScreen();
        }
    }
}
