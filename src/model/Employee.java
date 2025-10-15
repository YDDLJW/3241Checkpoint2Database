// ========================= model/Employee.java =========================
package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Employee Mirrors the Employee table: SSN (PK), Name, Phone #, Sex, Salary
 * Keep SSN immutable (no setter).
 */
public final class Employee {
    // --- Fields ---
    private final String ssn; // primary key
    private String name;
    private String phoneNumber;
    private String sex;
    private int salary;

    // --- Constructor ---
    public Employee(String ssn, String name, String phoneNumber, String sex, int salary) {
        if (ssn == null || ssn.isBlank()) {
            throw new IllegalArgumentException("ssn cannot be null/blank");
        }
        this.ssn = ssn;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.salary = salary;
    }

    // --- Getters ---
    public String getSsn() {
        return this.ssn;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getSex() {
        return this.sex;
    }

    public int getSalary() {
        return this.salary;
    }

    // --- Setters (no setter for ssn) ---
    public void setName(String v) {
        this.name = v;
    }

    public void setPhoneNumber(String v) {
        this.phoneNumber = v;
    }

    public void setSex(String v) {
        this.sex = v;
    }

    public void setSalary(int v) {
        this.salary = v;
    }

    // --- Bulk get ---
    public Map<String, Object> getAll() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ssn", this.ssn);
        m.put("name", this.name);
        m.put("phoneNumber", this.phoneNumber);
        m.put("sex", this.sex);
        m.put("salary", this.salary);
        return m;
    }

    // --- Bulk update (except key) ---
    public void updateAll(String name, String phoneNumber, String sex, int salary) {
        this.setName(name);
        this.setPhoneNumber(phoneNumber);
        this.setSex(sex);
        this.setSalary(salary);
    }

    @Override
    public String toString() {
        return "Employee{" + "ssn='" + this.ssn + '\'' + ", name='" + this.name + '\''
                + ", phoneNumber='" + this.phoneNumber + '\'' + ", sex='" + this.sex
                + '\'' + ", salary=" + this.salary + '}';
    }
}
