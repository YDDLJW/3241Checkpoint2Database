package repository;

import java.util.LinkedHashMap;
import java.util.Map;

public class EquipmentManagement {

    private final Map<Integer, String> store = new LinkedHashMap<>();

    public void AddEquipment(int id, String name) {
        store.put(id, name);
    }
    public String RentEquipment(int id) {
        if (store.containsKey(id)) {
            return store.get(id);
        } else {
            return "Invalid";
        }
       
    }
    public void ReturnEquipment(int id) {
        if (store.containsKey(id)) {
            System.out.println("Equipment returned.");
        } else {
            System.out.println("This equipment is not in our system! Please try again.");
        }
    }
    public void DeliverEquipment(int id, int droneId, String date) {
        if (store.containsKey(id)) {
            System.out.println("Equipment delivered by drone " + droneId + " on " + date);
        } else {
            System.out.println("This equipment is not in our system! Please try again.");
        }
    }
    public void PickupEquipment(int id, int droneId, String date) {
        if (store.containsKey(id)) {
            System.out.println("Equipment scheduled to be picked up by drone " + droneId + " on " + date);
        } else {
            System.out.println("This equipment is not in our system! Please try again.");
        }
    }
}