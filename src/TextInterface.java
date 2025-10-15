import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import repository.WarehouseRepository;
import repository.EquipmentManagement;

/**
 * TextInterface - Console-based entrypoint. - Hosts repositories (currently
 * only WarehouseRepository). - Presents menus and routes user input to
 * CRUD/query handlers. - Uses simple JSON strings returned by the repository
 * for display. - No lambdas/streams; Allman brace style.
 */
public class TextInterface {
    // Repositories (currently only Warehouse)
    private final WarehouseRepository warehouseRepo = new WarehouseRepository();
    private final EquipmentManagement equipmentRepo = new EquipmentManagement();

    // Console scanner
    private final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        new TextInterface().run();
    }

    // ============================== Entry ==============================

    /**
     * Main loop for the top-level menu.
     */
    public void run() {
        while (true) {
            this.printMainMenu();
            String choice = this.readLine("Please enter a number: ");
            if ("1".equals(choice)) {
                this.warehouseMenu();
            }
            else if ("2".equals(choice)) {
                this.equipmentManagementMenu();
            }
            else if ("0".equals(choice)) {
                this.println("Bye!");
                break;
            } else {
                this.println("[Input Error] Unknown choice: " + choice);
            }
        }
    }

    /**
     * Prints the main menu.
     */
    private void printMainMenu() {
        this.println("");
        this.println("Main menu:");
        this.println("1. Warehouse");
        this.println("2. Equipment Management");
        this.println("0. Exit");
    }

    // ============================== Warehouse menu ==============================

    /**
     * Menu loop for the Warehouse entity. Shows the current table and provides
     * CRUD + query options.
     */
    private void warehouseMenu() {
        while (true) {
            this.println("");
            this.println("=== Warehouse Table ===");
            this.println("Current data:");
            this.println(this.warehouseRepo.getAll()); // JSON array string

            this.println("");
            this.println("Operations:");
            this.println("1. Create");
            this.println("2. Update");
            this.println("3. Query");
            this.println("4. Delete");
            this.println("5. Return to Main menu");

            String op = this.readLine("Enter 1/2/3/4/5: ");

            if ("1".equals(op)) {
                this.handleWarehouseCreate();
            } else if ("2".equals(op)) {
                this.handleWarehouseUpdate();
            } else if ("3".equals(op)) {
                this.handleWarehouseQuery();
            } else if ("4".equals(op)) {
                this.handleWarehouseDelete();
            } else if ("5".equals(op)) {
                return; // back to main menu
            } else {
                this.println("[Input Error] Unknown operation: " + op);
            }
        }
    }

    private void equipmentManagementMenu() {
        while (true) {
            this.println("");
            this.println("=== Equipment Management ===");
            this.println("Current data:");
            this.println(this.equipmentRepo.toString());

            this.println("");
            this.println("Operations:");
            this.println("1. Add Equipment");
            this.println("2. Rent Equipment");
            this.println("3. Return Equipment");
            this.println("4. Deliver Equipment");
            this.println("5. Pickup Equipment");
            this.println("6. Return to Main menu");

            String op = this.readLine("Enter 1/2/3/4/5/6: ");
            if ("1".equals(op)) {
                this.handleEquipmentAdd();
            } else if ("2".equals(op)) {
                this.handleEquipmentRent();
            } else if ("3".equals(op)) {
                this.handleEquipmentReturn();
            } else if ("4".equals(op)) {
                this.handleEquipmentDelivery();
            } else if ("5".equals(op)) {
                this.handleEquipmentPickup();
            } else if ("6".equals(op)) {
                return; // back to main menu
            } else {
                this.println("[Input Error] Unknown operation: " + op);
            }
        }
    }

    // ============================== CRUD Handlers ==============================

    /**
     * Create handler. Prompts the user to input attributes in the format:
     * {phoneNumber, city, zipCode, street, equipmentCapacity, droneCapacity,
     * managerSSN} The id is auto-incremented inside the repository, starting at
     * "0".
     */
    private void handleWarehouseCreate() {
        this.println("");
        this.println("Create Warehouse");
        this.println(
                "Please input attributes in format {phoneNumber, city, zipCode, street, equipmentCapacity, droneCapacity, managerSSN}");
        String line = this.readLine("> ");

        List<String> parts = this.parseBraceList(line);
        if (parts == null || parts.size() != 7) {
            this.println("[Input Error] Expect 7 attributes inside braces.");
            return;
        }

        String phoneNumber = parts.get(0);
        String city = parts.get(1);
        String zipCode = parts.get(2);
        String street = parts.get(3);
        Integer equipmentCapacity = this.parseInt(parts.get(4));
        Integer droneCapacity = this.parseInt(parts.get(5));
        String managerSSN = parts.get(6);

        if (equipmentCapacity == null || droneCapacity == null) {
            this.println(
                    "[Input Error] equipmentCapacity / droneCapacity must be integers.");
            return;
        }

        String json = this.warehouseRepo.create(phoneNumber, city, zipCode, street,
                equipmentCapacity, droneCapacity, managerSSN);
        this.println("Created:");
        this.println(json);
    }

    /**
     * Update handler. First asks for the id to update. Then prompts for
     * attributes in the same format as create: {phoneNumber, city, zipCode,
     * street, equipmentCapacity, droneCapacity, managerSSN}
     */
    private void handleWarehouseUpdate() {
        this.println("");
        this.println("Update Warehouse");
        String id = this.readLine("Enter id: ");

        this.println(
                "Please input attributes in format {phoneNumber, city, zipCode, street, equipmentCapacity, droneCapacity, managerSSN}");
        String line = this.readLine("> ");

        List<String> parts = this.parseBraceList(line);
        if (parts == null || parts.size() != 7) {
            this.println("[Input Error] Expect 7 attributes inside braces.");
            return;
        }

        String phoneNumber = parts.get(0);
        String city = parts.get(1);
        String zipCode = parts.get(2);
        String street = parts.get(3);
        Integer equipmentCapacity = this.parseInt(parts.get(4));
        Integer droneCapacity = this.parseInt(parts.get(5));
        String managerSSN = parts.get(6);

        if (equipmentCapacity == null || droneCapacity == null) {
            this.println(
                    "[Input Error] equipmentCapacity / droneCapacity must be integers.");
            return;
        }

        String json = this.warehouseRepo.update(id, phoneNumber, city, zipCode, street,
                equipmentCapacity, droneCapacity, managerSSN);
        this.println("Updated (or error):");
        this.println(json);
    }

    /**
     * Query handler. Asks which field to query by, then asks for the value.
     * Exact matching only (case-sensitive), consistent with repository methods.
     * After showing results, asks the user for next action (1/2/3/4/5). Option
     * 5 returns to the Warehouse menu (not the main menu).
     */
    private void handleWarehouseQuery() {
        while (true) {
            this.println("");
            this.println("Query by which field?");
            this.println(
                    "Options: city | zipCode | managerSSN | phoneNumber | street | equipmentCapacity | droneCapacity");
            this.println("Or enter 9 to return to Warehouse menu.");
            String field = this.readLine("Field: ");

            if ("9".equals(field)) {
                return; // back to Warehouse menu
            }

            String resultJson = null;

            if ("city".equals(field)) {
                String val = this.readLine("Enter city: ");
                resultJson = this.warehouseRepo.queryByCity(val);
            } else if ("zipCode".equals(field)) {
                String val = this.readLine("Enter zipCode: ");
                resultJson = this.warehouseRepo.queryByZipCode(val);
            } else if ("managerSSN".equals(field)) {
                String val = this.readLine("Enter managerSSN: ");
                resultJson = this.warehouseRepo.queryByManagerSSN(val);
            } else if ("phoneNumber".equals(field)) {
                String val = this.readLine("Enter phoneNumber: ");
                resultJson = this.warehouseRepo.queryByPhoneNumber(val);
            } else if ("street".equals(field)) {
                String val = this.readLine("Enter street: ");
                resultJson = this.warehouseRepo.queryByStreet(val);
            } else if ("equipmentCapacity".equals(field)) {
                String val = this.readLine("Enter equipmentCapacity (int): ");
                Integer num = this.parseInt(val);
                if (num == null) {
                    this.println("[Input Error] equipmentCapacity must be an integer.");
                    continue;
                }
                resultJson = this.warehouseRepo.queryByEquipmentCapacity(num);
            } else if ("droneCapacity".equals(field)) {
                String val = this.readLine("Enter droneCapacity (int): ");
                Integer num = this.parseInt(val);
                if (num == null) {
                    this.println("[Input Error] droneCapacity must be an integer.");
                    continue;
                }
                resultJson = this.warehouseRepo.queryByDroneCapacity(num);
            } else {
                this.println("[Input Error] Unknown field: " + field);
                continue;
            }

            this.println("Query result:");
            this.println(resultJson);

            // Next-step menu for convenience
            this.println("");
            this.println("Next step:");
            this.println(
                    "1. Create   2. Update   3. Query   4. Delete   5. Back to Warehouse menu");
            String next = this.readLine("Enter 1/2/3/4/5: ");
            if ("1".equals(next)) {
                this.handleWarehouseCreate();
            } else if ("2".equals(next)) {
                this.handleWarehouseUpdate();
            } else if ("3".equals(next)) {
                // Stay in the query loop to perform another query
                continue;
            } else if ("4".equals(next)) {
                this.handleWarehouseDelete();
            } else if ("5".equals(next)) {
                return; // back to Warehouse menu
            } else {
                this.println("[Input Error] Unknown choice: " + next);
                // stay in query loop
            }
        }
    }

    /**
     * Delete handler. Asks for id, deletes it if exists, prints deleted JSON or
     * error JSON.
     */
    private void handleWarehouseDelete() {
        this.println("");
        this.println("Delete Warehouse");
        String id = this.readLine("Enter id: ");
        String json = this.warehouseRepo.delete(id);
        this.println("Deleted (or error):");
        this.println(json);
    }

    private void handleEquipmentAdd() {
        while (true) {
            this.println("");
            this.println("=== Equipment Add ===");
            String idStr = this.readLine("Please enter a unique equipment id: ");
            Integer id = this.parseInt(idStr);
            if (id == null) {
                this.println("[Input Error] Equipment id must be an integer. Exiting...");
                return;
            }
            String name = this.readLine("Please enter the equipment name: ");
            this.equipmentRepo.AddEquipment(id, name);
            this.println("Success! Equipment " + name + " added with id " + id + ".");
            return;
        }
    }

    private void handleEquipmentDelivery() {
        while (true) {
            this.println("");
            this.println("=== Equipment Delivery ===");
            String idStr = this.readLine("Please enter the unique equipment id: ");
            Integer idItem = this.parseInt(idStr);
            if (idItem == null) {
                this.println("[Input Error] Equipment id must be an integer. Exiting...");
                return;
            }
            idStr = this.readLine("Please enter the drone id: ");
            Integer idDrone = this.parseInt(idStr);
                if (idDrone == null) {
                this.println("[Input Error] Drone id must be an integer. Exiting...");
                return;
            }
            String date = this.readLine("Please enter the delivery date (MM/DD/YYYY): ");
            this.equipmentRepo.DeliverEquipment(idItem, idDrone, date);
            this.println("Exiting...");
            return;
        }
    }

    private void handleEquipmentPickup() {
        while (true) {
            this.println("");
            this.println("=== Equipment Pickup ===");
            String idStr = this.readLine("Please enter the unique equipment id: ");
            Integer idItem = this.parseInt(idStr);
            if (idItem == null) {
                this.println("[Input Error] Equipment id must be an integer. Exiting...");
                return;
            }
            idStr = this.readLine("Please enter the drone id: ");
            Integer idDrone = this.parseInt(idStr);
                if (idDrone == null) {
                this.println("[Input Error] Drone id must be an integer. Exiting...");
                return;
            }
            String date = this.readLine("Please enter the pickup date (MM/DD/YYYY): ");
            this.equipmentRepo.DeliverEquipment(idItem, idDrone, date);
            this.println("Exiting...");
            return;
        }
    }

    private void handleEquipmentRent() {
        while (true) {
            this.println("");
            this.println("=== Equipment Rent ===");
            String idStr = this.readLine("Please enter a unique equipment id: ");
            Integer id = this.parseInt(idStr);
            if (id == null) {
                this.println("[Input Error] Equipment id must be an integer. Exiting...");
                return;
            }
            String name = this.equipmentRepo.RentEquipment(id);
            if (name.equals("Invalid")) {
                this.println("[Input Error] This id does not correspond with any equipment. Exiting...");
            } else {
                this.println("Success! Equipment " + name + " rented with id " + id + ".");
            }
            return;
        }
    }

    private void handleEquipmentReturn() {
        this.println("");
        this.println("=== Return Equipment ===");
        String idStr = this.readLine("Enter id of the equipment to return: ");
        Integer id = this.parseInt(idStr);
            if (id == null) {
                this.println("[Input Error] Equipment id must be an integer. Exiting...");
                return;
            }
        this.equipmentRepo.ReturnEquipment(id);
        this.println("Exiting...");
    }

    // ============================== Helpers ==============================

    /**
     * Reads one line from console, trims it, never returns null.
     */
    private String readLine(String prompt) {
        System.out.print(prompt);
        String s = this.in.nextLine();
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    /**
     * Convenience printing wrapper.
     */
    private void println(String s) {
        System.out.println(s);
    }

    /**
     * Parses input of the form {a1, a2, ...} into a list of trimmed strings.
     * Returns null if the format is invalid (missing leading '{' or trailing
     * '}'). Note: This simple parser splits by comma and does not handle nested
     * braces or escaping.
     */
    private List<String> parseBraceList(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (!input.startsWith("{") || !input.endsWith("}")) {
            return null;
        }
        String inner = input.substring(1, input.length() - 1).trim();
        List<String> out = new ArrayList<String>();
        if (inner.length() == 0) {
            return out;
        }
        String[] parts = inner.split(",");
        for (int i = 0; i < parts.length; i++) {
            out.add(parts[i].trim());
        }
        return out;
    }

    /**
     * Parses an integer from a string; returns null on failure.
     */
    private Integer parseInt(String s) {
        if (s == null) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
