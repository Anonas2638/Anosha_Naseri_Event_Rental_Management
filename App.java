
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/28/2026
 * Assignment: SDC330 Week 4 Course Project - Database Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This is the main console application for the Event Rental
 * Management System.
 *
 * It provides menu options for managing customers, viewing
 * rental inventory, creating rental orders, viewing orders,
 * updating order statuses, and deleting orders.
 *
 * Week 4 integrates SQLite database storage through the
 * RentalService and DatabaseManager classes.
 *
 * Customer, inventory, and rental order information is saved
 * permanently and can be retrieved after restarting the program.
 *******************************************************************/

import java.util.Scanner;

public class App {

    // Application input and rental management service.
    private static final Scanner input = new Scanner(System.in);
    private static final RentalService service = new RentalService();

    public static void main(String[] args) {

        try {

            // Load sample inventory only if it does not already exist.
            loadSampleItems();

            System.out.println("==========================================");
            System.out.println("Anosha Naseri - Week 4 Database Implementation");
            System.out.println("EVENT RENTAL MANAGEMENT SYSTEM");
            System.out.println("==========================================");

            boolean running = true;

            while (running) {

                showMenu();
                int choice = readChoice();

                try {

                    switch (choice) {

                        case 1:
                            addCustomer();
                            break;

                        case 2:
                            viewCustomers();
                            break;

                        case 3:
                            viewRentalItems();
                            break;

                        case 4:
                            createRentalOrder();
                            break;

                        case 5:
                            viewOrders();
                            break;

                        case 6:
                            updateOrder();
                            break;

                        case 7:
                            deleteOrder();
                            break;

                        case 8:
                            System.out.println(
                                "\nThank you for using the Event Rental Management System!");
                            running = false;
                            break;

                        default:
                            System.out.println(
                                "\nInvalid selection. Please choose 1 through 8.");
                    }

                } catch (IllegalArgumentException
                         | IllegalStateException e) {

                    System.out.println("\nError: " + e.getMessage());
                }
            }

        } finally {

            // Close database and scanner when the program ends.
            service.closeDatabase();
            input.close();
        }
    }

    // Display the application menu.
    private static void showMenu() {

        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Add Customer");
        System.out.println("2. View Customers");
        System.out.println("3. View Rental Items");
        System.out.println("4. Create Rental Order");
        System.out.println("5. View Rental Orders");
        System.out.println("6. Update Order Status");
        System.out.println("7. Delete Rental Order");
        System.out.println("8. Exit");
        System.out.println("===============================");
        System.out.print("Enter your choice: ");
    }

    // Read menu selection without crashing on text input.
    private static int readChoice() {

        String value = input.nextLine();

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Read a numeric ID.
    private static int readId(String prompt) {

        System.out.print(prompt);
        String value = input.nextLine();

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Please enter a valid numeric ID.");
        }
    }

    // Add sample rental items only when their IDs do not exist.
    private static void loadSampleItems() {

        if (service.findRentalItem(101) == null) {
            service.addRentalItem(new FlowerWall(
                101, "White Rose Flower Wall",
                250.00, true, "8 ft x 8 ft"
            ));
        }

        if (service.findRentalItem(102) == null) {
            service.addRentalItem(new PhotoBooth(
                102, "Magic Mirror Photo Booth",
                450.00, true, "Interactive Magic Mirror"
            ));
        }

        if (service.findRentalItem(103) == null) {
            service.addRentalItem(new EventCart(
                103, "Elegant White Candy Cart",
                175.00, true, "Candy and Dessert"
            ));
        }

        if (service.findRentalItem(104) == null) {
            service.addRentalItem(new EventCart(
                104, "Luxury Flower Cart",
                200.00, true, "Flower Bouquet"
            ));
        }
    }

