package model;

import java.sql.Date;

public class FemaleCustomer extends Customer {
    private Date lastMammogram;

   /* public FemaleCustomer(String customerName, Date lastMammogram) {
        super(customerName);
        this.lastMammogram = lastMammogram;
    }

    public FemaleCustomer(String customerName, int customerId, Date lastMammogram) {
        super(customerName, customerId);
        this.lastMammogram = lastMammogram;
    }

    public FemaleCustomer(Date lastMammogram) {
        this.lastMammogram = lastMammogram;
    }

    public FemaleCustomer(int customerId, String customerName, String address, String postalCode, String phoneNumber, int divisionId, Date lastMammogram) {
        super(customerId, customerName, address, postalCode, phoneNumber, divisionId);
        this.lastMammogram = lastMammogram;
    }

    public FemaleCustomer(int customerId, String customerName, String address, String postalCode, String phoneNumber, int divisionId, String country, Date lastMammogram) {
        super(customerId, customerName, address, postalCode, phoneNumber, divisionId, country);
        this.lastMammogram = lastMammogram;
    }*/

    public FemaleCustomer(String customerName, String address, String postalCode, String phoneNumber, int divisionId, Date lastMammogram, String gender) {
        super(customerName, address, postalCode, phoneNumber, divisionId, gender);
        this.lastMammogram = lastMammogram;
    }

    public FemaleCustomer(int customerID, String customerName, String address, String postalCode, String phone, int divisionId, Date lastMammogram, String gender) {
        super(customerID, customerName, address, postalCode, phone, divisionId, gender);
        this.lastMammogram = lastMammogram;
    }

    public Date getLastMammogram() {
        return this.lastMammogram;
    }

    public void setLastMammogram(Date lastMammogram) {
        this.lastMammogram = lastMammogram;
    }
}
