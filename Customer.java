
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class represents a customer who rents event items.
 * It stores the customer's ID, name, phone number, and email.
 *
 * The class demonstrates encapsulation, constructors,
 * access specifiers, input validation, and method overriding.
 *******************************************************************/

public class Customer {

    // Private customer properties
    private int customerId;
    private String name;
    private String phone;
    private String email;

    // Constructor initializes customer information
    public Customer(int customerId, String name,
                    String phone, String email) {

        setCustomerId(customerId);
        setName(name);
        setPhone(phone);
        setEmail(email);
    }

    // Getter and setter for customer ID
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException(
                "Customer ID must be greater than zero.");
        }

        this.customerId = customerId;
    }

    // Getter and setter for customer name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Customer name cannot be empty.");
        }

        this.name = name.trim();
    }

    // Getter and setter for phone number
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Phone number cannot be empty.");
        }

        this.phone = phone.trim();
    }

    // Getter and setter for email address
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")
                || !email.contains(".")) {
            throw new IllegalArgumentException(
                "Please enter a valid email address.");
        }

        this.email = email.trim();
    }

    // Display customer information
    @Override
    public String toString() {
        return "Customer ID: " + customerId
                + "\nCustomer Name: " + name
                + "\nPhone Number: " + phone
                + "\nEmail Address: " + email;
    }
}
