// ========================= model/Review.java =========================
package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Review Mirrors the Reviews table: Order# (orderId), User_ID (userId),
 * Comments, Ratings Primary key is composite: (orderId, userId).
 */
public final class Review {
    // --- Fields ---
    private final String orderId; // part of composite PK
    private final String userId; // part of composite PK
    private String comments;
    private int ratings; // store as int for simplicity

    // --- Constructor ---
    public Review(String orderId, String userId, String comments, int ratings) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be null/blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null/blank");
        }
        this.orderId = orderId;
        this.userId = userId;
        this.comments = comments;
        this.ratings = ratings; // you can clamp/validate range if desired
    }

    // --- Getters ---
    public String getOrderId() {
        return this.orderId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getComments() {
        return this.comments;
    }

    public int getRatings() {
        return this.ratings;
    }

    // --- Setters (no setters for keys) ---
    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    // --- Bulk get ---
    public Map<String, Object> getAll() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderId", this.orderId);
        m.put("userId", this.userId);
        m.put("comments", this.comments);
        m.put("ratings", this.ratings);
        return m;
    }

    // --- Bulk update (except keys) ---
    public void updateAll(String comments, int ratings) {
        this.setComments(comments);
        this.setRatings(ratings);
    }

    @Override
    public String toString() {
        return "Review{" + "orderId='" + this.orderId + '\'' + ", userId='" + this.userId
                + '\'' + ", comments='" + this.comments + '\'' + ", ratings="
                + this.ratings + '}';
    }
}
