
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This is the main application for the Event Rental
 * Management System.
 *
 * It provides a terminal menu for managing customers,
 * viewing rental inventory, creating rental orders,
 * viewing orders, updating orders, and deleting orders.
 *
 * Week 3 uses in-memory data. SQLite database support
 * will be implemented during Week 4.
 *******************************************************************/

import java.util.Scanner;

public class App {

    // Private application properties
    private static Scanner input = new Scanner(System.in);
    private static RentalService service = new RentalService();

    public static void main(String[] args) {

        // Add sample rental products to the inventory
        loadSampleItems();

        System.out.println("==========================================");
        System.out.println("Anosha Naseri - Week 3 Class Implementation");
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

            } catch (IllegalArgumentException e) {
                System.out.println("\nError: " + e.getMessage());
            }
        }

        input.close();
    }

    // Display the main application menu
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

    // Read a valid integer without crashing on text input
    private static int readChoice() {

        String value = input.nextLine();

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Read an integer ID from the terminal
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

    // Load sample rental products
    private static void loadSampleItems() {

        service.addRentalItem(new FlowerWall(
            101, "White Rose Flower Wall",
            250.00, true, "8 ft x 8 ft"
        ));

        service.addRentalItem(new PhotoBooth(
            102, "Magic Mirror Photo Booth",
            450.00, true, "Interactive Magic Mirror"
        ));

        service.addRentalItem(new EventCart(
            103, "Elegant White Candy Cart",
            175.00, true, "Candy and Dessert"
        ));

        service.addRentalItem(new EventCart(
            104, "Luxury Flower Cart",
            200.00, true, "Flower Bouquet"
        ));
    }

    // Add a customer
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

    // View all customers
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

    // View available rental inventory
    private static void viewRentalItems() {

        System.out.println("\n========== RENTAL INVENTORY ==========");

        for (RentalItem item : service.listItems()) {

            // Polymorphism: each subclass provides its description
            System.out.println("\n" + item);
            System.out.println("--------------------------------");
        }
    }

    // Create a rental order
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
                order.addItem(item);
                System.out.println(
                    item.getName() + " added to the order!");
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

    // View all rental orders
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

    // Update an order's status
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

    // Delete an existing order
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
