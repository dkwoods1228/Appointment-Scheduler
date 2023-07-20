package controller;

import database.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static main.Timezone.timeAndDateToUTC;

public class AppointmentController implements Initializable {
    @FXML private Button updateAppointmentButton;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<String> updateAppointStartTime;
    @FXML
    private DatePicker updateAppointStartDate;
    @FXML
    private DatePicker updateAppointEndDate;
    @FXML
    private ComboBox<String> updateAppointEndTime;
    @FXML
    private TextField updateAppointID;
    @FXML
    private TextField updateAppointTitle;
    @FXML
    private TextField updateAppointDescription;
    @FXML
    private TextField updateAppointLocation;
    @FXML
    private ComboBox<String> updateAppointContact;
    @FXML
    private ComboBox<String> appointStartTime;
    @FXML
    private DatePicker appointStartDate;
    @FXML
    private DatePicker appointEndDate;
    @FXML
    private ComboBox<String> appointEndTime;
    @FXML
    private TextField updateAppointType;
    @FXML
    private TextField updateAppointCustomerID;
    @FXML
    private TextField updateAppointUserID;
    @FXML
    private Button updateSaveAppointmentButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addAppointButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button returnToMainMenuButton;
    @FXML
    private ToggleGroup toggle;
    @FXML
    private RadioButton appointWeekRadioButton;
    @FXML
    private RadioButton appointMonthRadioButton;
    @FXML
    private RadioButton allAppointRadioButton;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<?, ?> appointID;
    @FXML
    private TableColumn<?, ?> appointTitle;
    @FXML
    private TableColumn<?, ?> appointDescription;
    @FXML
    private TableColumn<?, ?> appointLocation;
    @FXML
    private TableColumn<?, ?> contactID;
    @FXML
    private TableColumn<?, ?> appointType;
    @FXML
    private TableColumn<?, ?> appointStart;
    @FXML
    private TableColumn<?, ?> appointEnd;
    @FXML
    private TableColumn<?, ?> appointCustomerID;
    @FXML
    private TableColumn<?, ?> userID;



