
/*******************************************************************
 * Name: Anosha Naseri
 * Date: 10/21/2026
 * Assignment: SDC330 Week 3 Course Project - Class Implementation
 * Project: Event Rental Management System
 *
 * Description:
 * This interface defines the common behaviors required
 * for all rentable items in the Event Rental Management
 * System.
 *
 * It requires implementing classes to provide a rental
 * price and a description of the rental item.
 *
 * This interface demonstrates abstraction and supports
 * polymorphism between different rental item types.
 *******************************************************************/

public interface Rentable {

    // Returns the rental price of an item
    double getRentalPrice();

    // Returns a description of the rental item
    String getDescription();

}