    // CREATE: Add a new customer.
    private static void addCustomer() {

        System.out.println("\n========== ADD CUSTOMER ==========");

        int id = readId("Enter Customer ID: ");

        System.out.print("Enter Customer Name: ");
        String name = input.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = input.nextLine();

        System.out.print("Enter Email Address: ");
        String email = input.nextLine();

        Customer customer = new Customer(id, name, phone, email);

        service.addCustomer(customer);

        System.out.println("\nCustomer added successfully!");
    }

    // READ: View all registered customers.
    private static void viewCustomers() {

        System.out.println("\n========== CUSTOMER LIST ==========");

        if (service.listCustomers().isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : service.listCustomers()) {
            System.out.println("\n" + customer);
            System.out.println("--------------------------------");
        }
    }

    // READ: Display rental inventory.
    private static void viewRentalItems() {

        System.out.println("\n========== RENTAL INVENTORY ==========");

        if (service.listItems().isEmpty()) {
            System.out.println("No rental items found.");
            return;
        }

        for (RentalItem item : service.listItems()) {

            // Polymorphism: subclass-specific descriptions.
            System.out.println("\n" + item);
            System.out.println("--------------------------------");
        }
    }

    // CREATE: Save a new rental order.
    private static void createRentalOrder() {

        System.out.println("\n========== CREATE RENTAL ORDER ==========");

        int orderId = readId("Enter Order ID: ");
        int customerId = readId("Enter Customer ID: ");

        Customer customer = service.findCustomer(customerId);

        if (customer == null) {
            System.out.println(
                "Customer not found. Please add the customer first.");
            return;
        }

        System.out.print("Enter Event Date (YYYY-MM-DD): ");
        String eventDate = input.nextLine();

        RentalOrder order = new RentalOrder(
            orderId, customer, eventDate, "Pending"
        );

        boolean addingItems = true;

        while (addingItems) {

            viewRentalItems();

            int itemId = readId(
                "\nEnter Rental Item ID (0 to finish): "
            );

            if (itemId == 0) {
                addingItems = false;
                continue;
            }

            RentalItem item = service.findRentalItem(itemId);

            if (item == null) {
                System.out.println("Rental item not found.");
            } else {

                // Prevent duplicate rental item IDs in one order.
                boolean alreadyAdded = false;

                for (RentalItem existing : order.getItems()) {
                    if (existing.getItemId() == itemId) {
                        alreadyAdded = true;
                        break;
                    }
                }

                if (alreadyAdded) {
                    System.out.println(
                        "This item is already in the order.");
                } else {
                    order.addItem(item);
                    System.out.println(
                        item.getName() + " added to the order!");
                }
            }
        }

        if (order.getItems().isEmpty()) {
            System.out.println(
                "Order cancelled because no items were selected.");
            return;
        }

        service.createOrder(order);

        System.out.println("\nRental order created successfully!");
        System.out.println(order);
    }

    // READ: View all rental orders.
    private static void viewOrders() {

        System.out.println("\n========== RENTAL ORDERS ==========");

        if (service.listOrders().isEmpty()) {
            System.out.println("No rental orders found.");
            return;
        }

        for (RentalOrder order : service.listOrders()) {
            System.out.println(order);
        }
    }

    // UPDATE: Change the status of an existing order.
    private static void updateOrder() {

        System.out.println("\n========== UPDATE ORDER ==========");

        int orderId = readId("Enter Order ID: ");

        System.out.print(
            "Enter New Status (Pending, Confirmed, Completed, Cancelled): "
        );

        String status = input.nextLine();

        boolean updated = service.updateOrder(orderId, status);

        if (updated) {
            System.out.println("Order updated successfully!");
        } else {
            System.out.println("Order not found.");
        }
    }

    // DELETE: Remove an existing rental order.
    private static void deleteOrder() {

        System.out.println("\n========== DELETE ORDER ==========");

        int orderId = readId("Enter Order ID: ");

        boolean deleted = service.deleteOrder(orderId);

        if (deleted) {
            System.out.println("Rental order deleted successfully!");
        } else {
            System.out.println("Order not found.");
        }
    }
}
