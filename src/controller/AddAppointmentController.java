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
import javafx.stage.Stage;
import model.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import static main.Timezone.timeAndDateToUTC;

public class AddAppointmentController {
    @FXML private TextField addAppointTitle;
    @FXML private TextField addAppointID;
    @FXML private TextField addAppointDescription;
    @FXML private TextField addAppointLocation;
    @FXML private ComboBox<String> addAppointContact;
    @FXML private TextField addAppointType;
    @FXML private DatePicker addAppointStartDate;
    @FXML private DatePicker addAppointEndDate;
    @FXML private ComboBox<String> addAppointStartTime;
    @FXML private ComboBox<String> addAppointEndTime;
    @FXML private ComboBox<Integer> addAppointCustomerID;
    @FXML private ComboBox<Integer> addAppointUserID;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @FXML void saveButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            if (addAppointTitle.getText().isEmpty() || addAppointDescription.getText().isEmpty() || addAppointLocation.getText().isEmpty() || addAppointType.getText().isEmpty() || addAppointStartDate.getValue() == null || addAppointEndDate.getValue() == null || addAppointStartTime.getValue().isEmpty() || addAppointEndTime.getValue().isEmpty() || addAppointCustomerID.getValue() == null) {
                Alert missingFields = new Alert(Alert.AlertType.ERROR);
                missingFields.setTitle("Error");
                missingFields.setHeaderText("Missing Information");
                missingFields.setContentText("You must enter information in all fields to add an appointment.");
                missingFields.showAndWait();
                return;
            } else if (!addAppointTitle.getText().isEmpty() && !addAppointDescription.getText().isEmpty() && !addAppointLocation.getText().isEmpty() && !addAppointType.getText().isEmpty() && addAppointStartDate.getValue() != null && addAppointEndDate.getValue() != null && !addAppointStartTime.getValue().isEmpty() && !addAppointEndTime.getValue().isEmpty() && addAppointCustomerID.getValue() != null ) {
                ObservableList<Customer> maintainCustomers = CustomerDAO.getCustomers(connection);
                ObservableList<Integer> maintainCustomID = FXCollections.observableArrayList();
                ObservableList<UserDAO> maintainUsers = UserDAO.getUsers();
                ObservableList<Integer> maintainUserID = FXCollections.observableArrayList();
                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();

                maintainUsers.stream().map(User::getUserID).forEach(maintainUserID::add);

                maintainCustomers.stream().map(Customer::getCustomerID).forEach(maintainCustomID::add);

                LocalDate startLocalDate = addAppointStartDate.getValue();
                LocalDate endLocalDate = addAppointEndDate.getValue();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime startLocalTime = LocalTime.parse(addAppointStartTime.getValue(), timeFormat);
                LocalTime endLocalTime = LocalTime.parse(addAppointEndTime.getValue(), timeFormat);
                LocalDateTime startLocalAll = LocalDateTime.of(startLocalDate, startLocalTime);
                LocalDateTime endLocalAll = LocalDateTime.of(endLocalDate, endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startLocalAll, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endLocalAll, ZoneId.systemDefault());
                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/Chicago"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/Chicago"));
                if (startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        startToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                        endToEasternTime.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue())) {
                    Alert outsideBusiness = new Alert(Alert.AlertType.ERROR);
                    outsideBusiness.setTitle("Error");
                    outsideBusiness.setHeaderText("Outside of Business Operations");
                    outsideBusiness.setContentText("You have selected a day outside of business operations. Business days are normally Monday-Friday.");
                    outsideBusiness.showAndWait();
                    return;
                }
                if (startToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || startToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))
                        || endToEasternTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || endToEasternTime.toLocalTime().isAfter(LocalTime.of(22, 0, 0))) {
                    Alert timeOutsideBusiness = new Alert(Alert.AlertType.ERROR);
                    timeOutsideBusiness.setTitle("Error");
                    timeOutsideBusiness.setHeaderText("Outside Business Operations");
                    timeOutsideBusiness.setContentText("You have selected a time outside of business operations. Business hours are normally 8:00am-10:00pm.");
                    timeOutsideBusiness.showAndWait();
                    return;
                }

                int alteredCustomID = Integer.parseInt(String.valueOf(addAppointCustomerID.getValue()));
                int disabledID = Integer.parseInt(String.valueOf((int) (Math.random() * 50)));


                //if & else if statement (with return;) to avoid user from entering unavailable customer ID.


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

                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (startLocalAll.isBefore(startAppointVerify)) && (endLocalAll.isAfter(endAppointVerify))) {
                            Alert overlapAppoint = new Alert(Alert.AlertType.ERROR);
                            overlapAppoint.setTitle("Error");
                            overlapAppoint.setHeaderText("Overlapping Appointment");
                            overlapAppoint.setContentText("This appointment will overlap with an existing appointment.");
                            overlapAppoint.showAndWait();
                            return;
                        }
                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (startLocalAll.isAfter(startAppointVerify)) && (startLocalAll.isBefore(endAppointVerify))) {
                            Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                            overlapStartTime.setTitle("Error");
                            overlapStartTime.setHeaderText("Overlapping Appointment");
                            overlapStartTime.setContentText("The start time of this appointment will overlap with an existing appointment.");
                            overlapStartTime.showAndWait();
                            return;
                        }
                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (endLocalAll.isAfter(startAppointVerify)) && (endLocalAll.isBefore(endAppointVerify))) {
                            Alert overlapEndTime = new Alert(Alert.AlertType.ERROR);
                            overlapEndTime.setTitle("Error");
                            overlapEndTime.setHeaderText("Overlapping Appointment");
                            overlapEndTime.setContentText("The end time of this appointment will overlap with an existing appointment.");
                            overlapEndTime.showAndWait();
                            return;
                        }
                    }
                    String timeStart = addAppointStartTime.getValue();
                    String dateStart = addAppointStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String timeEnd = addAppointEndTime.getValue();
                    String dateEnd = addAppointEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String utcStart = timeAndDateToUTC(dateStart + " " + timeStart + ":00");
                    String utcEnd = timeAndDateToUTC(dateEnd + " " + timeEnd + ":00");

                    String sqlCommand = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
                    PreparedStatement prepare = DBConnect.getPreparedStatement();

                    prepare.setInt(1, disabledID);
                    prepare.setString(2, addAppointTitle.getText());
                    prepare.setString(3, addAppointDescription.getText());
                    prepare.setString(4, addAppointLocation.getText());
                    prepare.setString(5, addAppointType.getText());
                    prepare.setString(6, utcStart);
                    prepare.setString(7, utcEnd);
                    prepare.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    prepare.setString(9, "admin");
                    prepare.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    prepare.setInt(11, 1);
                    prepare.setInt(12, addAppointCustomerID.getValue());
                    prepare.setInt(13, Integer.parseInt(ContactDAO.tryContactID(addAppointContact.getValue())));
                    prepare.setInt(14, addAppointUserID.getValue());
                    prepare.execute();

                }
                Parent goToAppoint = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
                Scene newScene = new Scene(goToAppoint);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(newScene);
                stage.show();
                stage.centerOnScreen();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment has been added.");
                alert.showAndWait();

            } catch (SQLException | IOException sqlException) {
            sqlException.printStackTrace();
        }
    }


    @FXML
    void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent goToAppoint = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Scene newScene = new Scene(goToAppoint);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
        stage.centerOnScreen();

    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
        ObservableList<String> contacts = FXCollections.observableArrayList();
        maintainContacts.forEach(contact -> contacts.add(contact.getContactName()));
        ObservableList<String> times = FXCollections.observableArrayList();

        LocalTime min = LocalTime.MIN.plusHours(8);
        LocalTime max = LocalTime.MAX.minusHours(1).minusMinutes(45);

        if (!min.equals(0) || !max.equals(0)) {
            while (min.isBefore(max)) {
                times.add(String.valueOf(min));
                min = min.plusMinutes(15);
            }
        }
        addAppointContact.setItems(contacts);
        addAppointStartTime.setItems(times);
        addAppointEndTime.setItems(times);
        addAppointUserID.setItems(UserDAO.getEveryUserID());
        addAppointCustomerID.setItems(CustomerDAO.getEveryCustomerID());

    }
}
