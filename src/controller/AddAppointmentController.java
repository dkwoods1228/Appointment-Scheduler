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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    @FXML private TextField addAppointCustomerID;
    @FXML private TextField addAppointUserID;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @FXML void saveButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();

            if (!addAppointTitle.getText().isEmpty() && !addAppointDescription.getText().isEmpty() && !addAppointLocation.getText().isEmpty() && !addAppointType.getText().isEmpty() && addAppointStartDate.getValue() != null && addAppointEndDate.getValue() != null && !addAppointStartTime.getValue().isEmpty() && !addAppointEndTime.getValue().isEmpty() && !addAppointCustomerID.getText().isEmpty()) {
                ObservableList<Customer> maintainCustomers = CustomerDAO.getCustomers(connection);
                ObservableList<Integer> maintainCustomID = FXCollections.observableArrayList();
                ObservableList<UserDAO> maintainUsers = UserDAO.getUsers();
                ObservableList<Integer> maintainUserID = FXCollections.observableArrayList();
                ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();

                maintainCustomers.stream().map(Customer::getCustomerID).forEach(maintainCustomID::add);
                maintainUsers.stream().map(User::getUserID).forEach(maintainUserID::add);

                LocalDate startLocalDate = addAppointStartDate.getValue();
                LocalDate endLocalDate = addAppointEndDate.getValue();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime startLocalTime = LocalTime.parse(addAppointStartTime.getValue(), timeFormat);
                LocalTime endLocalTime = LocalTime.parse(addAppointEndTime.getValue(), timeFormat);
                LocalDateTime startLocalAll = LocalDateTime.of(startLocalDate, startLocalTime);
                LocalDateTime endLocalAll = LocalDateTime.of(endLocalDate, endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startLocalAll, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endLocalAll, ZoneId.systemDefault());
                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/New_York"));

                LocalTime appointStartTime = startToEasternTime.toLocalTime();
                LocalTime appointEndTime = endToEasternTime.toLocalTime();

                DayOfWeek appointStartDay = startToEasternTime.toLocalDate().getDayOfWeek();
                DayOfWeek appointEndDay = endToEasternTime.toLocalDate().getDayOfWeek();

                int intAppointStart = appointStartDay.getValue();
                int intAppointEnd = appointEndDay.getValue();
                int getStartBusinessDays = DayOfWeek.MONDAY.getValue();
                int getEndBusinessDays = DayOfWeek.FRIDAY.getValue();
                LocalTime getStartBusinessHours = LocalTime.of(8, 0, 0);
                LocalTime getEndBusinessHours = LocalTime.of(22, 0, 0);


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

                int alteredCustomID = Integer.parseInt(addAppointCustomerID.getText());
                int disabledID = Integer.parseInt(String.valueOf((int) (Math.random() * 50)));

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

                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (disabledID != appointment.getAppointID()) &&
                            (startLocalAll.isBefore(startAppointVerify)) && (endLocalAll.isAfter(endAppointVerify))) {
                        Alert overlapAppoint = new Alert(Alert.AlertType.ERROR);
                        overlapAppoint.setTitle("Overlapping Appointment");
                        overlapAppoint.setContentText("This appointment will overlap with an existing appointment.");
                        overlapAppoint.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (disabledID != appointment.getAppointID()) &&
                            (startLocalAll.isAfter(startAppointVerify)) && (startLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Overlapping Appointment");
                        overlapStartTime.setContentText("The start time of this appointment will overlap with an existing appointment.");
                        overlapStartTime.showAndWait();
                        return;
                    }
                    if ((alteredCustomID == appointment.getAppointCustomerID()) && (disabledID != appointment.getAppointID()) &&
                            (endLocalAll.isAfter(startAppointVerify)) && (endLocalAll.isBefore(endAppointVerify))) {
                        Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                        overlapStartTime.setTitle("Overlapping Appointment");
                        overlapStartTime.setContentText("The end time of this appointment will overlap with an existing appointment.");
                        overlapStartTime.showAndWait();
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
                prepare.setInt(12, Integer.parseInt(addAppointCustomerID.getText()));
                prepare.setInt(13, Integer.parseInt(ContactDAO.tryContactID(addAppointContact.getValue())));
                prepare.setInt(14, Integer.parseInt(ContactDAO.tryContactID(addAppointUserID.getText())));
                prepare.execute();

            }
            Parent goToAppoint = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
            Scene newScene = new Scene(goToAppoint);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent goToAppoint = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Scene newScene = new Scene(goToAppoint);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();

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

    }
}
