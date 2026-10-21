
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class manages customers, rental inventory, and
 * rental orders for the Event Rental Management System.
 *
 * It provides methods for adding customers, viewing rental
 * items, creating orders, updating orders, and deleting orders.
 *
 * Week 3 uses in-memory ArrayLists to demonstrate business
 * functionality. SQLite persistence will be added in Week 4.
 *******************************************************************/

import java.util.ArrayList;
import java.util.List;

public class RentalService {

    // Private collections for temporary Week 3 storage
    private List<Customer> customers;
    private List<RentalItem> rentalItems;
    private List<RentalOrder> orders;

    // Database manager reserved for Week 4
    private DatabaseManager database;

    // Constructor
    public RentalService() {
        customers = new ArrayList<>();
        rentalItems = new ArrayList<>();
        orders = new ArrayList<>();

        database = new DatabaseManager();
    }

    // Add a new customer
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                "Customer cannot be null.");
        }

        for (Customer existing : customers) {
            if (existing.getCustomerId() == customer.getCustomerId()) {
                throw new IllegalArgumentException(
                    "A customer with this ID already exists.");
            }
        }

        customers.add(customer);
    }

    // Return all customers
    public List<Customer> listCustomers() {
        return new ArrayList<>(customers);
    }

    // Find a customer by ID
    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    // Add a rental item to inventory
    public void addRentalItem(RentalItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                "Rental item cannot be null.");
        }

        if (findRentalItem(item.getItemId()) != null) {
            throw new IllegalArgumentException(
                "A rental item with this ID already exists.");
        }

        rentalItems.add(item);
    }

    // Return all rental items
    public List<RentalItem> listItems() {
        return new ArrayList<>(rentalItems);
    }

    // Find a rental item by ID
    public RentalItem findRentalItem(int itemId) {
        for (RentalItem item : rentalItems) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }

        return null;
    }

    // Create a new rental order
    public void createOrder(RentalOrder order) {
        if (order == null) {
            throw new IllegalArgumentException(
                "Rental order cannot be null.");
        }

        if (findOrder(order.getOrderId()) != null) {
            throw new IllegalArgumentException(
                "An order with this ID already exists.");
        }

        if (findCustomer(order.getCustomer().getCustomerId()) == null) {
            throw new IllegalArgumentException(
                "The customer must be registered before creating an order.");
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                "An order must contain at least one rental item.");
        }

        orders.add(order);
    }

    // Return all rental orders
    public List<RentalOrder> listOrders() {
        return new ArrayList<>(orders);
    }

    // Find an order by ID
    public RentalOrder findOrder(int orderId) {
        for (RentalOrder order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // Update an existing order's status
    public boolean updateOrder(int orderId, String newStatus) {
        RentalOrder order = findOrder(orderId);

        if (order == null) {
            return false;
        }

        order.setStatus(newStatus);
        return true;
    }

    // Delete an existing order
    public boolean deleteOrder(int orderId) {
        RentalOrder order = findOrder(orderId);

        if (order == null) {
            return false;
        }

        return orders.remove(order);
    }
}
