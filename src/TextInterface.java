import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import repository.CustomerRepository;
import repository.EmployeeRepository;
import repository.EquipmentManagement;
import repository.OrderRepository;
import repository.WarehouseRepository;

/**
 * TextInterface - Console-based entrypoint. - Hosts repositories and presents
 * text menus. - Warehouse and Equipment parts keep original behavior; we only
 * ADD menus for Customer, Employee, and Order in the same style as Warehouse. -
 * Uses JSON strings returned by repositories for display. - No lambdas/streams;
 * Allman brace style.
 */
public class TextInterface {
    private final WarehouseRepository warehouseRepo = new WarehouseRepository();
    private final EquipmentManagement equipmentRepo = new EquipmentManagement();
    private final CustomerRepository customerRepo = new CustomerRepository();
    private final EmployeeRepository employeeRepo = new EmployeeRepository();
    private final OrderRepository orderRepo = new OrderRepository();

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
            } else if ("2".equals(choice)) {
                this.equipmentManagementMenu();
            } else if ("3".equals(choice)) {
                this.customerMenu();
            } else if ("4".equals(choice)) {
                this.employeeMenu();
            } else if ("5".equals(choice)) {
                this.orderMenu();
            } else if ("0".equals(choice)) {
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
        this.println("3. Customer");
        this.println("4. Employee");
        this.println("5. Order");
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

    // ============================== CRUD Handlers (Warehouse) ==============================

    private void handleWarehouseCreate() {
        this.println("");
        this.println("Create Warehouse");
        this.println("Please input attributes in format (with braces):");
        this.println(
                "{phoneNumber, city, zipCode, street, equipmentCapacity, droneCapacity, managerSSN}");
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
                continue; // run another query
            } else if ("4".equals(next)) {
                this.handleWarehouseDelete();
            } else if ("5".equals(next)) {
                return; // back to Warehouse menu
            } else {
                this.println("[Input Error] Unknown choice: " + next);
            }
        }
    }

    private void handleWarehouseDelete() {
        this.println("");
        this.println("Delete Warehouse");
        String id = this.readLine("Enter id: ");
        String json = this.warehouseRepo.delete(id);
        this.println("Deleted (or error):");
        this.println(json);
    }

    // ============================== Equipment handlers ==============================

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
                this.println(
                        "[Input Error] This id does not correspond with any equipment. Exiting...");
            } else {
                this.println(
                        "Success! Equipment " + name + " rented with id " + id + ".");
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

    // ============================== CUSTOMER MENU ==============================

    private void customerMenu() {
        while (true) {
            this.println("");
            this.println("=== Customer Table ===");
            this.println("Current data:");
            this.println(this.customerRepo.getAll());

            this.println("");
            this.println("Operations:");
            this.println("1. Create");
            this.println("2. Update");
            this.println("3. Query");
            this.println("4. Delete");
            this.println("5. Return to Main menu");

            String op = this.readLine("Enter 1/2/3/4/5: ");

            if ("1".equals(op)) {
                this.handleCustomerCreate();
            } else if ("2".equals(op)) {
                this.handleCustomerUpdate();
            } else if ("3".equals(op)) {
                this.handleCustomerQuery();
            } else if ("4".equals(op)) {
                this.handleCustomerDelete();
            } else if ("5".equals(op)) {
                return;
            } else {
                this.println("[Input Error] Unknown operation: " + op);
            }
        }
    }

    private void handleCustomerCreate() {
        this.println("");
        this.println("Create Customer");
        this.println(
                "Please input attributes in format {custStartDate, city, zipCode, street, email, phoneNumber, custName, type}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 8) {
            this.println("[Input Error] Expect 8 attributes inside braces.");
            return;
        }

        String json = this.customerRepo.create(p.get(0), p.get(1), p.get(2), p.get(3),
                p.get(4), p.get(5), p.get(6), p.get(7));
        this.println("Created:");
        this.println(json);
    }

    private void handleCustomerUpdate() {
        this.println("");
        this.println("Update Customer");
        String userId = this.readLine("Enter userId: ");

        this.println(
                "Please input attributes in format {custStartDate, city, zipCode, street, email, phoneNumber, custName, type}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 8) {
            this.println("[Input Error] Expect 8 attributes inside braces.");
            return;
        }

        String json = this.customerRepo.update(userId, p.get(0), p.get(1), p.get(2),
                p.get(3), p.get(4), p.get(5), p.get(6), p.get(7));
        this.println("Updated (or error):");
        this.println(json);
    }

    private void handleCustomerQuery() {
        while (true) {
            this.println("");
            this.println("Query by which field?");
            this.println(
                    "Options: city | zipCode | email | phoneNumber | custName | type | custStartDate");
            this.println("Or enter 9 to return to Customer menu.");
            String field = this.readLine("Field: ");

            if ("9".equals(field)) {
                return;
            }

            String res = null;
            if ("city".equals(field)) {
                res = this.customerRepo.queryByCity(this.readLine("Enter city: "));
            } else if ("zipCode".equals(field)) {
                res = this.customerRepo.queryByZipCode(this.readLine("Enter zipCode: "));
            } else if ("email".equals(field)) {
                res = this.customerRepo.queryByEmail(this.readLine("Enter email: "));
            } else if ("phoneNumber".equals(field)) {
                res = this.customerRepo
                        .queryByPhoneNumber(this.readLine("Enter phoneNumber: "));
            } else if ("custName".equals(field)) {
                res = this.customerRepo
                        .queryByCustName(this.readLine("Enter custName: "));
            } else if ("type".equals(field)) {
                res = this.customerRepo.queryByType(this.readLine("Enter type: "));
            } else if ("custStartDate".equals(field)) {
                res = this.customerRepo
                        .queryByCustStartDate(this.readLine("Enter custStartDate: "));
            } else {
                this.println("[Input Error] Unknown field: " + field);
                continue;
            }

            this.println("Query result:");
            this.println(res);

            this.println("");
            this.println("Next step:");
            this.println(
                    "1. Create   2. Update   3. Query   4. Delete   5. Back to Customer menu");
            String next = this.readLine("Enter 1/2/3/4/5: ");
            if ("1".equals(next)) {
                this.handleCustomerCreate();
            } else if ("2".equals(next)) {
                this.handleCustomerUpdate();
            } else if ("3".equals(next)) {
                continue;
            } else if ("4".equals(next)) {
                this.handleCustomerDelete();
            } else if ("5".equals(next)) {
                return;
            } else {
                this.println("[Input Error] Unknown choice: " + next);
            }
        }
    }

    private void handleCustomerDelete() {
        this.println("");
        this.println("Delete Customer");
        String userId = this.readLine("Enter userId: ");
        String json = this.customerRepo.delete(userId);
        this.println("Deleted (or error):");
        this.println(json);
    }

    // ============================== EMPLOYEE MENU ==============================

    private void employeeMenu() {
        while (true) {
            this.println("");
            this.println("=== Employee Table ===");
            this.println("Current data:");
            this.println(this.employeeRepo.getAll());

            this.println("");
            this.println("Operations:");
            this.println("1. Create");
            this.println("2. Update");
            this.println("3. Query");
            this.println("4. Delete");
            this.println("5. Return to Main menu");

            String op = this.readLine("Enter 1/2/3/4/5: ");

            if ("1".equals(op)) {
                this.handleEmployeeCreate();
            } else if ("2".equals(op)) {
                this.handleEmployeeUpdate();
            } else if ("3".equals(op)) {
                this.handleEmployeeQuery();
            } else if ("4".equals(op)) {
                this.handleEmployeeDelete();
            } else if ("5".equals(op)) {
                return;
            } else {
                this.println("[Input Error] Unknown operation: " + op);
            }
        }
    }

    private void handleEmployeeCreate() {
        this.println("");
        this.println("Create Employee");
        this.println(
                "Please input attributes in format {ssn, name, phoneNumber, sex, salary}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 5) {
            this.println("[Input Error] Expect 5 attributes inside braces.");
            return;
        }

        Integer salary = this.parseInt(p.get(4));
        if (salary == null) {
            this.println("[Input Error] salary must be an integer.");
            return;
        }

        String json = this.employeeRepo.create(p.get(0), p.get(1), p.get(2), p.get(3),
                salary);
        this.println("Created:");
        this.println(json);
    }

    private void handleEmployeeUpdate() {
        this.println("");
        this.println("Update Employee");
        String ssn = this.readLine("Enter ssn: ");

        this.println(
                "Please input attributes in format {name, phoneNumber, sex, salary}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 4) {
            this.println("[Input Error] Expect 4 attributes inside braces.");
            return;
        }

        Integer salary = this.parseInt(p.get(3));
        if (salary == null) {
            this.println("[Input Error] salary must be an integer.");
            return;
        }

        String json = this.employeeRepo.update(ssn, p.get(0), p.get(1), p.get(2), salary);
        this.println("Updated (or error):");
        this.println(json);
    }

    private void handleEmployeeQuery() {
        while (true) {
            this.println("");
            this.println("Query by which field?");
            this.println("Options: name | phoneNumber | sex | salary | salaryRange");
            this.println("Or enter 9 to return to Employee menu.");
            String field = this.readLine("Field: ");

            if ("9".equals(field)) {
                return;
            }

            String res = null;
            if ("name".equals(field)) {
                res = this.employeeRepo.queryByName(this.readLine("Enter name: "));
            } else if ("phoneNumber".equals(field)) {
                res = this.employeeRepo
                        .queryByPhoneNumber(this.readLine("Enter phoneNumber: "));
            } else if ("sex".equals(field)) {
                res = this.employeeRepo.queryBySex(this.readLine("Enter sex: "));
            } else if ("salary".equals(field)) {
                Integer s = this.parseInt(this.readLine("Enter salary (int): "));
                if (s == null) {
                    this.println("[Input Error] salary must be an integer.");
                    continue;
                }
                res = this.employeeRepo.queryBySalary(s);
            } else if ("salaryRange".equals(field)) {
                Integer min = this.parseInt(this.readLine("Enter min salary (int): "));
                Integer max = this.parseInt(this.readLine("Enter max salary (int): "));
                if (min == null || max == null) {
                    this.println("[Input Error] min/max must be integers.");
                    continue;
                }
                res = this.employeeRepo.queryBySalaryRange(min, max);
            } else {
                this.println("[Input Error] Unknown field: " + field);
                continue;
            }

            this.println("Query result:");
            this.println(res);

            this.println("");
            this.println("Next step:");
            this.println(
                    "1. Create   2. Update   3. Query   4. Delete   5. Back to Employee menu");
            String next = this.readLine("Enter 1/2/3/4/5: ");
            if ("1".equals(next)) {
                this.handleEmployeeCreate();
            } else if ("2".equals(next)) {
                this.handleEmployeeUpdate();
            } else if ("3".equals(next)) {
                continue;
            } else if ("4".equals(next)) {
                this.handleEmployeeDelete();
            } else if ("5".equals(next)) {
                return;
            } else {
                this.println("[Input Error] Unknown choice: " + next);
            }
        }
    }

    private void handleEmployeeDelete() {
        this.println("");
        this.println("Delete Employee");
        String ssn = this.readLine("Enter ssn: ");
        String json = this.employeeRepo.delete(ssn);
        this.println("Deleted (or error):");
        this.println(json);
    }

    // ============================== ORDER MENU ==============================

    private void orderMenu() {
        while (true) {
            this.println("");
            this.println("=== Order Table ===");
            this.println("Current data:");
            this.println(this.orderRepo.getAll());

            this.println("");
            this.println("Operations:");
            this.println("1. Create");
            this.println("2. Update");
            this.println("3. Query");
            this.println("4. Delete");
            this.println("5. Return to Main menu");

            String op = this.readLine("Enter 1/2/3/4/5: ");

            if ("1".equals(op)) {
                this.handleOrderCreate();
            } else if ("2".equals(op)) {
                this.handleOrderUpdate();
            } else if ("3".equals(op)) {
                this.handleOrderQuery();
            } else if ("4".equals(op)) {
                this.handleOrderDelete();
            } else if ("5".equals(op)) {
                return;
            } else {
                this.println("[Input Error] Unknown operation: " + op);
            }
        }
    }

    private void handleOrderCreate() {
        this.println("");
        this.println("Create Order");
        this.println(
                "Please input attributes in format {orderStartDate, estimatedArrivalDate, actualArrivalDate, dueDate, actualReturnDate, custUserId}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 6) {
            this.println("[Input Error] Expect 6 attributes inside braces.");
            return;
        }

        String json = this.orderRepo.create(p.get(0), p.get(1), p.get(2), p.get(3),
                p.get(4), p.get(5));
        this.println("Created:");
        this.println(json);
    }

    private void handleOrderUpdate() {
        this.println("");
        this.println("Update Order");
        String orderId = this.readLine("Enter orderId: ");

        this.println(
                "Please input attributes in format {orderStartDate, estimatedArrivalDate, actualArrivalDate, dueDate, actualReturnDate, custUserId}");
        String line = this.readLine("> ");

        List<String> p = this.parseBraceList(line);
        if (p == null || p.size() != 6) {
            this.println("[Input Error] Expect 6 attributes inside braces.");
            return;
        }

        String json = this.orderRepo.update(orderId, p.get(0), p.get(1), p.get(2),
                p.get(3), p.get(4), p.get(5));
        this.println("Updated (or error):");
        this.println(json);
    }

    private void handleOrderQuery() {
        while (true) {
            this.println("");
            this.println("Query by which field?");
            this.println(
                    "Options: custUserId | orderStartDate | estimatedArrivalDate | actualArrivalDate | dueDate | actualReturnDate");
            this.println("Or enter 9 to return to Order menu.");
            String field = this.readLine("Field: ");

            if ("9".equals(field)) {
                return;
            }

            String res = null;
            if ("custUserId".equals(field)) {
                res = this.orderRepo
                        .queryByCustUserId(this.readLine("Enter custUserId: "));
            } else if ("orderStartDate".equals(field)) {
                res = this.orderRepo
                        .queryByOrderStartDate(this.readLine("Enter orderStartDate: "));
            } else if ("estimatedArrivalDate".equals(field)) {
                res = this.orderRepo.queryByEstimatedArrivalDate(
                        this.readLine("Enter estimatedArrivalDate: "));
            } else if ("actualArrivalDate".equals(field)) {
                res = this.orderRepo.queryByActualArrivalDate(
                        this.readLine("Enter actualArrivalDate: "));
            } else if ("dueDate".equals(field)) {
                res = this.orderRepo.queryByDueDate(this.readLine("Enter dueDate: "));
            } else if ("actualReturnDate".equals(field)) {
                res = this.orderRepo.queryByActualReturnDate(
                        this.readLine("Enter actualReturnDate: "));
            } else {
                this.println("[Input Error] Unknown field: " + field);
                continue;
            }

            this.println("Query result:");
            this.println(res);

            this.println("");
            this.println("Next step:");
            this.println(
                    "1. Create   2. Update   3. Query   4. Delete   5. Back to Order menu");
            String next = this.readLine("Enter 1/2/3/4/5: ");
            if ("1".equals(next)) {
                this.handleOrderCreate();
            } else if ("2".equals(next)) {
                this.handleOrderUpdate();
            } else if ("3".equals(next)) {
                continue;
            } else if ("4".equals(next)) {
                this.handleOrderDelete();
            } else if ("5".equals(next)) {
                return;
            } else {
                this.println("[Input Error] Unknown choice: " + next);
            }
        }
    }

    private void handleOrderDelete() {
        this.println("");
        this.println("Delete Order");
        String orderId = this.readLine("Enter orderId: ");
        String json = this.orderRepo.delete(orderId);
        this.println("Deleted (or error):");
        this.println(json);
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
     * Returns null if the format is invalid.
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
