
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This abstract class represents a general rental item.
 * It stores common information such as the item ID, name,
 * category, daily rental price, and availability.
 *
 * It implements the Rentable interface and provides
 * shared methods for the different rental item types.
 *
 * FlowerWall, PhotoBooth, and EventCart will extend
 * this class to demonstrate inheritance and polymorphism.
 *******************************************************************/

public abstract class RentalItem implements Rentable {

    // Protected ID allows subclasses to access the item ID.
    protected int itemId;

    // Private properties demonstrate encapsulation.
    private String name;
    private String category;
    private double dailyPrice;
    private boolean available;

    // Constructor initializes rental item information.
    public RentalItem(int itemId, String name, String category,
                      double dailyPrice, boolean available) {

        this.itemId = itemId;
        setName(name);
        setCategory(category);
        setDailyPrice(dailyPrice);
        setAvailable(available);
    }

    // Getter and setter for item ID
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Rental item name cannot be empty.");
        }
        this.name = name;
    }

    // Getter and setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Rental item category cannot be empty.");
        }
        this.category = category;
    }

    // Getter and setter for daily rental price
    public double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(double dailyPrice) {
        if (!Double.isFinite(dailyPrice) || dailyPrice < 0) {
            throw new IllegalArgumentException(
                "Rental price must be a valid non-negative number.");
        }
        this.dailyPrice = dailyPrice;
    }

    // Getter and setter for availability
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Implements the Rentable interface.
    @Override
    public double getRentalPrice() {
        return dailyPrice;
    }

    // Subclasses provide their own descriptions.
    @Override
    public abstract String getDescription();

    // Displays rental item information.
    @Override
    public String toString() {
        return "Item ID: " + itemId
                + "\nName: " + name
                + "\nCategory: " + category
                + "\nDaily Rental Price: $"
                + String.format("%.2f", dailyPrice)
                + "\nAvailability: "
                + (available ? "Available" : "Unavailable")
                + "\nDescription: " + getDescription();
    }
}
