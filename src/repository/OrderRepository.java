// ========================= repository/OrderRepository.java =========================
package repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.Order;

/**
 * OrderRepository - Stores Order entities in a Map<String orderId, Order>. -
 * Generates incremental String orderIds starting from "0". - Returns JSON
 * strings for convenience (same style as WarehouseRepository).
 */
public class OrderRepository {
    // Storage
    private final Map<String, Order> store = new LinkedHashMap<>();

    // ---------- Create ----------
    // orderId auto-increment; provide all other attributes
    public synchronized String create(String orderStartDate, String estimatedArrivalDate,
            String actualArrivalDate, String dueDate, String actualReturnDate,
            String custUserId) {
        String id = this.nextId();
        Order o = new Order(id, orderStartDate, estimatedArrivalDate, actualArrivalDate,
                dueDate, actualReturnDate, custUserId);
        this.store.put(id, o);
        return this.toJson(o);
    }

    // ---------- Read one ----------
    public synchronized String getById(String orderId) {
        Order o = this.store.get(orderId);
        return (o == null) ? this.errorJson("not_found", "orderId", orderId)
                : this.toJson(o);
    }

    // ---------- Read all ----------
    public synchronized String getAll() {
        String arr = this.store.values().stream().map(this::toJson)
                .collect(Collectors.joining(","));
        return "[" + arr + "]";
    }

    // ---------- Update (except id) ----------
    public synchronized String update(String orderId, String orderStartDate,
            String estimatedArrivalDate, String actualArrivalDate, String dueDate,
            String actualReturnDate, String custUserId) {
        Order existing = this.store.get(orderId);
        if (existing == null) {
            return this.errorJson("not_found", "orderId", orderId);
        }

        Order updated = new Order(orderId, orderStartDate, estimatedArrivalDate,
                actualArrivalDate, dueDate, actualReturnDate, custUserId);
        this.store.put(orderId, updated);
        return this.toJson(updated);
    }

    // ---------- Delete ----------
    public synchronized String delete(String orderId) {
        Order removed = this.store.remove(orderId);
        return (removed == null) ? this.errorJson("not_found", "orderId", orderId)
                : this.toJson(removed);
    }

    // ====================== Queries (exact match) ======================

    public String queryByCustUserId(String custUserId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Order o : this.store.values()) {
            if (this.equalsSafe(o.getCustUserId(), custUserId)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(o));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByOrderStartDate(String orderStartDate) {
        return this.queryByField("orderStartDate", orderStartDate);
    }

    public String queryByEstimatedArrivalDate(String est) {
        return this.queryByField("estimatedArrivalDate", est);
    }

    public String queryByActualArrivalDate(String act) {
        return this.queryByField("actualArrivalDate", act);
    }

    public String queryByDueDate(String due) {
        return this.queryByField("dueDate", due);
    }

    public String queryByActualReturnDate(String ret) {
        return this.queryByField("actualReturnDate", ret);
    }

    // Generic helper to reduce repetition for String fields
    private String queryByField(String which, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Order o : this.store.values()) {
            boolean match = false;
            if ("orderStartDate".equals(which)) {
                match = this.equalsSafe(o.getOrderStartDate(), value);
            } else if ("estimatedArrivalDate".equals(which)) {
                match = this.equalsSafe(o.getEstimatedArrivalDate(), value);
            } else if ("actualArrivalDate".equals(which)) {
                match = this.equalsSafe(o.getActualArrivalDate(), value);
            } else if ("dueDate".equals(which)) {
                match = this.equalsSafe(o.getDueDate(), value);
            } else if ("actualReturnDate".equals(which)) {
                match = this.equalsSafe(o.getActualReturnDate(), value);
            }

            if (match) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(o));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ====================== Helpers ======================

    // Generate next numeric String id
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

    // JSON serialization
    private String toJson(Order o) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"orderId\":\"").append(this.esc(o.getOrderId()))
                .append("\",").append("\"orderStartDate\":\"")
                .append(this.esc(o.getOrderStartDate())).append("\",")
                .append("\"estimatedArrivalDate\":\"")
                .append(this.esc(o.getEstimatedArrivalDate())).append("\",")
                .append("\"actualArrivalDate\":\"")
                .append(this.esc(o.getActualArrivalDate())).append("\",")
                .append("\"dueDate\":\"").append(this.esc(o.getDueDate())).append("\",")
                .append("\"actualReturnDate\":\"")
                .append(this.esc(o.getActualReturnDate())).append("\",")
                .append("\"custUserId\":\"").append(this.esc(o.getCustUserId()))
                .append("\"").append("}");
        return sb.toString();
    }

    // Error JSON
    private String errorJson(String code, String field, String value) {
        return "{" + "\"error\":\"" + this.esc(code) + "\"," + "\"field\":\""
                + this.esc(field) + "\"," + "\"value\":\"" + this.esc(value) + "\"" + "}";
    }

    // Minimal escaping for JSON strings
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

    // Exact, case-sensitive equality with null-safety
    private boolean equalsSafe(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}
