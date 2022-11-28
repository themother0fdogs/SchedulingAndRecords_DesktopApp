package Model;


/** This is the Customers class that stores the data of customer records from the mySQL database. */
public class Customers{

    private int customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String customerDivision;
    private String customerPostalCode;
    public String customerCountry;

    public Customers(int customerId, String customerName, String customerPhone, String customerAddress, String customerDivision, String customerPostalCode, String customerCountry) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerDivision = customerDivision;
        this.customerPostalCode = customerPostalCode;
        this.customerCountry = customerCountry;

    }
    /** @return the customer Id*/
    public int getCustomerId() {
        return customerId;
    }
    /**@param customerId customer Id to set*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**@return the customer name*/
    public String getCustomerName() {
        return customerName;
    }

    /**@param customerName the customer name to set */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**@return customer phone number */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**@param customerPhone customer phone number to set */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /** @return the customer address*/
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**@param customerAddress customer address to set */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /** @return the customer's division */
    public String getCustomerDivision() { return customerDivision; }

    /**@param customerDivision the customerDivision to set*/
    public void setCustomerDivision(String customerDivision) {
        this.customerDivision = customerDivision;
    }

    /**@return the customer's postal code */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**@param customerPostalCode the customer's postal code to set */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**@return customer's country*/
    public String getCustomerCountry() { return customerCountry; }

    /**@param customerCountry the customer's country to set */
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }
/**@return string of the customer's country*/
    public String toString(){ return customerCountry; }
}


