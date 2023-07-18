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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class CustomerController {
    @FXML private TextField updateCustomerID;
    @FXML private TextField updateCustomerName;
    @FXML private TextField updateCustomerAddress;
    @FXML private TextField updateCustomerPostalCode;
    @FXML private TextField updateCustomerPhoneNumber;
    @FXML private ComboBox<String> updateCustomerCountry;
    @FXML private ComboBox<String> updateCustomerStateProv;
    @FXML private Button customerSaveButton;
    @FXML private Button customerCancelButton;
    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerRecordsButton;
    @FXML private Button customerToMainMenuButton;
    @FXML private Button deleteCustomerButton;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<?,?> customerIDColumn;
    @FXML private TableColumn<?,?> customerNameColumn;
    @FXML private TableColumn<?,?> customerAddressColumn;
    @FXML private TableColumn<?,?> customerPostalCodeColumn;
    @FXML private TableColumn<?,?> customerPhoneNumberColumn;
    @FXML private TableColumn<?,?> customerFirstLevelColumn;

    public void addCustomerButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            if (!updateCustomerName.getText().isEmpty() || !updateCustomerAddress.getText().isEmpty() || !updateCustomerPostalCode.getText().isEmpty() || !updateCustomerPhoneNumber.getText().isEmpty() || !updateCustomerCountry.getValue().isEmpty() || !updateCustomerStateProv.getValue().isEmpty()) {

                Integer disabledCustomerID = (int) (Math.random() * 50);
                int division = 0;
                for (DivisionDAO division1 : DivisionDAO.getDivisions()) {
                    if (updateCustomerStateProv.getSelectionModel().getSelectedItem().equals(division1.getDivision())) {
                        division = division1.getDivisionID();
                    }
                }
                String sqlCommand = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
                PreparedStatement prepare = DBConnect.getPreparedStatement();
                    prepare.setInt(1, disabledCustomerID);
                    prepare.setString(2, updateCustomerName.getText());
                    prepare.setString(3, updateCustomerAddress.getText());
                    prepare.setString(4, updateCustomerPostalCode.getText());
                    prepare.setString(5, updateCustomerPhoneNumber.getText());
                    prepare.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    prepare.setString(7, "admin");
                    prepare.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    prepare.setString(9, "admin");
                    prepare.setInt(10, division);
                    prepare.execute();

                    updateCustomerID.clear();
                    updateCustomerName.clear();
                    updateCustomerAddress.clear();
                    updateCustomerPostalCode.clear();
                    updateCustomerPhoneNumber.clear();

                    ObservableList<Customer> newCustomersTable = CustomerDAO.getCustomers(connection);
                    customerTable.setItems(newCustomersTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateCustomerRecordsButtonClicked(ActionEvent actionEvent) {
            try {
                DBConnect.openConnection();
                Customer customerClicked = (Customer) customerTable.getSelectionModel().getSelectedItem();
                String division = "", country = "";

                if(customerClicked != null) {
                    ObservableList<CountryDAO> getCountries = CountryDAO.getCountries();
                    ObservableList<DivisionDAO> getDivisions = DivisionDAO.getDivisions();
                    ObservableList<String> allDivisions = FXCollections.observableArrayList();

                    updateCustomerID.setText(String.valueOf((customerClicked.getCustomerID())));
                    updateCustomerName.setText(customerClicked.getCustomerName());
                    updateCustomerAddress.setText(customerClicked.getCustomerAddress());
                    updateCustomerPostalCode.setText(customerClicked.getCustomerPostal());
                    updateCustomerPhoneNumber.setText(customerClicked.getCustomerPhone());
                    updateCustomerStateProv.setItems(allDivisions);

                    for (Division division1: getDivisions) {
                        allDivisions.add(division1.getDivision());
                        int countryID = division1.getCountryID();

                        if (division1.getDivisionID() == customerClicked.getDivisionID()) {
                            division = division1.getDivision();

                            for (Countries countries: getCountries) {
                                if (countries.getCountryID() == countryID) {
                                    country = countries.getCountry();
                                }
                            }
                        }
                    }
                    updateCustomerStateProv.setValue(division);
                    updateCustomerCountry.setValue(country);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void customerSaveButtonClicked(ActionEvent actionEvent) {
    }

    @FXML void deleteCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnect.openConnection();
        ObservableList<Appointment> maintainAppointments = AppointmentDAO.getAppointments();
        Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this customer and their corresponding appointments?");
        Optional<ButtonType> validate = deleteCustomerAlert.showAndWait();
        if (validate.isPresent() && validate.get() == ButtonType.OK) {
            int delCustomID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
            AppointmentDAO.deleteAppoint(delCustomID, connection);

            String sqlCommand = "DELETE FROM customers WHERE Customer_ID = ?";
            DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
            PreparedStatement prepare = DBConnect.getPreparedStatement();
            int deleteCustomer = customerTable.getSelectionModel().getSelectedItem().getCustomerID();

            for (Appointment appoint: maintainAppointments) {
                int customerIDAppoint = appoint.getAppointCustomerID();
                if (deleteCustomer == customerIDAppoint) {
                    String sqlCommand2 = "DELETE FROM appointments WHERE Appointment_ID = ?";
                    DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand2);
                }
            }
            prepare.setInt(1, deleteCustomer);
            prepare.execute();
            ObservableList<Customer> newCustomersTable = CustomerDAO.getCustomers(connection);
            customerTable.setItems(newCustomersTable);
        }

    }

    public void customerCancelButtonClicked(ActionEvent actionEvent) {
    }

    public void updateCustomerCountryComboBox(ActionEvent actionEvent) throws SQLException {

    }

    public void customerToMainMenuButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to return to the main menu?");
        alert.showAndWait();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Scene newScene = new Scene(root);
        Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        returnToMain.setScene(newScene);
        returnToMain.show();
        returnToMain.centerOnScreen();
    }
}
