package model;

import java.util.LinkedHashMap;
import java.util.Map;

// This is Christina's test commit

/**
 * Customer - Mirrors the "Customer" table in your schema: User_ID (PK),
 * Cust_Start_Date, City, Zip Code, Street, Email, Phone #, Cust_Name, Type -
 * Follows the same style/pattern as your Warehouse class: * String-based fields
 * * No setter for the primary key (userId) * Per-field getters/setters and bulk
 * get/update helpers
 */
public final class Customer {
    // --- Fields (attributes) ---
    private String userId; // primary key
    private String custStartDate; // stored as String for simplicity
    private String city;
    private String zipCode;
    private String street;
    private String email;
    private String phoneNumber;
    private String custName;
    private String type;

    // --- Constructor ---
    public Customer(String userId, String custStartDate, String city, String zipCode,
            String street, String email, String phoneNumber, String custName,
            String type) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null/blank");
        }
        this.userId = userId;
        this.custStartDate = custStartDate;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.custName = custName;
        this.type = type;
    }

    // --- Getters (single-field reads) ---
    public String getUserId() {
        return this.userId;
    }

    public String getCustStartDate() {
        return this.custStartDate;
    }

    public String getCity() {
        return this.city;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getStreet() {
        return this.street;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCustName() {
        return this.custName;
    }

    public String getType() {
        return this.type;
    }

    // --- Setters (single-field updates; no setter for userId) ---
    public void setCustStartDate(String custStartDate) {
        this.custStartDate = custStartDate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setType(String type) {
        this.type = type;
    }

    // --- Bulk get: return all fields as a Map (handy for printing/JSON-like output) ---
    public Map<String, Object> getAll() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("userId", this.userId);
        m.put("custStartDate", this.custStartDate);
        m.put("city", this.city);
        m.put("zipCode", this.zipCode);
        m.put("street", this.street);
        m.put("email", this.email);
        m.put("phoneNumber", this.phoneNumber);
        m.put("custName", this.custName);
        m.put("type", this.type);
        return m;
    }

    // --- Bulk update: update all non-key fields in one call ---
    public void updateAll(String custStartDate, String city, String zipCode,
            String street, String email, String phoneNumber, String custName,
            String type) {
        this.setCustStartDate(custStartDate);
        this.setCity(city);
        this.setZipCode(zipCode);
        this.setStreet(street);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setCustName(custName);
        this.setType(type);
    }

    // --- toString for debugging/logging ---
    @Override
    public String toString() {
        return "Customer{" + "userId='" + this.userId + '\'' + ", custStartDate='"
                + this.custStartDate + '\'' + ", city='" + this.city + '\''
                + ", zipCode='" + this.zipCode + '\'' + ", street='" + this.street + '\''
                + ", email='" + this.email + '\'' + ", phoneNumber='" + this.phoneNumber
                + '\'' + ", custName='" + this.custName + '\'' + ", type='" + this.type
                + '\'' + '}';
    }
}
