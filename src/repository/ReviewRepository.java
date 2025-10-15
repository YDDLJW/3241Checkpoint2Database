// ========================= repository/ReviewRepository.java =========================
package repository;

import java.util.LinkedHashMap;
import java.util.Map;

import model.Review;

/**
 * ReviewRepository - Stores Review entities in a Map<String compositeKey,
 * Review>. - compositeKey format: orderId + "|" + userId - Public methods
 * return JSON-formatted strings for convenience.
 */
public class ReviewRepository {
    private final Map<String, Review> store = new LinkedHashMap<>();

    // ---------- Create ----------
    // Keys are provided by caller because PK is composite: (orderId, userId)
    public synchronized String create(String orderId, String userId, String comments,
            int ratings) {
        String k = this.key(orderId, userId);
        Review r = new Review(orderId, userId, comments, ratings);
        this.store.put(k, r);
        return this.toJson(r);
    }

    // ---------- Read one ----------
    public synchronized String getById(String orderId, String userId) {
        Review r = this.store.get(this.key(orderId, userId));
        return (r == null)
                ? this.errorJson("not_found", "compositeKey", this.key(orderId, userId))
                : this.toJson(r);
    }

    // ---------- Read all ----------
    public synchronized String getAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Review r : this.store.values()) {
            if (!first) {
                sb.append(",");
            }
            sb.append(this.toJson(r));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    // ---------- Update (except keys) ----------
    public synchronized String update(String orderId, String userId, String comments,
            int ratings) {
        String k = this.key(orderId, userId);
        Review existing = this.store.get(k);
        if (existing == null) {
            return this.errorJson("not_found", "compositeKey", k);
        }
        Review updated = new Review(orderId, userId, comments, ratings);
        this.store.put(k, updated);
        return this.toJson(updated);
    }

    // ---------- Delete ----------
    public synchronized String delete(String orderId, String userId) {
        String k = this.key(orderId, userId);
        Review removed = this.store.remove(k);
        return (removed == null) ? this.errorJson("not_found", "compositeKey", k)
                : this.toJson(removed);
    }

    // ====================== Queries (exact match) ======================

    public String queryByOrderId(String orderId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Review r : this.store.values()) {
            if (this.equalsSafe(r.getOrderId(), orderId)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(r));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByUserId(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Review r : this.store.values()) {
            if (this.equalsSafe(r.getUserId(), userId)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(r));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByRatings(int ratings) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Review r : this.store.values()) {
            if (r.getRatings() == ratings) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(r));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryByComments(String comments) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Review r : this.store.values()) {
            if (this.equalsSafe(r.getComments(), comments)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(r));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ====================== Helpers ======================

    private String key(String orderId, String userId) {
        return (orderId == null ? "" : orderId) + "|" + (userId == null ? "" : userId);
    }

    private String toJson(Review r) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"orderId\":\"").append(this.esc(r.getOrderId()))
                .append("\",").append("\"userId\":\"").append(this.esc(r.getUserId()))
                .append("\",").append("\"comments\":\"").append(this.esc(r.getComments()))
                .append("\",").append("\"ratings\":").append(r.getRatings()).append("}");
        return sb.toString();
    }

    private String errorJson(String code, String field, String value) {
        return "{" + "\"error\":\"" + this.esc(code) + "\"," + "\"field\":\""
                + this.esc(field) + "\"," + "\"value\":\"" + this.esc(value) + "\"" + "}";
    }

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
