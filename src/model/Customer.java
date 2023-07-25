package model;
/** Class provided to describe objects within Customers.*/
public class Customer {
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostal;
    private String customerPhone;
    private int divisionID;
    private String division;

    /**
     * Lists information within Customers
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerPostal
     * @param customerPhone
     * @param divisionID
     * @param division
     */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostal, String customerPhone,
                    int divisionID, String division) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerPostal = customerPostal;
        this.divisionID = divisionID;
        this.division = division;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID setter for customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName setter for customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /**
     * @return customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress setter for customerAddress
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    /**
     * @return customerPostal
     */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /**
     * @param customerPostal setter for customerPostal
     */
    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }
    /**
     * @return customerPhone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone setter for customerPhone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID setter for divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division setter for division
     */
    public void setDivision(String division) {
        this.division = division;
    }
}
