package model;
/** Class provided to describe objects within Contacts.*/
public class Contact {
    public int contactID;
    public String contactName;
    public String contactEM;

    /**
     * List information within Contacts.
     * @param contactID
     * @param contactName
     * @param contactEM
     */
    public Contact(int contactID, String contactName, String contactEM) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEM = contactEM;
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
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName setter for contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return contactEM (Email)
     */
    public String getContactEM() {
        return contactEM;
    }

    /**
     * @param contactEM setter for contactEM (Email)
     */
    public void setContactEM(String contactEM) {
        this.contactEM = contactEM;
    }
}
