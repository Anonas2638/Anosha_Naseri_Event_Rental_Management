
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/28/2026
 * Assignment: SDC330 Week 4 Course Project - Database Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class manages the SQLite database connection and provides
 * CRUD operations for customers, rental items, and rental orders.
 *
 * It uses JDBC, prepared statements, foreign keys, and
 * transactions to store rental information permanently.
 *******************************************************************/

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DATABASE_URL =
            "jdbc:sqlite:AnoshaNaseriEventRentals.db";

    private Connection connection;

    // Constructor
    public DatabaseManager() {
        connection = null;
    }

    // Connect to SQLite
    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);

            // Enable foreign key constraints for this connection.
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }

            System.out.println("SQLite database connected successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException(
                    "Unable to connect to SQLite database.", e);
        }
    }

    // Return the active connection.
    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database connection is unavailable.", e);
        }
    }

    // Create the four database tables.
    public void createTables() {

        String customersSQL =
                "CREATE TABLE IF NOT EXISTS Customers ("
                + "CustomerID INTEGER PRIMARY KEY, "
                + "Name TEXT NOT NULL, "
                + "Phone TEXT NOT NULL, "
                + "Email TEXT NOT NULL)";

        String itemsSQL =
                "CREATE TABLE IF NOT EXISTS RentalItems ("
                + "ItemID INTEGER PRIMARY KEY, "
                + "Name TEXT NOT NULL, "
                + "Category TEXT NOT NULL, "
                + "DailyPrice REAL NOT NULL CHECK(DailyPrice >= 0), "
                + "Available INTEGER NOT NULL CHECK(Available IN (0, 1)), "
                + "Dimensions TEXT, "
                + "BoothType TEXT, "
                + "CartType TEXT)";

        String ordersSQL =
                "CREATE TABLE IF NOT EXISTS RentalOrders ("
                + "OrderID INTEGER PRIMARY KEY, "
                + "CustomerID INTEGER NOT NULL, "
                + "EventDate TEXT NOT NULL, "
                + "Status TEXT NOT NULL, "
                + "FOREIGN KEY(CustomerID) REFERENCES Customers(CustomerID))";

        String orderItemsSQL =
                "CREATE TABLE IF NOT EXISTS OrderItems ("
                + "OrderID INTEGER NOT NULL, "
                + "ItemID INTEGER NOT NULL, "
                + "PRIMARY KEY(OrderID, ItemID), "
                + "FOREIGN KEY(OrderID) REFERENCES RentalOrders(OrderID) "
                + "ON DELETE CASCADE, "
                + "FOREIGN KEY(ItemID) REFERENCES RentalItems(ItemID))";

        try (Statement statement = getConnection().createStatement()) {

            statement.execute(customersSQL);
            statement.execute(itemsSQL);
            statement.execute(ordersSQL);
            statement.execute(orderItemsSQL);

            System.out.println("Database tables created successfully.");

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to create database tables.", e);
        }
    }

    // CREATE: Insert a new customer.
    public void insertCustomer(Customer customer) {

        String sql =
                "INSERT INTO Customers "
                + "(CustomerID, Name, Phone, Email) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setInt(1, customer.getCustomerId());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to insert customer.", e);
        }
    }

    // READ: Retrieve all customers.
    public List<Customer> selectCustomers() {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM Customers ORDER BY CustomerID";

        try (Statement statement = getConnection().createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            while (results.next()) {

                Customer customer = new Customer(
                        results.getInt("CustomerID"),
                        results.getString("Name"),
                        results.getString("Phone"),
                        results.getString("Email"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to retrieve customers.", e);
        }

        return customers;
    }

    // CREATE: Insert a rental item.
    public void insertRentalItem(RentalItem item) {

        String sql =
                "INSERT INTO RentalItems "
                + "(ItemID, Name, Category, DailyPrice, Available, "
                + "Dimensions, BoothType, CartType) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String dimensions = null;
        String boothType = null;
        String cartType = null;

        if (item instanceof FlowerWall) {
            dimensions = ((FlowerWall) item).getDimensions();
        } else if (item instanceof PhotoBooth) {
            boothType = ((PhotoBooth) item).getBoothType();
        } else if (item instanceof EventCart) {
            cartType = ((EventCart) item).getCartType();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported rental item type.");
        }

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setInt(1, item.getItemId());
            statement.setString(2, item.getName());
            statement.setString(3, item.getCategory());
            statement.setDouble(4, item.getDailyPrice());
            statement.setInt(5, item.isAvailable() ? 1 : 0);
            statement.setString(6, dimensions);
            statement.setString(7, boothType);
            statement.setString(8, cartType);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to insert rental item.", e);
        }
    }

    // READ: Retrieve all rental items.
    public List<RentalItem> selectItems() {

        List<RentalItem> items = new ArrayList<>();

        String sql = "SELECT * FROM RentalItems ORDER BY ItemID";

        try (Statement statement = getConnection().createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            while (results.next()) {
                items.add(createRentalItem(results));
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to retrieve rental items.", e);
        }

        return items;
    }

    // Reconstruct the correct subclass from a database row.
    private RentalItem createRentalItem(ResultSet results)
            throws SQLException {

        int itemId = results.getInt("ItemID");
        String name = results.getString("Name");
        String category = results.getString("Category");
        double price = results.getDouble("DailyPrice");
        boolean available = results.getInt("Available") == 1;

        switch (category) {

            case "FlowerWall":
                return new FlowerWall(
                        itemId, name, price, available,
                        results.getString("Dimensions"));

            case "PhotoBooth":
                return new PhotoBooth(
                        itemId, name, price, available,
                        results.getString("BoothType"));

            case "EventCart":
                return new EventCart(
                        itemId, name, price, available,
                        results.getString("CartType"));

            default:
                throw new IllegalStateException(
                        "Unknown rental category: " + category);
        }
    }

    // CREATE: Insert an order and its rental items.
    public void insertOrder(RentalOrder order) {

        String orderSQL =
                "INSERT INTO RentalOrders "
                + "(OrderID, CustomerID, EventDate, Status) "
                + "VALUES (?, ?, ?, ?)";

        String itemSQL =
                "INSERT INTO OrderItems (OrderID, ItemID) VALUES (?, ?)";

        Connection conn = getConnection();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStatement =
                         conn.prepareStatement(orderSQL);
                 PreparedStatement itemStatement =
                         conn.prepareStatement(itemSQL)) {

                orderStatement.setInt(1, order.getOrderId());
                orderStatement.setInt(
                        2, order.getCustomer().getCustomerId());
                orderStatement.setString(3, order.getEventDate());
                orderStatement.setString(4, order.getStatus());

                orderStatement.executeUpdate();

                for (RentalItem item : order.getItems()) {
                    itemStatement.setInt(1, order.getOrderId());
                    itemStatement.setInt(2, item.getItemId());
                    itemStatement.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {

            try {
                conn.rollback();
            } catch (SQLException rollbackError) {
                e.addSuppressed(rollbackError);
            }

            throw new IllegalStateException(
                    "Unable to insert rental order.", e);

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new IllegalStateException(
                        "Unable to reset database transaction.", e);
            }
        }
    }

    // READ: Retrieve orders with their customers and rental items.
    public List<RentalOrder> selectOrders() {

        List<RentalOrder> orders = new ArrayList<>();

        String sql =
                "SELECT OrderID, CustomerID, EventDate, Status "
                + "FROM RentalOrders ORDER BY OrderID";

        try (Statement statement = getConnection().createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            while (results.next()) {

                int orderId = results.getInt("OrderID");
                int customerId = results.getInt("CustomerID");

                Customer customer = findCustomerInDatabase(customerId);

                if (customer == null) {
                    throw new IllegalStateException(
                            "Customer not found for order " + orderId);
                }

                RentalOrder order = new RentalOrder(
                        orderId,
                        customer,
                        results.getString("EventDate"),
                        results.getString("Status"));

                loadOrderItems(order);

                orders.add(order);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to retrieve rental orders.", e);
        }

        return orders;
    }

    // Find one customer in SQLite.
    private Customer findCustomerInDatabase(int customerId)
            throws SQLException {

        String sql =
                "SELECT * FROM Customers WHERE CustomerID = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet results = statement.executeQuery()) {

                if (results.next()) {
                    return new Customer(
                            results.getInt("CustomerID"),
                            results.getString("Name"),
                            results.getString("Phone"),
                            results.getString("Email"));
                }
            }
        }

        return null;
    }

    // Load the rental items belonging to one order.
    private void loadOrderItems(RentalOrder order)
            throws SQLException {

        String sql =
                "SELECT r.* FROM RentalItems r "
                + "INNER JOIN OrderItems oi ON r.ItemID = oi.ItemID "
                + "WHERE oi.OrderID = ? ORDER BY r.ItemID";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setInt(1, order.getOrderId());

            try (ResultSet results = statement.executeQuery()) {

                while (results.next()) {
                    RentalItem item = createRentalItem(results);

                    // addItem requires availability to be true.
                    // Temporarily allow reconstruction of saved orders.
                    boolean originalAvailability = item.isAvailable();
                    item.setAvailable(true);
                    order.addItem(item);
                    item.setAvailable(originalAvailability);
                }
            }
        }
    }

    // UPDATE: Change an existing order's status.
    public void updateOrder(RentalOrder order) {

        String sql =
                "UPDATE RentalOrders SET Status = ? WHERE OrderID = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setString(1, order.getStatus());
            statement.setInt(2, order.getOrderId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to update rental order.", e);
        }
    }

    // DELETE: Remove an order and its associated order items.
    public void deleteOrder(int orderId) {

        String sql = "DELETE FROM RentalOrders WHERE OrderID = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to delete rental order.", e);
        }
    }

    // Close the database connection.
    public void close() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("SQLite database connection closed.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to close database connection.", e);
        } finally {
            connection = null;
        }
    }
}
