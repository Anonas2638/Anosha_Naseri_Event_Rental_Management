
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class provides the database management structure
 * for the Event Rental Management System.
 *
 * It will manage SQLite database connections, table
 * creation, and CRUD operations for customers, rental
 * items, and rental orders.
 *
 * Database functionality will be implemented in Week 4.
 *******************************************************************/

import java.sql.Connection;
import java.util.List;

public class DatabaseManager {

    // Private database connection
    private Connection connection;

    // Constructor
    public DatabaseManager() {
        connection = null;
    }

    // Connect to the SQLite database
    public void connect() {
        // Week 4: Implement SQLite JDBC connection.
    }

    // Create database tables
    public void createTables() {
        // Week 4: Create Customers, RentalItems,
        // RentalOrders, and OrderItems tables.
    }

    // Insert a customer into the database
    public void insertCustomer(Customer customer) {
        // Week 4: Implement customer INSERT operation.
    }

    // Retrieve customers from the database
    public List<Customer> selectCustomers() {
        // Week 4: Implement customer SELECT operation.
        throw new UnsupportedOperationException(
            "Database operations will be implemented in Week 4.");
    }

    // Retrieve rental items from the database
    public List<RentalItem> selectItems() {
        // Week 4: Implement rental item SELECT operation.
        throw new UnsupportedOperationException(
            "Database operations will be implemented in Week 4.");
    }

    // Insert a rental order into the database
    public void insertOrder(RentalOrder order) {
        // Week 4: Implement order INSERT operation.
    }

    // Retrieve rental orders from the database
    public List<RentalOrder> selectOrders() {
        // Week 4: Implement order SELECT operation.
        throw new UnsupportedOperationException(
            "Database operations will be implemented in Week 4.");
    }

    // Update an existing rental order
    public void updateOrder(RentalOrder order) {
        // Week 4: Implement order UPDATE operation.
    }

    // Delete a rental order from the database
    public void deleteOrder(int orderId) {
        // Week 4: Implement order DELETE operation.
    }

    // Close the database connection
    public void close() {
        // Week 4: Close the SQLite JDBC connection.
    }
}