    @FXML void updateAppointmentButtonClicked(ActionEvent actionEvent) {
        try {
            DBConnect.openConnection();
            Appointment appointClicked = appointmentTable.getSelectionModel().getSelectedItem();

            if (appointClicked == null) {
                Alert unselectedAppoint = new Alert(Alert.AlertType.ERROR);
                unselectedAppoint.setTitle("Appointment not Selected");
                unselectedAppoint.setContentText("You must select an appointment to update before continuing.");
                unselectedAppoint.showAndWait();
                return;
            }

            if (appointClicked != null) {
                ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
                ObservableList<String> contacts = FXCollections.observableArrayList();
                String showContact = "";

                maintainContacts.forEach(contact -> contacts.add(contact.getContactName()));
                updateAppointContact.setItems(contacts);

                for (Contact cont : maintainContacts) {
                    if (appointClicked.getContactID() == cont.getContactID()) {
                        showContact = cont.getContactName();
                    }
                }
                updateAppointID.setText(String.valueOf(appointClicked.getAppointID()));
                updateAppointTitle.setText(appointClicked.getAppointTitle());
                updateAppointDescription.setText(appointClicked.getAppointDescription());
                updateAppointLocation.setText(appointClicked.getAppointLocation());
                updateAppointContact.setValue(showContact);
                updateAppointType.setText(appointClicked.getAppointType());
                updateAppointStartDate.setValue(appointClicked.getStart().toLocalDate());
                updateAppointEndDate.setValue(appointClicked.getEnd().toLocalDate());
                updateAppointStartTime.setValue(String.valueOf(appointClicked.getStart().toLocalTime()));
                updateAppointEndTime.setValue(String.valueOf(appointClicked.getEnd().toLocalTime()));
                updateAppointCustomerID.setText(String.valueOf(appointClicked.getAppointCustomerID()));
                updateAppointUserID.setText(String.valueOf(appointClicked.getUserID()));

                ObservableList<String> times = FXCollections.observableArrayList();
                LocalTime min = LocalTime.MIN.plusHours(8);
                LocalTime max = LocalTime.MAX.minusHours(1).minusMinutes(45);

                if (!min.equals(0) || !max.equals(0)) {
                    while (min.isBefore(max)) {
                        times.add(String.valueOf(min));
                        min = min.plusMinutes(15);
                    }
                }
                updateAppointStartTime.setItems(times);
                updateAppointEndTime.setItems(times);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    void addAppointButtonClicked(ActionEvent actionEvent) throws IOException {
       Parent addAppoint = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
       Scene newScene = new Scene(addAppoint);
       Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
       window.setScene(newScene);
       window.show();
       window.centerOnScreen();
    }

    @FXML
    void updateSaveButtonClicked(ActionEvent actionEvent) throws IOException {
        try {
            Connection connection = DBConnect.openConnection();

            if (!updateAppointTitle.getText().isEmpty() && !updateAppointDescription.getText().isEmpty() && !updateAppointLocation.getText().isEmpty() && !updateAppointType.getText().isEmpty() && updateAppointStartDate.getValue() != null && updateAppointEndDate.getValue() != null && !updateAppointStartTime.getValue().isEmpty() && !updateAppointEndTime.getValue().isEmpty() && !updateAppointCustomerID.getText().isEmpty()) {
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
                LocalDateTime endLocalAll = LocalDateTime.of(endLocalDate, endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startLocalAll, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endLocalAll, ZoneId.systemDefault());

                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/New_York"));

                if (startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue())) {
                    Alert outsideBusiness = new Alert(Alert.AlertType.ERROR);
                    outsideBusiness.setTitle("Outside Business Operations");
                    outsideBusiness.setContentText("You have selected a day outside of business operations. Business days are normally Monday-Friday.");
                    outsideBusiness.showAndWait();
                    return;
                }
                if (startToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || startToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))
                        || endToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || endToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))) {
                    Alert timeOutsideBusiness = new Alert(Alert.AlertType.ERROR);
                    timeOutsideBusiness.setTitle("Outside Business Operations");
                    timeOutsideBusiness.setContentText("You have selected a time outside of business operations. Business hours are normally 8:00am-10:00pm.");
                    timeOutsideBusiness.showAndWait();
                    return;
                }

                int alteredCustomID = Integer.parseInt(updateAppointCustomerID.getText());
                int appointID = Integer.parseInt(updateAppointID.getText());

                if (endLocalAll.isBefore(startLocalAll)) {
                    Alert valueError = new Alert(Alert.AlertType.ERROR);
                    valueError.setTitle("Start Time After End Time");
                    valueError.setContentText("The selected start time cannot be after the selected end time.");
                    valueError.showAndWait();
                    return;
                }
                if (endLocalAll.isEqual(startLocalAll)) {
                    Alert sameTimes = new Alert(Alert.AlertType.ERROR);
                    sameTimes.setTitle("Same Start and End Times");
                    sameTimes.setContentText("The appointment has the same start and end times. End time must be later than start time.");
                    sameTimes.showAndWait();
                    return;
                }
                for (Appointment appointment : maintainAppointments) {
                    LocalDateTime startAppointVerify = appointment.getStart();
                    LocalDateTime endAppointVerify = appointment.getEnd();

                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (startLocalAll.isBefore(startAppointVerify)) && (endLocalAll.isAfter(endAppointVerify))) {
                        Alert overlapAppoint = new Alert(Alert.AlertType.ERROR);
                        overlapAppoint.setTitle("Overlapping Appointment");
                        overlapAppoint.setContentText("This appointment will overlap with an existing appointment.");
                        overlapAppoint.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (startLocalAll.isAfter(startAppointVerify)) && (startLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Overlapping Appointment");
                        overlapStartTime.setContentText("The start time of this appointment will overlap with an existing appointment.");
                        overlapStartTime.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (endLocalAll.isAfter(startAppointVerify)) && (endLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Overlapping Appointment");
                        overlapStartTime.setContentText("The end time of this appointment will overlap with an existing appointment.");
                        overlapStartTime.showAndWait();
                        return;
                    }
                }
                String timeStart = updateAppointStartTime.getValue();
                String dateStart = updateAppointStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String timeEnd = updateAppointEndTime.getValue();
                String dateEnd = updateAppointEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String utcStart = timeAndDateToUTC(dateStart + " " + timeStart + ":00");
                String utcEnd = timeAndDateToUTC(dateEnd + " " + timeEnd + ":00");

                String sqlCommand = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
                DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
                PreparedStatement prepare = DBConnect.getPreparedStatement();

                prepare.setInt(1, Integer.parseInt(updateAppointID.getText()));
                prepare.setString(2, updateAppointTitle.getText());
                prepare.setString(3, updateAppointDescription.getText());
                prepare.setString(4, updateAppointLocation.getText());
                prepare.setString(5, updateAppointDescription.getText());
                prepare.setString(6, utcStart);
                prepare.setString(7, utcEnd);
                prepare.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                prepare.setString(9, "admin");
                prepare.setInt(10, Integer.parseInt(updateAppointCustomerID.getText()));
                prepare.setInt(11, Integer.parseInt(updateAppointUserID.getText()));
                prepare.setInt(12, Integer.parseInt(ContactDAO.tryContactID(updateAppointContact.getValue())));
                prepare.setInt(13, Integer.parseInt(updateAppointID.getText()));
                prepare.execute();

                updateAppointID.clear();
                updateAppointTitle.clear();
                updateAppointDescription.clear();
                updateAppointLocation.clear();
                updateAppointType.clear();
                updateAppointCustomerID.clear();
                updateAppointUserID.clear();


                ObservableList<Appointment> listOfAppointments = AppointmentDAO.getAppointments();
                appointmentTable.setItems(listOfAppointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        @FXML void deleteButtonClicked (ActionEvent actionEvent) {
            try {
                Appointment appointClicked = appointmentTable.getSelectionModel().getSelectedItem();
                Connection connection = DBConnect.openConnection();
                if (appointClicked == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment not Selected");
                    alert.setContentText("You must select an appointment to delete.");
                    alert.show();
                } else {
                    int delID = appointmentTable.getSelectionModel().getSelectedItem().getAppointID();
                    String delType = appointmentTable.getSelectionModel().getSelectedItem().getAppointType();
                    Alert delIDAndType = new Alert(Alert.AlertType.CONFIRMATION, "Delete the appointment with the following information? Appointment ID: " + delID + " | Appointment Type: " + delType + ".");
                    Optional<ButtonType> validate = delIDAndType.showAndWait();
                    if (validate.isPresent() && validate.get() == ButtonType.OK) {
                        AppointmentDAO.deleteAppoint(delID, connection);

                        ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
                        appointmentTable.setItems(maintainAppointments);
                    }
                }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

        @FXML void returnToMainMenuButtonClicked (ActionEvent actionEvent) throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to return to the main menu?");
            Optional<ButtonType> validate = alert.showAndWait();
            if (validate.isPresent() && validate.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
                Scene newScene = new Scene(root);
                Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                returnToMain.setScene(newScene);
                returnToMain.show();
                returnToMain.centerOnScreen();
            }
        }
        @FXML void appointWeekRadioButtonClicked (ActionEvent actionEvent){
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

        @FXML void allAppointRadioButtonClicked (ActionEvent actionEvent){
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

        @FXML void appointMonthRadioButtonClicked (ActionEvent actionEvent){
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

        public void exitButtonClicked (ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit this program?");
            Optional<ButtonType> validate = alert.showAndWait();
            if (validate.isPresent() && validate.get() == ButtonType.OK) {
                Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                exitProgram.close();
            }
        }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            appointID.setCellValueFactory(new PropertyValueFactory<>("appointID"));
            appointTitle.setCellValueFactory(new PropertyValueFactory<>("appointTitle"));
            appointDescription.setCellValueFactory(new PropertyValueFactory<>("appointDescription"));
            appointLocation.setCellValueFactory(new PropertyValueFactory<>("appointLocation"));
            contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            appointType.setCellValueFactory(new PropertyValueFactory<>("appointType"));
            appointStart.setCellValueFactory(new PropertyValueFactory<>("start"));
            appointEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
            appointCustomerID.setCellValueFactory(new PropertyValueFactory<>("appointCustomerID"));
            userID.setCellValueFactory(new PropertyValueFactory<>("userID"));

            appointmentTable.setItems(maintainAppointments);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}

