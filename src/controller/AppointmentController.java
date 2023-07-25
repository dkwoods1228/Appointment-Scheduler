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
    @FXML
    private Button updateAppointmentButton;
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
    private ComboBox<Integer> updateAppointCustomerID;
    @FXML
    private ComboBox<Integer> updateAppointUserID;
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
    private TableColumn<?, ?> customerID;
    @FXML
    private TableColumn<?, ?> userID;

    /**
     * When Update Appointment button is clicked, the components of the selected appointment will fill into the text fields and ComboBoxes under the update appointment section to the right of the screen.
     * @param actionEvent
     */
    @FXML
    void updateAppointmentButtonClicked(ActionEvent actionEvent) {
        try {
            DBConnect.openConnection();
            Appointment appointClicked = appointmentTable.getSelectionModel().getSelectedItem();

            if (appointClicked == null) {
                Alert unselectedAppoint = new Alert(Alert.AlertType.ERROR);
                unselectedAppoint.setTitle("Error");
                unselectedAppoint.setHeaderText("Appointment not Selected");
                unselectedAppoint.setContentText("You must select an appointment to update before continuing.");
                unselectedAppoint.showAndWait();
                return;
            }

            if (appointClicked != null) {
                ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
                ObservableList<String> contacts = FXCollections.observableArrayList();
                String showContact = "";
                ObservableList<Integer> user = FXCollections.observableArrayList();
                String showUsers = "";
                updateAppointUserID.setItems(user);


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
                updateAppointCustomerID.setItems(CustomerDAO.getEveryCustomerID());
                updateAppointCustomerID.getSelectionModel().select(appointClicked.getCustomerID());
                updateAppointCustomerID.setValue(appointClicked.getCustomerID());
                updateAppointUserID.setItems(UserDAO.getEveryUserID());
                updateAppointUserID.getSelectionModel().select(appointClicked.getUserID());
                updateAppointUserID.setValue(appointClicked.getUserID());

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

    /**
     * Takes user to the add appointment screen.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    void addAppointButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent addAppoint = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        Scene newScene = new Scene(addAppoint);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
        window.centerOnScreen();
    }

    /**
     * When clicking the Save Updated Appointment button, the appointment the user updated is saved and updated in the appointment table.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    void updateSaveButtonClicked(ActionEvent actionEvent) throws IOException {
        try {
            Connection connection = DBConnect.openConnection();

            if (!updateAppointTitle.getText().isEmpty() && !updateAppointDescription.getText().isEmpty() && !updateAppointLocation.getText().isEmpty() && !updateAppointType.getText().isEmpty() && updateAppointStartDate.getValue() != null && updateAppointEndDate.getValue() != null && !updateAppointStartTime.getValue().isEmpty() && !updateAppointEndTime.getValue().isEmpty() && updateAppointCustomerID.getValue() != null) {
                ObservableList<Customer> maintainCustomers = CustomerDAO.getCustomers(connection);
                ObservableList<Integer> maintainCustomID = FXCollections.observableArrayList();
                ObservableList<UserDAO> maintainUsers = UserDAO.getUsers();
                ObservableList<Integer> maintainUserID = FXCollections.observableArrayList();
                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
                //get customers and users data added
                maintainCustomers.stream().map(Customer::getCustomerID).forEach(maintainCustomID::add);
                maintainUsers.stream().map(User::getUserID).forEach(maintainUserID::add);
                //time conversions
                LocalDate startLocalDate = updateAppointStartDate.getValue();
                LocalDate endLocalDate = updateAppointEndDate.getValue();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

                LocalTime startLocalTime = LocalTime.parse(updateAppointStartTime.getValue(), timeFormat);
                LocalTime endLocalTime = LocalTime.parse(updateAppointEndTime.getValue(), timeFormat);

                LocalDateTime startLocalAll = LocalDateTime.of(startLocalDate, startLocalTime);
                LocalDateTime endLocalAll = LocalDateTime.of(endLocalDate, endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startLocalAll, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endLocalAll, ZoneId.systemDefault());

                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/Chicago"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/Chicago"));
                //avoids scheduling an appointment on Saturdays and Sundays
                if (startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue())) {
                    Alert outsideBusiness = new Alert(Alert.AlertType.ERROR);
                    outsideBusiness.setTitle("Error");
                    outsideBusiness.setHeaderText("Outside Business Operations");
                    outsideBusiness.setContentText("You have selected a day outside of business operations. Business days are normally Monday-Friday.");
                    outsideBusiness.showAndWait();
                    return;
                }
                //Avoids scheduling an appointment outside of business hours
                if (startToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || startToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))
                        || endToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || endToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))) {
                    Alert timeOutsideBusiness = new Alert(Alert.AlertType.ERROR);
                    timeOutsideBusiness.setTitle("Error");
                    timeOutsideBusiness.setHeaderText("Outside Business Operations");
                    timeOutsideBusiness.setContentText("You have selected a time outside of business operations. Business hours are normally 8:00am-10:00pm EST.");
                    timeOutsideBusiness.showAndWait();
                    return;
                }

                int alteredCustomID = (updateAppointCustomerID.getValue());
                int appointID = Integer.parseInt(updateAppointID.getText());

                if (endLocalAll.isBefore(startLocalAll)) {
                    Alert valueError = new Alert(Alert.AlertType.ERROR);
                    valueError.setTitle("Error");
                    valueError.setHeaderText("Start Time After End Time");
                    valueError.setContentText("The selected start time cannot be after the selected end time.");
                    valueError.showAndWait();
                    return;
                }
                if (endLocalAll.isEqual(startLocalAll)) {
                    Alert sameTimes = new Alert(Alert.AlertType.ERROR);
                    sameTimes.setTitle("Error");
                    sameTimes.setHeaderText("Same Start and End Times");
                    sameTimes.setContentText("The appointment has the same start and end times. End time must be later than start time.");
                    sameTimes.showAndWait();
                    return;
                }
                for (Appointment appointment : maintainAppointments) {
                    LocalDateTime startAppointVerify = appointment.getStart();
                    LocalDateTime endAppointVerify = appointment.getEnd();

                    if ((alteredCustomID == appointment.getCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (startLocalAll.isBefore(startAppointVerify)) && (endLocalAll.isAfter(endAppointVerify))) {
                        Alert overlapAppoint = new Alert(Alert.AlertType.ERROR);
                        overlapAppoint.setTitle("Error");
                        overlapAppoint.setHeaderText("Overlapping Appointment");
                        overlapAppoint.setContentText("This appointment will overlap with an existing appointment.");
                        overlapAppoint.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (startLocalAll.isAfter(startAppointVerify)) && (startLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Error");
                        overlapStartTime.setHeaderText("Overlapping Appointment");
                        overlapStartTime.setContentText("The start time of this appointment will overlap with an existing appointment.");
                        overlapStartTime.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getCustomerID()) && (appointID != appointment.getAppointID()) &&
                            (endLocalAll.isAfter(startAppointVerify)) && (endLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Error");
                        overlapStartTime.setHeaderText("Overlapping Appointment");
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
                //updates appointments within the database when an appointment is updated in the program
                String sqlCommand = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
                DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
                PreparedStatement prepare = DBConnect.getPreparedStatement();
                updateAppointUserID.setItems(UserDAO.getEveryUserID());

                prepare.setInt(1, Integer.parseInt(updateAppointID.getText()));
                prepare.setString(2, updateAppointTitle.getText());
                prepare.setString(3, updateAppointDescription.getText());
                prepare.setString(4, updateAppointLocation.getText());
                prepare.setString(5, updateAppointType.getText());
                prepare.setString(6, utcStart);
                prepare.setString(7, utcEnd);
                prepare.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                prepare.setString(9, "admin");
                prepare.setInt(10, updateAppointCustomerID.getValue());
                prepare.setInt(11, updateAppointUserID.getValue());
                prepare.setInt(12, Integer.parseInt(ContactDAO.tryContactID(updateAppointContact.getValue())));
                prepare.setInt(13, Integer.parseInt(updateAppointID.getText()));
                prepare.execute();
                //clears data from text fields and combo boxes after appointment is updated into the appointment table
                updateAppointID.clear();
                updateAppointTitle.clear();
                updateAppointDescription.clear();
                updateAppointLocation.clear();
                updateAppointType.clear();
                updateAppointContact.getSelectionModel().clearSelection();
                updateAppointStartDate.getEditor().clear();
                updateAppointStartTime.getSelectionModel().clearSelection();
                updateAppointEndDate.getEditor().clear();
                updateAppointEndTime.getSelectionModel().clearSelection();
                updateAppointCustomerID.getSelectionModel().clearSelection();
                updateAppointUserID.getSelectionModel().clearSelection();


                ObservableList<Appointment> listOfAppointments = AppointmentDAO.getAppointments();
                appointmentTable.setItems(listOfAppointments);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The selected appointment has been updated.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes appointment from appointment table and database.
     * @param actionEvent
     */
    @FXML
    void deleteButtonClicked(ActionEvent actionEvent) {
        try {
            Appointment appointClicked = appointmentTable.getSelectionModel().getSelectedItem();
            Connection connection = DBConnect.openConnection();
            if (appointClicked == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment not Selected");
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The selected appointment has been deleted.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes user to Reports page when clicked.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    void viewReportsButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
            Scene newScene = new Scene(root);
            Stage viewReports = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            viewReports.setScene(newScene);
            viewReports.show();
            viewReports.centerOnScreen();
        }

    /**
     * When this radio button is chosen, the appointments within the current week will appear in the appointment table.
     * @param actionEvent
     */
    @FXML
    void appointWeekRadioButtonClicked(ActionEvent actionEvent) {
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

    /**
     * When this radio button is chosen, all appointments (regardless of appointment date) will appear within the appointment table. This is the default setting.
     * @param actionEvent
     */
    @FXML
    void allAppointRadioButtonClicked(ActionEvent actionEvent) {
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

    /**
     * When this radio button is chosen, the appointments within the current month will appear in the appointment table.
     * @param actionEvent
     */
    @FXML
    void appointMonthRadioButtonClicked(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            ObservableList<Appointment> maintainMonthlyAppointments = FXCollections.observableArrayList();
            LocalDateTime startMonthNow = LocalDateTime.now().minusMonths(1);
            LocalDateTime endMonthNow = LocalDateTime.now().plusMonths(1);

            if (maintainAppointments != null)
            maintainAppointments.forEach(appointment -> {
                if (appointment.getEnd().isBefore(endMonthNow) && appointment.getEnd().isAfter(startMonthNow)) {
                    maintainMonthlyAppointments.add(appointment);
                }
                appointmentTable.setItems(maintainMonthlyAppointments);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the program when clicked.
     * @param actionEvent
     */
    public void exitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit this program?");
        Optional<ButtonType> validate = alert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            exitProgram.close();
        }
    }

    /**
     * Initializes data within the appointment table.
     * @param url
     * @param resourceBundle
     */
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
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userID.setCellValueFactory(new PropertyValueFactory<>("userID"));

            appointmentTable.setItems(maintainAppointments);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Takes user to the Customer Records page when clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void viewCustomersButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            Scene newScene = new Scene(root);
            Stage viewCustomers = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            viewCustomers.setScene(newScene);
            viewCustomers.show();
            viewCustomers.centerOnScreen();
        }
    }


