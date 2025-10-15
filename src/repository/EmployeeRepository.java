// ========================= repository/EmployeeRepository.java =========================
package repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.Employee;

/**
 * EmployeeRepository - Stores Employee entities in a Map<String ssn, Employee>.
 * - SSN is provided by caller (no auto-increment). - Returns JSON strings for
 * convenience.
 */
public class EmployeeRepository {
    private final Map<String, Employee> store = new LinkedHashMap<>();

    // ---------- Create (caller provides SSN) ----------
    public synchronized String create(String ssn, String name, String phoneNumber,
            String sex, int salary) {
        Employee e = new Employee(ssn, name, phoneNumber, sex, salary);
        this.store.put(ssn, e);
        return this.toJson(e);
    }

    // ---------- Read one ----------
    public synchronized String getById(String ssn) {
        Employee e = this.store.get(ssn);
        return (e == null) ? this.errorJson("not_found", "ssn", ssn) : this.toJson(e);
    }

    // ---------- Read all ----------
    public synchronized String getAll() {
        String arr = this.store.values().stream().map(this::toJson)
                .collect(Collectors.joining(","));
        return "[" + arr + "]";
    }

    // ---------- Update (except key) ----------
    public synchronized String update(String ssn, String name, String phoneNumber,
            String sex, int salary) {
        Employee existing = this.store.get(ssn);
        if (existing == null) {
            return this.errorJson("not_found", "ssn", ssn);
        }
        Employee updated = new Employee(ssn, name, phoneNumber, sex, salary);
        this.store.put(ssn, updated);
        return this.toJson(updated);
    }

    // ---------- Delete ----------
    public synchronized String delete(String ssn) {
        Employee removed = this.store.remove(ssn);
        return (removed == null) ? this.errorJson("not_found", "ssn", ssn)
                : this.toJson(removed);
    }

    // ================== Queries (exact match) ==================

    public String queryByName(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Employee e : this.store.values()) {
            if (this.equalsSafe(e.getName(), name)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(e));
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
        for (Employee e : this.store.values()) {
            if (this.equalsSafe(e.getPhoneNumber(), phoneNumber)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(e));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryBySex(String sex) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Employee e : this.store.values()) {
            if (this.equalsSafe(e.getSex(), sex)) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(e));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryBySalary(int salary) {
        StringBuilder sb = new StringBuilder();
        sb.append("[]");
        sb.setLength(1); // keep '['
        boolean first = true;
        for (Employee e : this.store.values()) {
            if (e.getSalary() == salary) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(e));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String queryBySalaryRange(int minInclusive, int maxInclusive) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Employee e : this.store.values()) {
            int v = e.getSalary();
            if (v >= minInclusive && v <= maxInclusive) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(this.toJson(e));
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ================== Helpers ==================

    private String toJson(Employee e) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"ssn\":\"").append(this.esc(e.getSsn())).append("\",")
                .append("\"name\":\"").append(this.esc(e.getName())).append("\",")
                .append("\"phoneNumber\":\"").append(this.esc(e.getPhoneNumber()))
                .append("\",").append("\"sex\":\"").append(this.esc(e.getSex()))
                .append("\",").append("\"salary\":").append(e.getSalary()).append("}");
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
