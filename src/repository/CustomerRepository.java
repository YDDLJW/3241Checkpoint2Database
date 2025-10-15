package repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.Customer;

/**
 * CustomerRepository - Stores Customer entities in a Map<String userId,
 * Customer>. - Generates incremental String userIds starting from "0". - All
 * public methods return JSON-formatted strings for convenience.
 *
 * Fields (from schema): userId (PK), custStartDate, city, zipCode, street,
 * email, phoneNumber, custName, type
 */
public class CustomerRepository {
    // --- Storage (keeps insertion order for predictable listing) ---
    private final Map<String, Customer> store = new LinkedHashMap<>();

    // --- Create: add a new Customer with auto-increment userId (as String) ---
    // The id is generated from existing numeric ids, starting at "0".
    public synchronized String create(String custStartDate, String city, String zipCode,
            String street, String email, String phoneNumber, String custName,
            String type) {
        String userId = this.nextId();
        Customer c = new Customer(userId, custStartDate, city, zipCode, street, email,
                phoneNumber, custName, type);
        this.store.put(userId, c);
        return this.toJson(c);
    }

    // --- Read one: get a customer by userId as JSON ---
    public synchronized String getById(String userId) {
        Customer c = this.store.get(userId);
        return (c == null) ? this.errorJson("not_found", "userId", userId)
                : this.toJson(c);
    }

    // --- Read all: return all customers as a JSON array ---
    public synchronized String getAll() {
        String arr = this.store.values().stream().map(this::toJson)
                .collect(Collectors.joining(","));
        return "[" + arr + "]";
    }

    // --- Update: set all attributes (except userId) for the given userId ---
    // Returns the updated customer as JSON; if not found, returns an error JSON.
    public synchronized String update(String userId, String custStartDate, String city,
            String zipCode, String street, String email, String phoneNumber,
            String custName, String type) {
        Customer existing = this.store.get(userId);
        if (existing == null) {
            return this.errorJson("not_found", "userId", userId);
        }

        Customer updated = new Customer(userId, custStartDate, city, zipCode, street,
                email, phoneNumber, custName, type);
        this.store.put(userId, updated);
        return this.toJson(updated);
    }

    // --- Delete: remove by userId and return the deleted entity as JSON ---
    public synchronized String delete(String userId) {
        Customer removed = this.store.remove(userId);
        return (removed == null) ? this.errorJson("not_found", "userId", userId)
                : this.toJson(removed);
    }

    // ====================== Helpers ======================

    // Generate the next numeric String id based on current keys.
    // If store is empty -> "0"; otherwise max(existing) + 1.
    private String nextId() {
        if (this.store.isEmpty()) {
            return "0";
        }
        int max = -1;
        for (String k : this.store.keySet()) {
            try {
                int v = Integer.parseInt(k);
                if (v > max) {
                    max = v;
                }
            } catch (NumberFormatException ignore) {
                // ignore non-numeric keys
            }
        }
        return String.valueOf(max + 1);
    }

    // Convert one Customer to a JSON object string.
    private String toJson(Customer c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"userId\":\"").append(this.esc(c.getUserId()))
                .append("\",").append("\"custStartDate\":\"")
                .append(this.esc(c.getCustStartDate())).append("\",")
                .append("\"city\":\"").append(this.esc(c.getCity())).append("\",")
                .append("\"zipCode\":\"").append(this.esc(c.getZipCode())).append("\",")
                .append("\"street\":\"").append(this.esc(c.getStreet())).append("\",")
                .append("\"email\":\"").append(this.esc(c.getEmail())).append("\",")
                .append("\"phoneNumber\":\"").append(this.esc(c.getPhoneNumber()))
                .append("\",").append("\"custName\":\"").append(this.esc(c.getCustName()))
                .append("\",").append("\"type\":\"").append(this.esc(c.getType()))
                .append("\"").append("}");
        return sb.toString();
    }

    // Simple error JSON helper
    private String errorJson(String code, String field, String value) {
        return "{" + "\"error\":\"" + this.esc(code) + "\"," + "\"field\":\""
                + this.esc(field) + "\"," + "\"value\":\"" + this.esc(value) + "\"" + "}";
    }

    // Minimal JSON string escape (quotes and backslashes)
    private String esc(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '\"') {
                out.append('\\').append(c);
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    // ================== Single-attribute query methods (exact match) ==================

    public String queryByCity(String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getCity(), city)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByZipCode(String zipCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getZipCode(), zipCode)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByEmail(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getEmail(), email)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getPhoneNumber(), phoneNumber)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByCustName(String custName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getCustName(), custName)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByType(String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getType(), type)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByCustStartDate(String custStartDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Customer c : this.store.values()) {
            if (this.equalsSafe(c.getCustStartDate(), custStartDate)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(c));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ================== String helper ==================
    private boolean equalsSafe(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b); // exact, case-sensitive
    }
}
