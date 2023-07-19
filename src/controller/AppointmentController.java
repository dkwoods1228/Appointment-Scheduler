package controller;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AppointmentController {
    public Button exitButton;
    public ComboBox<String> updateAppointStartTime;
    public DatePicker updateAppointStartDate;
    public DatePicker updateAppointEndDate;
    public ComboBox<String> updateAppointEndTime;
    @FXML private TextField updateAppointID;
    @FXML private TextField updateAppointTitle;
    @FXML private TextField updateAppointDescription;
    @FXML private TextField updateAppointLocation;
    @FXML private ComboBox<String> updateAppointContact;
    @FXML private ComboBox<String> appointStartTime;
    @FXML private DatePicker appointStartDate;
    @FXML private DatePicker appointEndDate;
    @FXML private ComboBox<String> appointEndTime;
    @FXML private TextField updateAppointType;
    @FXML private TextField updateAppointCustomerID;
    @FXML private TextField updateAppointUserID;
    @FXML private Button updateSaveAppointmentButton;
    @FXML private Button cancelButton;
    @FXML private Button addAppointButton;
    @FXML private Button deleteButton;
    @FXML private Button returnToMainMenuButton;
    @FXML private ToggleGroup toggle;
    @FXML private RadioButton appointWeekRadioButton;
    @FXML private RadioButton appointMonthRadioButton;
    @FXML private RadioButton allAppointRadioButton;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<?,?> appointID;
    @FXML private TableColumn<?,?> appointTitle;
    @FXML private TableColumn<?,?> appointDescription;
    @FXML private TableColumn<?,?> appointLocation;
    @FXML private TableColumn<?,?> contactID;
    @FXML private TableColumn<?,?> appointType;
    @FXML private TableColumn<?,?> appointStart;
    @FXML private TableColumn<?,?> appointEnd;
    @FXML private TableColumn<?,?> appointCustomerID;
    @FXML private TableColumn<?,?> userID;

    @FXML void addAppointButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void updateSaveButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();

            if (!updateAppointTitle.getText().isEmpty() && !updateAppointDescription.getText().isEmpty() && !updateAppointLocation.getText().isEmpty() && !updateAppointType.getText().isEmpty()) && updateAppointStartDate.getValue() != null && updateAppointEndDate.getValue() != null && !updateAppointStartTime.getValue().isEmpty() && !updateAppointEndTime.getValue().isEmpty() && !updateAppointCustomerID.getText().isEmpty()) {
                ObservableList<Customer> maintainCustomers = CustomerDAO.getCustomers(connection);
                ObservableList<Integer> maintainCustomID = FXCollections.observableArrayList();
                ObservableList<UserDAO> maintainUsers = UserDAO.getUsers();
                ObservableList<Integer> maintainUserID = FXCollections.observableArrayList();
                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();

                maintainCustomers.stream().map(Customer::getCustomerID).forEach(maintainCustomID::add);
                maintainUsers.stream().map(User::getUserID).forEach(maintainUserID::add);

                LocalDate startLocalDate = updateAppointStartDate.getValue();
                LocalDate endLocalDate = updateAppointEndDate.getValue();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

                LocalTime startLocalTime = LocalTime.parse(updateAppointStartTime.getValue(), timeFormat);
                LocalTime endLocalTime = LocalTime.parse(updateAppointEndTime.getValue(), timeFormat);

                LocalDateTime startLocalAll = LocalDateTime.of(startLocalDate, startLocalTime);
                LocalDateTime endLocalAll = LocalDateTime.of(endLocalDate,endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startLocalAll, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endLocalAll, ZoneId.systemDefault());

                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/New_York"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML void deleteButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            int delID = appointmentTable.getSelectionModel().getSelectedItem().getAppointID();
            String delType = appointmentTable.getSelectionModel().getSelectedItem().getAppointType();
            Alert delIDAndType = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete the appointment containing appointment id of " + delID + " and appointment type of " + delType + "?");
            Optional<ButtonType> validate = delIDAndType.showAndWait();
            if (validate.isPresent() && validate.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppoint(delID, connection);

                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
                appointmentTable.setItems(maintainAppointments);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML void cancelButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void returnToMainMenuButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to return to the main menu?");
        alert.showAndWait();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Scene newScene = new Scene(root);
        Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        returnToMain.setScene(newScene);
        returnToMain.show();
        returnToMain.centerOnScreen();
    }

    @FXML void appointWeekRadioButtonClicked(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            ObservableList<Appointment> maintainWeeklyAppointments = FXCollections.observableArrayList();
            LocalDateTime startWeekNow = LocalDateTime.now().minusWeeks(1);
            LocalDateTime endWeekNow = LocalDateTime.now().plusWeeks(1);

            if (maintainAppointments != null) {
                maintainAppointments.forEach(appointment -> {
                    if (appointment.getEnd().isBefore(endWeekNow) && appointment.getEnd().isAfter(startWeekNow)) {
                        maintainWeeklyAppointments.add(appointment);
                    }
                    appointmentTable.setItems(maintainWeeklyAppointments);
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
    }

    }

    @FXML void allAppointRadioButtonClicked(ActionEvent actionEvent) {
            try {
                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
                if (maintainAppointments != null)
                    for (Appointment appointment : maintainAppointments) {
                        appointmentTable.setItems(maintainAppointments);
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    @FXML void appointMonthRadioButtonClicked(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            ObservableList<Appointment> maintainMonthlyAppointments = FXCollections.observableArrayList();
            LocalDateTime startMonthNow = LocalDateTime.now().minusMonths(1);
            LocalDateTime endMonthNow = LocalDateTime.now().plusMonths(1);

            if (maintainAppointments != null) {
                maintainAppointments.forEach(appointment -> {
                    if (appointment.getEnd().isBefore(endMonthNow) && appointment.getEnd().isAfter(startMonthNow)) {
                        maintainMonthlyAppointments.add(appointment);
                    }
                    appointmentTable.setItems(maintainMonthlyAppointments);
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you would like to exit this program?");
        alert.showAndWait();
        Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        exitProgram.close();
    }
}
