package model;

public class Contact {
    public int contactID;
    public String contactName;
    public String contactEM;

    public Contact(int contactID, String contactName, String contactEM) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEM = contactEM;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEM() {
        return contactEM;
    }

    public void setContactEM(String contactEM) {
        this.contactEM = contactEM;
    }
}
