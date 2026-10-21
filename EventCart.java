
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This class represents an event cart available for rent.
 * It extends the abstract RentalItem superclass and
 * stores additional information about the cart type.
 *
 * The class demonstrates inheritance, encapsulation,
 * constructors, and method overriding.
 *******************************************************************/

public class EventCart extends RentalItem {

    // Private property for the event cart type
    private String cartType;

    // Constructor initializes the event cart information
    public EventCart(int itemId, String name, double dailyPrice,
                     boolean available, String cartType) {

        // Call the RentalItem superclass constructor
        super(itemId, name, "EventCart", dailyPrice, available);

        setCartType(cartType);
    }

    // Public getter for cart type
    public String getCartType() {
        return cartType;
    }

    // Public setter for cart type
    public void setCartType(String cartType) {

        if (cartType == null || cartType.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Event cart type cannot be empty.");
        }

        this.cartType = cartType;
    }

    // Override the description method from RentalItem
    @Override
    public String getDescription() {
        return getName() + " - " + cartType
                + " cart rental, perfect for weddings, "
                + "birthdays, bridal showers, and special events.";
    }
}
