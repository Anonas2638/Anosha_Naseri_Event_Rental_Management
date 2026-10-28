
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/28/2026
 * Assignment: SDC330 Week 4 Course Project - Database Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class manages customers, rental inventory, and rental
 * orders using SQLite database storage.
 *
 * It implements RentalServiceInterface to demonstrate
 * abstraction and provides methods for creating, reading,
 * updating, and deleting rental information.
 *
 * Unlike Week 3, information is saved permanently in SQLite
 * rather than being stored only in temporary ArrayLists.
 *******************************************************************/

import java.util.List;

public class RentalService implements RentalServiceInterface {

    // Database manager handles SQLite operations.
    private DatabaseManager database;

    // Constructor connects to SQLite and creates tables.
    public RentalService() {
        database = new DatabaseManager();
        database.connect();
        database.createTables();
    }

    // CREATE: Add a customer to the database.
    @Override
    public void addCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null.");
        }

        if (findCustomer(customer.getCustomerId()) != null) {
            throw new IllegalArgumentException(
                    "A customer with this ID already exists.");
        }

        database.insertCustomer(customer);
    }

    // READ: Retrieve all registered customers.
    @Override
    public List<Customer> listCustomers() {
        return database.selectCustomers();
    }

    // READ: Find a customer using their ID.
    @Override
    public Customer findCustomer(int customerId) {

        for (Customer customer : listCustomers()) {

            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    // CREATE: Add a rental item to inventory.
    @Override
    public void addRentalItem(RentalItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Rental item cannot be null.");
        }

        if (findRentalItem(item.getItemId()) != null) {
            throw new IllegalArgumentException(
                    "A rental item with this ID already exists.");
        }

        database.insertRentalItem(item);
    }

    // READ: Retrieve all rental inventory items.
    @Override
    public List<RentalItem> listItems() {
        return database.selectItems();
    }

    // READ: Find a rental item using its ID.
    @Override
    public RentalItem findRentalItem(int itemId) {

        for (RentalItem item : listItems()) {

            if (item.getItemId() == itemId) {
                return item;
            }
        }

        return null;
    }

    // CREATE: Save a new rental order.
    @Override
    public void createOrder(RentalOrder order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Rental order cannot be null.");
        }

        if (findOrder(order.getOrderId()) != null) {
            throw new IllegalArgumentException(
                    "An order with this ID already exists.");
        }

        if (findCustomer(
                order.getCustomer().getCustomerId()) == null) {

            throw new IllegalArgumentException(
                    "The customer must be registered "
                    + "before creating an order.");
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "An order must contain at least one rental item.");
        }

        // Verify that every item exists in inventory.
        for (RentalItem item : order.getItems()) {

            if (findRentalItem(item.getItemId()) == null) {
                throw new IllegalArgumentException(
                        "Rental item " + item.getItemId()
                        + " is not registered in inventory.");
            }
        }

        database.insertOrder(order);
    }

    // READ: Retrieve all saved rental orders.
    @Override
    public List<RentalOrder> listOrders() {
        return database.selectOrders();
    }

    // READ: Find an order by its ID.
    @Override
    public RentalOrder findOrder(int orderId) {

        for (RentalOrder order : listOrders()) {

            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // UPDATE: Change an existing order's status.
    @Override
    public boolean updateOrder(int orderId, String newStatus) {

        RentalOrder order = findOrder(orderId);

        if (order == null) {
            return false;
        }

        // Validate status using the RentalOrder setter.
        order.setStatus(newStatus);

        // Save the updated status in SQLite.
        database.updateOrder(order);

        return true;
    }

    // DELETE: Remove an existing rental order.
    @Override
    public boolean deleteOrder(int orderId) {

        RentalOrder order = findOrder(orderId);

        if (order == null) {
            return false;
        }

        // Delete the order from SQLite.
        database.deleteOrder(orderId);

        return true;
    }

    // Close the database connection when finished.
    public void closeDatabase() {
        database.close();
    }
}
