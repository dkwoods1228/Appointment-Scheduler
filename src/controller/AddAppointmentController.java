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
/**Class used to control the addition of an appointment to the appointment table.*/
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

    /**
     * When pressing the Save Button, it saves the appointment that the user has inputted and adds the appointment to the appointment table.
     * @param actionEvent
     */
    @FXML public void saveButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            //ensures all fields have information added
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
                //date and time conversions
                LocalDate startLocalDate = addAppointStartDate.getValue();
                LocalDate endLocalDate = addAppointEndDate.getValue();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

                String startDateOfAppoint = addAppointStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String startTimeOfAppoint = addAppointStartTime.getValue();
                String endDateOfAppoint = addAppointEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String endTimeOfAppoint = addAppointEndTime.getValue();

                System.out.println("This Date + This Start " + startDateOfAppoint + " " + startTimeOfAppoint + ":00");
                String utcStart = timeAndDateToUTC(startDateOfAppoint + " " + startTimeOfAppoint + ":00");
                String utcEnd = timeAndDateToUTC(endDateOfAppoint + " " + endTimeOfAppoint + ":00");

                LocalTime startLocalTime = LocalTime.parse(addAppointStartTime.getValue(), timeFormat);
                LocalTime endLocalTime = LocalTime.parse(addAppointEndTime.getValue(), timeFormat);
                LocalDateTime startDateAndTime = LocalDateTime.of(startLocalDate, startLocalTime);
                LocalDateTime endDateAndTime = LocalDateTime.of(endLocalDate, endLocalTime);

                ZonedDateTime startZone = ZonedDateTime.of(startDateAndTime, ZoneId.systemDefault());
                ZonedDateTime endZone = ZonedDateTime.of(endDateAndTime, ZoneId.systemDefault());
                ZonedDateTime startToEasternTime = startZone.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime endToEasternTime = endZone.withZoneSameInstant(ZoneId.of("America/New_York"));

                LocalTime checkStartAppointTime = startToEasternTime.toLocalTime();
                LocalTime checkEndAppointTime = endToEasternTime.toLocalTime();

                DayOfWeek checkStartAppointDate = startToEasternTime.toLocalDate().getDayOfWeek();
                DayOfWeek checkEndAppointDate = endToEasternTime.toLocalDate().getDayOfWeek();

                int checkStartAppointDateValue = checkStartAppointDate.getValue();
                int checkEndAppointDateValue = checkEndAppointDate.getValue();

                int startOfBusinessWeek = DayOfWeek.MONDAY.getValue();
                int endOfBusinessWeek = DayOfWeek.FRIDAY.getValue();
                //if any of these scenarios/errors occur, return user to the same page.
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
                    timeOutsideBusiness.setHeaderText("Outside of Business Operations");
                    timeOutsideBusiness.setContentText("You have selected a time outside of business operations. Business hours are normally 8:00am-10:00pm EST.");
                    timeOutsideBusiness.showAndWait();
                    return;
                }

                int alteredCustomID = Integer.parseInt(String.valueOf(addAppointCustomerID.getValue()));
                //Allows for 50 different appointment IDs
                int disabledID = Integer.parseInt(String.valueOf((int) (Math.random() * 50)));


                    if (endDateAndTime.isBefore(startDateAndTime)) {
                        Alert valueError = new Alert(Alert.AlertType.ERROR);
                        valueError.setTitle("Error");
                        valueError.setHeaderText("Start Time After End Time");
                        valueError.setContentText("The selected start time cannot be after the selected end time.");
                        valueError.showAndWait();
                        return;
                    }
                    if (endDateAndTime.isEqual(startDateAndTime)) {
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

                        //overlapping appointments
                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (startDateAndTime.isBefore(startAppointVerify)) && (endDateAndTime.isAfter(endAppointVerify))) {
                            Alert overlapAppoint = new Alert(Alert.AlertType.ERROR);
                            overlapAppoint.setTitle("Error");
                            overlapAppoint.setHeaderText("Overlapping Appointment");
                            overlapAppoint.setContentText("This appointment overlaps with an existing appointment that the selected customer is already assigned to. To continue, either alter the time of the appointment or the selected customer ID to avoid an overlap.");
                            overlapAppoint.showAndWait();
                            return;
                        }
                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (startDateAndTime.isAfter(startAppointVerify)) && (startDateAndTime.isBefore(endAppointVerify))) {
                            Alert overlapStartTime = new Alert(Alert.AlertType.ERROR);
                            overlapStartTime.setTitle("Error");
                            overlapStartTime.setHeaderText("Overlapping Appointment");
                            overlapStartTime.setContentText("The start time of this appointment will overlap with an existing appointment that the selected customer is already assigned to. To continue, either alter the time of the appointment or the selected customer ID to avoid an overlap..");
                            overlapStartTime.showAndWait();
                            return;
                        }
                        if ((alteredCustomID == appointment.getCustomerID()) && (disabledID != appointment.getAppointID()) &&
                                (endDateAndTime.isAfter(startAppointVerify)) && (endDateAndTime.isBefore(endAppointVerify))) {
                            Alert overlapEndTime = new Alert(Alert.AlertType.ERROR);
                            overlapEndTime.setTitle("Error");
                            overlapEndTime.setHeaderText("Overlapping Appointment");
                            overlapEndTime.setContentText("The end time of this appointment will overlap with an existing appointment that the selected customer is already assigned to.");
                            overlapEndTime.showAndWait();
                            return;
                        }
                    }
                    //adds new appointment to the database
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
                //After scene message appears
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment has successfully been added.");
                alert.showAndWait();

            } catch (SQLException | IOException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * When user clicks cancel button, the user is taken back to the appointments page.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent goToAppoint = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Scene newScene = new Scene(goToAppoint);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
        stage.centerOnScreen();

    }

    /**
     * Initializes and sets/fills ComboBoxes.
     * <br><br>
     * <p>
     *     <b>LAMBDA EXPRESSION - forEach used to easily fill contactName into the observable list, maintainContacts. </b>
     * </p>
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
        ObservableList<String> contacts = FXCollections.observableArrayList();
        //Lambda Expression - forEach used to easily fill contactName into the observable list, maintainContacts.
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
