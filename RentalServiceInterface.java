
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/28/2026
 * Assignment: SDC330 Week 4 Course Project
 * Title: Database Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This interface defines the operations required for managing
 * customers, rental inventory, and rental orders.
 *
 * ABSTRACTION:
 * The interface separates the application's business operations
 * from their implementation. This allows different storage
 * implementations, such as SQLite or another database system.
 *******************************************************************/

import java.util.List;

public interface RentalServiceInterface {

    // CUSTOMER OPERATIONS

    // Add a customer to the system.
    void addCustomer(Customer customer);

    // Retrieve all registered customers.
    List<Customer> listCustomers();

    // Find a customer using their unique ID.
    Customer findCustomer(int customerId);


    // RENTAL INVENTORY OPERATIONS

    // Add a rental item to inventory.
    void addRentalItem(RentalItem item);

    // Retrieve all available rental items.
    List<RentalItem> listItems();

    // Find a rental item using its unique ID.
    RentalItem findRentalItem(int itemId);


    // RENTAL ORDER OPERATIONS

    // Create a new rental order.
    void createOrder(RentalOrder order);

    // Retrieve all rental orders.
    List<RentalOrder> listOrders();

    // Find an order using its unique ID.
    RentalOrder findOrder(int orderId);

    // Update the status of an existing order.
    boolean updateOrder(int orderId, String newStatus);

    // Delete an existing rental order.
    boolean deleteOrder(int orderId);
}
