package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Appointment;

public class AppointmentController {
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
    }

    @FXML void deleteButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void cancelButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void returnToMainMenuButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void appointWeekRadioButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void appointMonthRadioButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void allAppointRadioButtonClicked(ActionEvent actionEvent) {
    }
}
