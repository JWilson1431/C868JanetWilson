package model;

import java.sql.Date;

public class MaleCustomer extends Customer {
    private Date lastProstateExam;

    public MaleCustomer(String customerName, Date lastProstateExam) {
        super(customerName);
        this.lastProstateExam = lastProstateExam;
    }

    public MaleCustomer(String customerName, int customerId, Date lastProstateExam) {
        super(customerName, customerId);
        this.lastProstateExam = lastProstateExam;
    }

    public MaleCustomer(Date lastProstateExam) {
        this.lastProstateExam = lastProstateExam;
    }

    public MaleCustomer(int customerId, String customerName, String address, String postalCode, String phoneNumber, int divisionId, Date lastProstateExam) {
        super(customerId, customerName, address, postalCode, phoneNumber, divisionId);
        this.lastProstateExam = lastProstateExam;
    }

    public MaleCustomer(int customerId, String customerName, String address, String postalCode, String phoneNumber, int divisionId, String country, Date lastProstateExam) {
        super(customerId, customerName, address, postalCode, phoneNumber, divisionId, country);
        this.lastProstateExam = lastProstateExam;
    }

    public MaleCustomer(String customerName, String address, String postalCode, String phoneNum, int divisionId, Date lastProstateExam, String gender) {
        super(customerName,address,postalCode,phoneNum,divisionId,gender);
        this.lastProstateExam=lastProstateExam;
    }

    public MaleCustomer(int customerID, String customerName, String address, String postalCode, String phone, int divisionId, Date lastProstateExam, String gender) {
        super(customerID, customerName, address, postalCode, phone, divisionId, gender);
        this.lastProstateExam = lastProstateExam;
    }

    public Date getLastProstateExam() {
        return this.lastProstateExam;
    }

    public void setLastProstateExam(Date lastProstateExam) {
        this.lastProstateExam = lastProstateExam;
    }
}
