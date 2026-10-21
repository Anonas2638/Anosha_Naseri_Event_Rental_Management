
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class represents a flower wall available for rent.
 * It extends the abstract RentalItem superclass and
 * stores additional information about the wall dimensions.
 *
 * The class demonstrates inheritance, encapsulation,
 * constructors, and method overriding.
 *******************************************************************/

public class FlowerWall extends RentalItem {

    // Private property for flower wall dimensions
    private String dimensions;

    // Constructor
    public FlowerWall(int itemId, String name, double dailyPrice,
                      boolean available, String dimensions) {

        // Call the RentalItem superclass constructor
        super(itemId, name, "FlowerWall", dailyPrice, available);

        setDimensions(dimensions);
    }

    // Public getter for dimensions
    public String getDimensions() {
        return dimensions;
    }

    // Public setter for dimensions
    public void setDimensions(String dimensions) {

        if (dimensions == null || dimensions.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Flower wall dimensions cannot be empty.");
        }

        this.dimensions = dimensions;
    }

    // Override the description method from RentalItem
    @Override
    public String getDescription() {
        return getName() + " - Flower wall measuring "
                + dimensions + ", perfect for weddings, "
                + "birthdays, and special events.";
    }
}
