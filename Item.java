

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
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setItemLocation(String itemLocation)
    {
        this.itemLocation = itemLocation;
    }
    
    public String getItemLocation()
    {
        return itemLocation;
    }
}
