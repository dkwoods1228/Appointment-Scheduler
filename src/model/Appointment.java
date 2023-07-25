package model;

import java.time.LocalDateTime;

/** Class provided to describe objects within Appointments.*/
public class Appointment {
    private int appointID;
    private String appointTitle;
    private String appointDescription;
    private String appointLocation;
    public int contactID;
    private String appointType;
    private LocalDateTime start;
    private LocalDateTime end;
    public int customerID;
    public int userID;

    /**
     * Lists information within Appointments.
     * @param appointID
     * @param appointTitle
     * @param appointDescription
     * @param appointLocation
     * @param contactID
     * @param appointType
     * @param start
     * @param end
     * @param customerID
     * @param userID
     */
    public Appointment(int appointID, String appointTitle, String appointDescription, String appointLocation, int contactID,
                       String appointType, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
        this.appointID = appointID;
        this.appointTitle = appointTitle;
        this.appointDescription = appointDescription;
        this.appointLocation = appointLocation;
        this.contactID = contactID;
        this.appointType = appointType;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    /**
     * @return appointID
     */
    public int getAppointID() {
        return appointID;
    }

    /**
     * @param appointID setter for appointID
     */
    public void setAppointID(int appointID) {
        this.appointID = appointID;
    }

    /**
     * @return appointTitle
     */
    public String getAppointTitle() {
        return appointTitle;
    }

    /**
     * @param appointTitle setter for appointTitle
     */
    public void setAppointTitle(String appointTitle) {
        this.appointTitle = appointTitle;
    }

    /**
     * @return appointDescription
     */
    public String getAppointDescription() {
        return appointDescription;
    }

    /**
     * @param appointDescription setter for appointDescription
     */
    public void setAppointDescription(String appointDescription) {
        this.appointDescription = appointDescription;
    }

    /**
     * @return appointLocation
     */
    public String getAppointLocation() {
        return appointLocation;
    }

    /**
     * @param appointLocation setter for appointLocation
     */
    public void setAppointLocation(String appointLocation) {
        this.appointLocation = appointLocation;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID setter for contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return appointType
     */
    public String getAppointType() {
        return appointType;
    }

    /**
     * @param appointType setter for appointType
     */
    public void setAppointType(String appointType) {
        this.appointType = appointType;
    }

    /**
     * @return start (date and time)
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start setter for start date and time
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    /**
     * @return end (date and time)
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     *
     * @param end setter for end date and time
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
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
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID setter for userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
