
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class represents a customer's event rental order.
 * It stores the order ID, customer, event date, order
 * status, and a collection of rental items.
 *
 * It demonstrates composition by containing a Customer
 * object and a list of RentalItem objects.
 *
 * It also demonstrates constructors, encapsulation,
 * polymorphism, and methods for calculating order totals.
 *******************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class RentalOrder {

    // Private order properties
    private int orderId;
    private Customer customer;
    private String eventDate;
    private String status;
    private List<RentalItem> items;

    // Constructor initializes order information
    public RentalOrder(int orderId, Customer customer,
                       String eventDate, String status) {

        setOrderId(orderId);
        setCustomer(customer);
        setEventDate(eventDate);
        setStatus(status);

        // Initialize the rental item collection
        this.items = new ArrayList<>();
    }

    // Getter and setter for order ID
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException(
                "Order ID must be greater than zero.");
        }

        this.orderId = orderId;
    }

    // Getter and setter for customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                "An order must have a customer.");
        }

        this.customer = customer;
    }

    // Getter and setter for event date
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        if (eventDate == null) {
            throw new IllegalArgumentException(
                "Event date cannot be empty.");
        }

        try {
            LocalDate.parse(eventDate);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Event date must use YYYY-MM-DD format.");
        }

        this.eventDate = eventDate;
    }

    // Getter and setter for order status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null ||
                !(status.equalsIgnoreCase("Pending")
                || status.equalsIgnoreCase("Confirmed")
                || status.equalsIgnoreCase("Completed")
                || status.equalsIgnoreCase("Cancelled"))) {

            throw new IllegalArgumentException(
                "Status must be Pending, Confirmed, "
                + "Completed, or Cancelled.");
        }

        this.status = status;
    }

    // Return a copy of the rental item list
    public List<RentalItem> getItems() {
        return new ArrayList<>(items);
    }

    // Add a rental item to the order
    public void addItem(RentalItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                "Cannot add a null rental item.");
        }

        if (!item.isAvailable()) {
            throw new IllegalArgumentException(
                "This rental item is currently unavailable.");
        }

        items.add(item);
    }

    // Remove a rental item using its ID
    public boolean removeItem(int itemId) {
        return items.removeIf(
            item -> item.getItemId() == itemId
        );
    }

    // Calculate the total rental price
    public double calculateTotal() {

        double total = 0.0;

        // Polymorphism: each item uses getRentalPrice()
        for (RentalItem item : items) {
            total += item.getRentalPrice();
        }

        return total;
    }

    // Display complete order information
    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append("\n========== RENTAL ORDER ==========\n");
        result.append("Order ID: ").append(orderId).append("\n");
        result.append("Customer: ").append(customer.getName()).append("\n");
        result.append("Event Date: ").append(eventDate).append("\n");
        result.append("Order Status: ").append(status).append("\n");

        result.append("\nRental Items:\n");

        if (items.isEmpty()) {
            result.append("No rental items added.\n");
        } else {
            for (RentalItem item : items) {
                result.append("- ")
                      .append(item.getName())
                      .append(" ($")
                      .append(String.format("%.2f",
                              item.getRentalPrice()))
                      .append(")\n");
            }
        }

        result.append("\nTotal Rental Cost: $")
              .append(String.format("%.2f", calculateTotal()))
              .append("\n");

        result.append("==================================");

        return result.toString();
    }
}
