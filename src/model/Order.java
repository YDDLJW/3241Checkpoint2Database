// ========================= model/Order.java =========================
package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Order Mirrors the Orders table: Order# (PK), Order_Start_Date, Estimated
 * Arrival Date, Actual Arrival Date, Due Date, Actual Return Date, Cust_User_ID
 * All date-like values are stored as String for simplicity (same style as
 * Warehouse/Customer).
 */
public final class Order {
    // --- Fields ---
    private final String orderId; // primary key: Order #
    private String orderStartDate;
    private String estimatedArrivalDate;
    private String actualArrivalDate;
    private String dueDate;
    private String actualReturnDate;
    private String custUserId;

    // --- Constructor ---
    public Order(String orderId, String orderStartDate, String estimatedArrivalDate,
            String actualArrivalDate, String dueDate, String actualReturnDate,
            String custUserId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be null/blank");
        }
        this.orderId = orderId;
        this.orderStartDate = orderStartDate;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.actualArrivalDate = actualArrivalDate;
        this.dueDate = dueDate;
        this.actualReturnDate = actualReturnDate;
        this.custUserId = custUserId;
    }

    // --- Getters ---
    public String getOrderId() {
        return this.orderId;
    }

    public String getOrderStartDate() {
        return this.orderStartDate;
    }

    public String getEstimatedArrivalDate() {
        return this.estimatedArrivalDate;
    }

    public String getActualArrivalDate() {
        return this.actualArrivalDate;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public String getActualReturnDate() {
        return this.actualReturnDate;
    }

    public String getCustUserId() {
        return this.custUserId;
    }

    // --- Setters (no setter for orderId) ---
    public void setOrderStartDate(String v) {
        this.orderStartDate = v;
    }

    public void setEstimatedArrivalDate(String v) {
        this.estimatedArrivalDate = v;
    }

    public void setActualArrivalDate(String v) {
        this.actualArrivalDate = v;
    }

    public void setDueDate(String v) {
        this.dueDate = v;
    }

    public void setActualReturnDate(String v) {
        this.actualReturnDate = v;
    }

    public void setCustUserId(String v) {
        this.custUserId = v;
    }

    // --- Bulk get: JSON-like map ---
    public Map<String, Object> getAll() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderId", this.orderId);
        m.put("orderStartDate", this.orderStartDate);
        m.put("estimatedArrivalDate", this.estimatedArrivalDate);
        m.put("actualArrivalDate", this.actualArrivalDate);
        m.put("dueDate", this.dueDate);
        m.put("actualReturnDate", this.actualReturnDate);
        m.put("custUserId", this.custUserId);
        return m;
    }

    // --- Bulk update (except id) ---
    public void updateAll(String orderStartDate, String estimatedArrivalDate,
            String actualArrivalDate, String dueDate, String actualReturnDate,
            String custUserId) {
        this.setOrderStartDate(orderStartDate);
        this.setEstimatedArrivalDate(estimatedArrivalDate);
        this.setActualArrivalDate(actualArrivalDate);
        this.setDueDate(dueDate);
        this.setActualReturnDate(actualReturnDate);
        this.setCustUserId(custUserId);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Order{" + "orderId='" + this.orderId + '\'' + ", orderStartDate='"
                + this.orderStartDate + '\'' + ", estimatedArrivalDate='"
                + this.estimatedArrivalDate + '\'' + ", actualArrivalDate='"
                + this.actualArrivalDate + '\'' + ", dueDate='" + this.dueDate + '\''
                + ", actualReturnDate='" + this.actualReturnDate + '\'' + ", custUserId='"
                + this.custUserId + '\'' + '}';
    }
}
