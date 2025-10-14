package repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.Warehouse;

/**
 * WarehouseRepository - Stores Warehouse entities in a Map<String id,
 * Warehouse>. - Generates incremental String ids starting from "0". - All
 * public methods return JSON-formatted strings for convenience.
 */
public class WarehouseRepository {
    // --- Storage (keeps insertion order for predictable listing) ---
    private final Map<String, Warehouse> store = new LinkedHashMap<>();

    // --- Create: add a new Warehouse with auto-increment id (as String) ---
    // The id is generated from existing numeric ids, starting at "0".
    public synchronized String create(String phoneNumber, String city, String zipCode,
            String street, int equipmentCapacity, int droneCapacity, String managerSSN) {
        String id = this.nextId();
        Warehouse w = new Warehouse(id, phoneNumber, city, zipCode, street,
                equipmentCapacity, droneCapacity, managerSSN);
        this.store.put(id, w);
        return this.toJson(w);
    }

    // --- Read one: get a warehouse by id as JSON ---
    public synchronized String getById(String id) {
        Warehouse w = this.store.get(id);
        return (w == null) ? this.errorJson("not_found", "id", id) : this.toJson(w);
    }

    // --- Read all: return all warehouses as a JSON array ---
    public synchronized String getAll() {
        String arr = this.store.values().stream().map(this::toJson)
                .collect(Collectors.joining(","));
        return "[" + arr + "]";
    }

    // --- Update: set all attributes (except id) for the warehouse with the given id ---
    // Returns the updated warehouse as JSON; if not found, returns an error JSON.
    public synchronized String update(String id, String phoneNumber, String city,
            String zipCode, String street, int equipmentCapacity, int droneCapacity,
            String managerSSN) {
        Warehouse existing = this.store.get(id);
        if (existing == null) {
            return this.errorJson("not_found", "id", id);
        }

        Warehouse updated = new Warehouse(id, phoneNumber, city, zipCode, street,
                equipmentCapacity, droneCapacity, managerSSN);
        this.store.put(id, updated);
        return this.toJson(updated);
    }

    // --- Delete: remove by id and return the deleted entity as JSON ---
    public synchronized String delete(String id) {
        Warehouse removed = this.store.remove(id);
        return (removed == null) ? this.errorJson("not_found", "id", id)
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
            // Try to parse numeric ids; ignore non-numeric keys if any appear.
            try {
                int v = Integer.parseInt(k);
                if (v > max) {
                    max = v;
                }
            } catch (NumberFormatException ignore) {
                // If a non-numeric id exists, we simply ignore it for auto-increment purposes.
            }
        }
        return String.valueOf(max + 1);
    }

    // Convert one Warehouse to a JSON object string (manual, minimal escaping).
    private String toJson(Warehouse w) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"id\":\"").append(this.esc(w.getId())).append("\",")
                .append("\"phoneNumber\":\"").append(this.esc(w.getPhoneNumber()))
                .append("\",").append("\"city\":\"").append(this.esc(w.getCity()))
                .append("\",").append("\"zipCode\":\"").append(this.esc(w.getZipCode()))
                .append("\",").append("\"street\":\"").append(this.esc(w.getStreet()))
                .append("\",").append("\"equipmentCapacity\":")
                .append(w.getEquipmentCapacity()).append(",").append("\"droneCapacity\":")
                .append(w.getDroneCapacity()).append(",").append("\"managerSSN\":\"")
                .append(this.esc(w.getManagerSSN())).append("\"").append("}");
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

    // ================== Single-attribute query methods ==================

    public String queryByCity(String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            if (this.equalsSafe(w.getCity(), city)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
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
        for (Warehouse w : this.store.values()) {
            if (this.equalsSafe(w.getZipCode(), zipCode)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByManagerSSN(String managerSSN) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            if (this.equalsSafe(w.getManagerSSN(), managerSSN)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
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
        for (Warehouse w : this.store.values()) {
            if (this.equalsSafe(w.getPhoneNumber(), phoneNumber)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByStreet(String street) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            if (this.equalsSafe(w.getStreet(), street)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Capacity equals / ranges (exact numeric comparisons)

    public String queryByEquipmentCapacity(int capacity) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            if (w.getEquipmentCapacity() == capacity) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByEquipmentCapacityRange(int minInclusive, int maxInclusive) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            int v = w.getEquipmentCapacity();
            if (v >= minInclusive && v <= maxInclusive) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByDroneCapacity(int capacity) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            if (w.getDroneCapacity() == capacity) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByDroneCapacityRange(int minInclusive, int maxInclusive) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Warehouse w : this.store.values()) {
            int v = w.getDroneCapacity();
            if (v >= minInclusive && v <= maxInclusive) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(w));
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
