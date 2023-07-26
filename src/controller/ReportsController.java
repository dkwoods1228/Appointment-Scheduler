package controller;

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
import java.sql.SQLException;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
/**Class used to showcase a number of reports gathered from appointment and customer data*/
public class ReportsController {
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<?,?> appointID;
    @FXML private TableColumn<?,?> appointTitle;
    @FXML private TableColumn<?,?> appointDescription;
    @FXML private TableColumn<?,?> appointType;
    @FXML private TableColumn<?,?> appointStart;
    @FXML private TableColumn<?,?> appointEnd;
    @FXML private TableColumn<?,?> appointCustomerID;
    @FXML private ComboBox<String> contactsComboBox;
    @FXML private TableView<ReportByType> appointTypeTotalTable;
    @FXML private TableColumn<?,?> reportsAppointTypeColumn;
    @FXML private TableColumn<?,?> reportsTotalAmountTypeColumn;
    @FXML private TableView<ReportByMonth> appointMonthTotalTable;
    @FXML private TableColumn<?,?> reportsAppointByMonthColumn;
    @FXML private TableColumn<?,?> reportsTotalAmountMonthColumn;
    @FXML private TableView<Report> customersCountryRelationTable;
    @FXML private TableColumn<?,?> country;
    @FXML private TableColumn<?,?> totalAmountCountry;
    @FXML private Button exitButton;
    @FXML private Button returnToMainMenuButton;

    /**
     * Populates the tables featured on the page.
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        appointID.setCellValueFactory(new PropertyValueFactory<>("appointID"));
        appointTitle.setCellValueFactory(new PropertyValueFactory<>("appointTitle"));
        appointDescription.setCellValueFactory(new PropertyValueFactory<>("appointDescription"));
        appointType.setCellValueFactory(new PropertyValueFactory<>("appointType"));
        appointStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        reportsAppointTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointByType"));
        reportsTotalAmountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointByTypeTotal"));
        reportsAppointByMonthColumn.setCellValueFactory(new PropertyValueFactory<>("appointByMonth"));
        reportsTotalAmountMonthColumn.setCellValueFactory(new PropertyValueFactory<>("appointByMonthTotal"));

        country.setCellValueFactory(new PropertyValueFactory<>("Country"));
        totalAmountCountry.setCellValueFactory(new PropertyValueFactory<>("totalAmountCountry"));


        ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
        ObservableList<String> gotContacts = FXCollections.observableArrayList();
        maintainContacts.forEach(contact -> gotContacts.add(contact.getContactName()));
        contactsComboBox.setItems(gotContacts);


    }

    /**
     * 3rd Chosen Report: Finds the number of customers from the 3 different countries(US, UK, Canada).
     * @throws SQLException
     */
    @FXML public void customerCountryRelation() throws SQLException {
        try {
            ObservableList<Report> maintainCountries = ReportDAO.getCountries();
            ObservableList<Report> gotCountries = FXCollections.observableArrayList();

            maintainCountries.forEach(gotCountries::add);
            customersCountryRelationTable.setItems(gotCountries);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Exits the program.
     * @param actionEvent
     */
        @FXML public void exitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to exit this program?");
        Optional<ButtonType> validate = alert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            Stage exitProgram = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            exitProgram.close();
        }
    }

    /**
     * Gathers data from appointments to detect the number of appointments by month and by type.
     * <br><br>
     * <p>
     * <b>LAMBDA EXPRESSION - used to efficiently fill the maintainAppointments observable list with data from appointments.</b>
     * </p>
     * @throws SQLException
     */
    @FXML public void appointMonthAndTypeTotalsButtonClicked() throws SQLException {
        try {
            ObservableList<Month> monthly = FXCollections.observableArrayList();
            ObservableList<Month> newMonth = FXCollections.observableArrayList();
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            ObservableList<String> type = FXCollections.observableArrayList();
            ObservableList<String> newAppoint = FXCollections.observableArrayList();
            ObservableList<ReportByMonth> monthlyReport = FXCollections.observableArrayList();
            ObservableList<ReportByType> typeReport = FXCollections.observableArrayList();
            //Lambda Expression - used to efficiently fill the maintainAppointments observable list with data from appointments.
            maintainAppointments.forEach(appointment -> {
                type.add(appointment.getAppointType());
            });
            //Also lambda
            maintainAppointments.stream().map(appointment -> {
                return appointment.getStart().getMonth();
            }).forEach(monthly::add);
            //Also lambda
            monthly.stream().filter(month -> {
                return !newMonth.contains(month);
            })  .forEach(newMonth::add);

            for(Appointment appoint: maintainAppointments) {
                String maintainAppointType = appoint.getAppointType();
                if (!newAppoint.contains(maintainAppointType)) {
                    newAppoint.add(maintainAppointType);
                }
            }
            for (Month mon: newMonth) {
                int allMonths = Collections.frequency(monthly, mon);
                String nameOfMonth = mon.name();
                ReportByMonth fillMonth = new ReportByMonth(nameOfMonth, allMonths);
                monthlyReport.add(fillMonth);
            }
            appointMonthTotalTable.setItems(monthlyReport);

            for (String maintainType: newAppoint) {
                String fillType = maintainType;
                int totalTypeReport = Collections.frequency(type, maintainType);
                ReportByType newType = new ReportByType(fillType, totalTypeReport);
                typeReport.add(newType);
            }
            appointTypeTotalTable.setItems(typeReport);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns to main menu if clicked.
     * @param actionEvent
     * @throws IOException
     */
    @FXML public void returnToMainMenuButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToMain.setScene(newScene);
            returnToMain.show();
            returnToMain.centerOnScreen();
    }

    /**
     * Fill the contacts combo box from appointments to show the appointments that are linked to specific contacts.
     * @param actionEvent
     */
    public void contactsComboBoxClicked(ActionEvent actionEvent) {
        try {
            ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
            ObservableList<Appointment> pullFromAppoint = FXCollections.observableArrayList();
            ObservableList<Contact> maintainContacts = ContactDAO.getContacts();
            Appointment appointToContact;
            int contactID = 0;
            String contact = contactsComboBox.getSelectionModel().getSelectedItem();

            for (Contact cont: maintainContacts) {
                if (contact.equals(cont.getContactName())) {
                    contactID = cont.getContactID();
                }
            }
            for (Appointment appoint: maintainAppointments) {
                if (appoint.getContactID() == contactID) {
                    appointToContact = appoint;
                    pullFromAppoint.add(appointToContact);
                }
            }
            appointmentTable.setItems(pullFromAppoint);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Takes user to the Appointments page if clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void goToAppointButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToMain.setScene(newScene);
            returnToMain.show();
            returnToMain.centerOnScreen();
        }

    /**
     * Takes user to the Customer Records page if clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void goToCustomersButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToMain.setScene(newScene);
            returnToMain.show();
            returnToMain.centerOnScreen();
        }
    }

