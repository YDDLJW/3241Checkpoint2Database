package model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Warehouse {
    // --- Fields (attributes) ---
    private String id;
    private String phoneNumber;
    private String city;
    private String zipCode;
    private String street;
    private int equipmentCapacity;
    private int droneCapacity;
    private String managerSSN;

    // --- Constructor ---
    public Warehouse(String id, String phoneNumber, String city, String zipCode,
            String street, int equipmentCapacity, int droneCapacity, String managerSSN) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null/blank");
        }
        if (equipmentCapacity < 0 || droneCapacity < 0) {
            throw new IllegalArgumentException("capacity values must be non-negative");
        }

        this.id = id;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.equipmentCapacity = equipmentCapacity;
        this.droneCapacity = droneCapacity;
        this.managerSSN = managerSSN;
    }

    // --- Getters (single-field reads) ---
    public String getId() {
        return this.id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
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

    public int getEquipmentCapacity() {
        return this.equipmentCapacity;
    }

    public int getDroneCapacity() {
        return this.droneCapacity;
    }

    public String getManagerSSN() {
        return this.managerSSN;
    }

    // --- Setters (single-field updates) ---
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public void setEquipmentCapacity(int equipmentCapacity) {
        if (equipmentCapacity < 0) {
            throw new IllegalArgumentException("equipmentCapacity must be non-negative");
        }
        this.equipmentCapacity = equipmentCapacity;
    }

    public void setDroneCapacity(int droneCapacity) {
        if (droneCapacity < 0) {
            throw new IllegalArgumentException("droneCapacity must be non-negative");
        }
        this.droneCapacity = droneCapacity;
    }

    public void setManagerSSN(String managerSSN) {
        this.managerSSN = managerSSN;
    }

    // --- Bulk get: return all fields as a Map (handy for printing/JSON-like output) ---
    public Map<String, Object> getAll() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", this.id);
        m.put("phoneNumber", this.phoneNumber);
        m.put("city", this.city);
        m.put("zipCode", this.zipCode);
        m.put("street", this.street);
        m.put("equipmentCapacity", this.equipmentCapacity);
        m.put("droneCapacity", this.droneCapacity);
        m.put("managerSSN", this.managerSSN);
        return m;
    }

    // --- Bulk update: update all fields in one call ---
    public void updateAll(String phoneNumber, String city, String zipCode, String street,
            int equipmentCapacity, int droneCapacity, String managerSSN) {
        this.setPhoneNumber(phoneNumber);
        this.setCity(city);
        this.setZipCode(zipCode);
        this.setStreet(street);
        this.setEquipmentCapacity(equipmentCapacity);
        this.setDroneCapacity(droneCapacity);
        this.setManagerSSN(managerSSN);
    }

    // --- toString for debugging/logging ---
    @Override
    public String toString() {
        return "Warehouse{" + "id='" + this.id + '\'' + ", phoneNumber='"
                + this.phoneNumber + '\'' + ", city='" + this.city + '\'' + ", zipCode='"
                + this.zipCode + '\'' + ", street='" + this.street + '\''
                + ", equipmentCapacity=" + this.equipmentCapacity + ", droneCapacity="
                + this.droneCapacity + ", managerSSN='" + this.managerSSN + '\'' + '}';
    }
}
