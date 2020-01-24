

/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    public String description;
    private String itemLocation;

    /**
     * Constructor for objects of class Item
     */
    public Item(String description)
    {
        this.description = description;
    }

    /**
     * sets the location for the items
     * @param String the item location
     */
    public void setItemLocation(String itemLocation)
    {
        this.itemLocation = itemLocation;
    }
    
    /**
     * gives the location of the item
     * @return the item location
     */
    public String getItemLocation()
    {
        return itemLocation;
    }
}
