package Model;

/** This is the Contacts class that stores the data for contacts from the mySQL database. */
public class Contacts {
    int contactId;
    String contactName;
    String contactEmail;

    public Contacts(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**@return contactId*/
    public int getContactId() {
        return contactId;
    }

    /**@param contactId contactId to set*/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**@return contact name*/
    public String getContactName() {
        return contactName;
    }
    /**@param contactName contact name to set*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**@return contact's email*/
    public String getContactEmail() {
        return contactEmail;
    }
    /**@param contactEmail contact's email to set*/
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
/**@return string of contact names */
    public String toString(){
        return(contactName);
    }
}
