
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class represents a photo booth available for rent.
 * It extends the abstract RentalItem superclass and
 * stores additional information about the booth type.
 *
 * The class demonstrates inheritance, encapsulation,
 * constructors, and method overriding.
 *******************************************************************/

public class PhotoBooth extends RentalItem {

    // Private property for photo booth type
    private String boothType;

    // Constructor
    public PhotoBooth(int itemId, String name, double dailyPrice,
                      boolean available, String boothType) {

        // Call the RentalItem superclass constructor
        super(itemId, name, "PhotoBooth", dailyPrice, available);

        setBoothType(boothType);
    }

    // Public getter for booth type
    public String getBoothType() {
        return boothType;
    }

    // Public setter for booth type
    public void setBoothType(String boothType) {

        if (boothType == null || boothType.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Photo booth type cannot be empty.");
        }

        this.boothType = boothType;
    }

    // Override the description method from RentalItem
    @Override
    public String getDescription() {
        return getName() + " - " + boothType
                + " photo booth rental, perfect for capturing "
                + "memories at weddings, birthdays, and events.";
    }
}
