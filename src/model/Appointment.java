package model;

import java.time.LocalDateTime;

public class Appointment {
    private int appointID;
    private String appointTitle;
    private String appointDescription;
    private String appointLocation;
    private int contactID;
    private String appointType;
    private LocalDateTime start;
    private LocalDateTime end;
    private int appointCustomerID;
    private int userID;

    public Appointment(int appointID, String appointTitle, String appointDescription, String appointLocation, int contactID,
                       String appointType, LocalDateTime start, LocalDateTime end, int appointCustomerID, int userID) {
        this.appointID = appointID;
        this.appointTitle = appointTitle;
        this.appointDescription = appointDescription;
        this.appointLocation = appointLocation;
        this.contactID = contactID;
        this.appointType = appointType;
        this.start = start;
        this.end = end;
        this.appointCustomerID = appointCustomerID;
        this.userID = userID;
    }

    public int getAppointID() {
        return appointID;
    }

    public void setAppointID(int appointID) {
        this.appointID = appointID;
    }

    public String getAppointTitle() {
        return appointTitle;
    }

    public void setAppointTitle(String appointTitle) {
        this.appointTitle = appointTitle;
    }

    public String getAppointDescription() {
        return appointDescription;
    }

    public void setAppointDescription(String appointDescription) {
        this.appointDescription = appointDescription;
    }

    public String getAppointLocation() {
        return appointLocation;
    }

    public void setAppointLocation(String appointLocation) {
        this.appointLocation = appointLocation;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getAppointType() {
        return appointType;
    }

    public void setAppointType(String appointType) {
        this.appointType = appointType;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getAppointCustomerID() {
        return appointCustomerID;
    }

    public void setAppointCustomerID(int appointCustomerID) {
        this.appointCustomerID = appointCustomerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
