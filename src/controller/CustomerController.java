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
import java.lang.reflect.Executable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
/**Class used to showcase imported data from the database into a table of customer information. Also allows the user to add a customer and update customer records.*/
public class CustomerController implements Initializable {
    @FXML
    private TextField updateCustomerID;
    @FXML
    private TextField updateCustomerName;
    @FXML
    private TextField updateCustomerAddress;
    @FXML
    private TextField updateCustomerPostalCode;
    @FXML
    private TextField updateCustomerPhoneNumber;
    @FXML
    private ComboBox<String> updateCustomerCountry;
    @FXML
    private ComboBox<String> updateCustomerStateProv;
    @FXML
    private Button customerSaveButton;
    @FXML
    private Button customerCancelButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button updateCustomerRecordsButton;
    @FXML
    private Button customerToMainMenuButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<?, ?> customerIDColumn;
    @FXML
    private TableColumn<?, ?> customerNameColumn;
    @FXML
    private TableColumn<?, ?> customerAddressColumn;
    @FXML
    private TableColumn<?, ?> customerPostalCodeColumn;
    @FXML
    private TableColumn<?, ?> customerPhoneNumberColumn;
    @FXML
    private TableColumn<?, ?> customerFirstLevelColumn;

    /**
     * After entering data for a new customer into text fields and combo boxes, the user will click the add button, which will add the created customer to the customer table.
     * @param actionEvent
     */
    @FXML
    public void addCustomerButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            if (updateCustomerName.getText().isEmpty() || updateCustomerAddress.getText().isEmpty() || updateCustomerPostalCode.getText().isEmpty() || updateCustomerPhoneNumber.getText().isEmpty() || updateCustomerCountry.getValue().isEmpty() || updateCustomerStateProv.getValue().isEmpty()) {
                Alert missingFields = new Alert(Alert.AlertType.ERROR);
                missingFields.setTitle("Error");
                missingFields.setHeaderText("Missing Information");
                missingFields.setContentText("You must enter information in all fields to add a customer.");
                missingFields.showAndWait();

            } else if (!updateCustomerName.getText().isEmpty() || !updateCustomerAddress.getText().isEmpty() || !updateCustomerPostalCode.getText().isEmpty() || !updateCustomerPhoneNumber.getText().isEmpty() || !updateCustomerCountry.getValue().isEmpty() || !updateCustomerStateProv.getValue().isEmpty()) {

                Integer disabledCustomerID = (int) (Math.random() * 50);
                int division = 0;
                for (DivisionDAO division1 : DivisionDAO.getDivisions()) {
                    if (updateCustomerStateProv.getSelectionModel().getSelectedItem().equals(division1.getDivision())) {
                        division = division1.getDivisionID();
                    }
                }
                //inserts added customer to the database table
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
                //clears fields once customer is added to the customers table.
                updateCustomerID.clear();
                updateCustomerName.clear();
                updateCustomerAddress.clear();
                updateCustomerPostalCode.clear();
                updateCustomerPhoneNumber.clear();


                ObservableList<Customer> newCustomersTable = CustomerDAO.getCustomers(connection);
                customerTable.setItems(newCustomersTable);
            }  Alert alert = new Alert(Alert.AlertType.INFORMATION, "New customer record has been added.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When the Update Customer Records button is clicked, the components of the selected customer will fill into the text fields and ComboBoxes under the update appointment section to the right of the screen.
     * @param actionEvent
     */
    @FXML
    public void updateCustomerRecordsButtonClicked(ActionEvent actionEvent) {
        try {
            DBConnect.openConnection();
            Customer customerClicked = (Customer) customerTable.getSelectionModel().getSelectedItem();
            String division = "", country = "";

            if (customerClicked == null) {
                Alert unselectedCustomer = new Alert(Alert.AlertType.ERROR);
                unselectedCustomer.setTitle("Error");
                unselectedCustomer.setHeaderText("Customer not Selected");
                unselectedCustomer.setContentText("You must select a customer to update their records.");
                unselectedCustomer.showAndWait();
                return;
            }

            if (customerClicked != null) {
                ObservableList<CountryDAO> getCountries = CountryDAO.getCountries();
                ObservableList<DivisionDAO> getDivisions = DivisionDAO.getDivisions();
                ObservableList<String> allDivisions = FXCollections.observableArrayList();

                updateCustomerID.setText(String.valueOf((customerClicked.getCustomerID())));
                updateCustomerName.setText(customerClicked.getCustomerName());
                updateCustomerAddress.setText(customerClicked.getCustomerAddress());
                updateCustomerPostalCode.setText(customerClicked.getCustomerPostal());
                updateCustomerPhoneNumber.setText(customerClicked.getCustomerPhone());
                updateCustomerStateProv.setItems(allDivisions);

                for (Division division1 : getDivisions) {
                    allDivisions.add(division1.getDivision());
                    int countryID = division1.getCountryID();

                    if (division1.getDivisionID() == customerClicked.getDivisionID()) {
                        division = division1.getDivision();

                        for (Countries countries : getCountries) {
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

    /**
     * Saves the updated customer record that the user altered.
     * @param actionEvent
     */
    @FXML
    public void customerSaveButtonClicked(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnect.openConnection();
            if (!updateCustomerName.getText().isEmpty() || !updateCustomerAddress.getText().isEmpty() || !updateCustomerPostalCode.getText().isEmpty() || !updateCustomerPhoneNumber.getText().isEmpty() || !updateCustomerCountry.getValue().isEmpty() || !updateCustomerStateProv.getValue().isEmpty()) {
                int division = 0;
                for (DivisionDAO division1 : DivisionDAO.getDivisions()) {
                    if (updateCustomerStateProv.getSelectionModel().getSelectedItem().equals(division1.getDivision())) {
                        division = division1.getDivisionID();
                    }
                }
                String sqlCommand = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
                DBConnect.setPreparedStatement(DBConnect.getConnection(), sqlCommand);
                PreparedStatement prepare = DBConnect.getPreparedStatement();
                prepare.setInt(1, Integer.parseInt(updateCustomerID.getText()));
                prepare.setString(2, updateCustomerName.getText());
                prepare.setString(3, updateCustomerAddress.getText());
                prepare.setString(4, updateCustomerPostalCode.getText());
                prepare.setString(5, updateCustomerPhoneNumber.getText());
                prepare.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                prepare.setString(7, "admin");
                prepare.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                prepare.setString(9, "admin");
                prepare.setInt(10, division);
                prepare.setInt(11, Integer.parseInt((updateCustomerID.getText())));
                prepare.execute();
                //clear text fields and combo boxes after updated customer is back into the customer table
                updateCustomerID.clear();
                updateCustomerName.clear();
                updateCustomerAddress.clear();
                updateCustomerPostalCode.clear();
                updateCustomerPhoneNumber.clear();


                ObservableList<Customer> newCustomersTable = CustomerDAO.getCustomers(connection);
                customerTable.setItems(newCustomersTable);
            }  Alert alert = new Alert(Alert.AlertType.INFORMATION, "The record of the selected customer has been updated.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes customer from the database, along with the customer's appointments.
     * @param actionEvent
     * @throws SQLException
     */
    @FXML
    public void deleteCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        Customer customerClicked = customerTable.getSelectionModel().getSelectedItem();
        Connection connection = DBConnect.openConnection();
        if (customerClicked == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer not Selected");
            alert.setContentText("You must select a customer to delete.");
            alert.show();
        } else {
            Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this customer and their corresponding appointments?");
            Optional<ButtonType> validate = deleteCustomerAlert.showAndWait();
            if (validate.isPresent() && validate.get() == ButtonType.OK) {
                Boolean appointDelete = AppointmentDAO.deleteAppointAndCustomer(customerClicked.getCustomerID());
                Boolean customerDelete = CustomerDAO.deleteCustomer(customerClicked.getCustomerID());

                if (appointDelete && customerDelete) {
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    Alert customerDeleted = new Alert(Alert.AlertType.CONFIRMATION, "Selected customer and their corresponding appointments have been deleted.", ok);
                    customerDeleted.showAndWait();
                } else {
                    ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    Alert unsuccessfulDelete = new Alert(Alert.AlertType.WARNING, "Failed to delete customer or related appointments.", ok);
                    unsuccessfulDelete.showAndWait();
                    return;
                }
                try {
                    ObservableList<Customer> newCustomersTable = CustomerDAO.getCustomers(connection);
                    customerTable.setItems(newCustomersTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Creation and entry of the country and state/province combo boxes.
     * <br><br>
     * <p>
     *     <b>LAMBDA EXPRESSION - fills the first level division observable lists by easily pulling countryID data and assigning them to given values. </b>
     * </p>
     * @param actionEvent
     * @throws SQLException
     */
    public void updateCustomerCountryComboBox(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnect.openConnection();
            String countryClicked = updateCustomerCountry.getSelectionModel().getSelectedItem();
            ObservableList<DivisionDAO> maintainDivisions = DivisionDAO.getDivisions();
            ObservableList<String> US = FXCollections.observableArrayList();
            ObservableList<String> UK = FXCollections.observableArrayList();
            ObservableList<String> Canada = FXCollections.observableArrayList();
            //Lambda Expression - - fills the first level division observable lists by easily pulling countryID data and assigning them to given values.
            maintainDivisions.forEach(Division -> {
                if (Division.getCountryID() == 1) {
                    US.add(Division.getDivision());
                } else if (Division.getCountryID() == 2) {
                    UK.add(Division.getDivision());
                } else if (Division.getCountryID() == 3) {
                    Canada.add(Division.getDivision());
                }
            });
            switch (countryClicked) {
                case "U.S":
                    updateCustomerStateProv.setItems(US);
                    break;
                case "UK":
                    updateCustomerStateProv.setItems(UK);
                    break;
                case "Canada":
                    updateCustomerStateProv.setItems(Canada);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Takes user to the customers page when clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void customerToMainMenuButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToMain.setScene(newScene);
            returnToMain.show();
            returnToMain.centerOnScreen();
        }

    /**
     * Initializes data within the customers table.
     * <br><br>
     * <p>
     *     <b>LAMBDA EXPRESSION - fills first level division(maintainDivisions) observable list with the divisions.</b>
     * </p>
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DBConnect.openConnection();

            ObservableList<Customer> maintainCustomers = CustomerDAO.getCustomers(connection);
            ObservableList<CountryDAO> maintainCountries = CountryDAO.getCountries();
            ObservableList<String> countries = FXCollections.observableArrayList();
            ObservableList<DivisionDAO> maintainDivisions = DivisionDAO.getDivisions();
            ObservableList<String> divisions = FXCollections.observableArrayList();

            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
            customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            customerFirstLevelColumn.setCellValueFactory(new PropertyValueFactory<>("division"));

            maintainCountries.stream().map(Countries::getCountry).forEach(countries::add);
            updateCustomerCountry.setItems(countries);
            //Lambda Expression
            maintainDivisions.forEach(Division -> divisions.add(Division.getDivision()));
            updateCustomerStateProv.setItems(divisions);
            customerTable.setItems(maintainCustomers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the program.
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
     * Takes user to the Reports page when clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void viewReportsButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToReports = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToReports.setScene(newScene);
            returnToReports.show();
            returnToReports.centerOnScreen();
        }

    /**
     * Takes user to the Appointments page when clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void viewAppointmentsButtonClicked(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
            Scene newScene = new Scene(root);
            Stage returnToMain = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            returnToMain.setScene(newScene);
            returnToMain.show();
            returnToMain.centerOnScreen();
        }
}